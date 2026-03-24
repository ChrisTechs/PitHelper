/*
 * PitHelper - A Hypixel The Pit Helper Mod.
 * Copyright (C) 2026 Christian Steenkamp
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.github.christechs.pithelper.features;

import io.github.christechs.pithelper.config.PitConfig;
import io.github.christechs.pithelper.data.HypixelGameMode;
import io.github.christechs.pithelper.utils.ServerState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.regex.Pattern;

public class QuickMathsHandler {

    private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-OR]");
    private static final Logger LOGGER = LogManager.getLogger("PitHelper");

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if (event.type == 2) return;

        if (PitConfig.general().onlyOnHypixel && (!ServerState.onHypixel || ServerState.currentGameMode != HypixelGameMode.PIT)) {
            return;
        }

        String message = STRIP_COLOR_PATTERN.matcher(event.message.getUnformattedText().toLowerCase()).replaceAll("");

        if (message.startsWith("quick maths! solve: ")) {
            String problem = message.substring(message.indexOf("solve: ") + 7).replace(" ", "").replace("x", "*");

            try {
                double rawResult = new ExpressionBuilder(problem).build().evaluate();
                String result = (rawResult == Math.floor(rawResult)) ? String.valueOf((long) rawResult) : String.valueOf(rawResult);
                handleAnswerFound(result);
            } catch (Exception e) {
                LOGGER.error("Failed to solve Quick Maths equation: {}", problem);
            }
        }
    }

    private void handleAnswerFound(String result) {
        Minecraft mc = Minecraft.getMinecraft();

        if (PitConfig.general().quickMathsClipboard) copyToClipboard(result);

        String alert = "§6§l[PitHelper] §eQuick Math Answer: §a§l" + result;
        if (PitConfig.general().quickMathsClipboard) alert += " §7(Copied to Clipboard)";
        mc.thePlayer.addChatMessage(new ChatComponentText(alert));

        if (PitConfig.general().quickMathsAutoOpenChat) {
            mc.addScheduledTask(() -> mc.displayGuiScreen(new GuiChat(result)));
        }
    }

    private void copyToClipboard(String text) {
        StringSelection selection = new StringSelection(text);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
    }
}