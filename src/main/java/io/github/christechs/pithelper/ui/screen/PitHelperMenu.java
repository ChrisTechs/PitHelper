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

package io.github.christechs.pithelper.ui.screen;

import io.github.christechs.clayj.ClayJ.*;
import io.github.christechs.clayj.config.ImageConfigBuilder;
import io.github.christechs.clayj.math.Dimensions;
import io.github.christechs.config.ConfigManager;
import io.github.christechs.pithelper.config.PitConfig;
import io.github.christechs.pithelper.data.EventFetcher;
import io.github.christechs.pithelper.data.PitEvent;
import io.github.christechs.pithelper.features.NotificationManager;
import io.github.christechs.pithelper.ui.base.AutoConfigScreen;
import io.github.christechs.pithelper.ui.base.IMenuTab;
import io.github.christechs.pithelper.ui.components.ClayComponents;
import io.github.christechs.pithelper.ui.hud.EventOverlay;
import io.github.christechs.pithelper.ui.utils.IconCache;
import io.github.christechs.pithelper.ui.utils.PitUIUtil;
import io.github.christechs.pithelper.ui.utils.ThemeManager;
import io.github.christechs.pithelper.ui.xml.ClayXmlEngine;
import io.github.christechs.pithelper.ui.xml.XmlContext;
import io.github.christechs.pithelper.ui.xml.nodes.XmlNode;
import io.github.christechs.pithelper.utils.ChatUtil;
import net.minecraft.client.gui.GuiScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.github.christechs.clayj.ClayJ.*;

public class PitHelperMenu extends AutoConfigScreen {

    public GuiScreen screenToOpen = null;

    public PitHelperMenu() {
        super("PitHelper");
        loadConfigCategories();
        addTab(new LiveEventsTab());
        addTab(new SocialTab());
        addTab(new DevToolsTab());
    }

    private static void bindThemeContext(XmlContext ctx) {
        ctx.bindString("theme.bg", () -> ThemeManager.getHex("background"));
        ctx.bindString("theme.sidebar", () -> ThemeManager.getHex("sidebar"));
        ctx.bindString("theme.element", () -> ThemeManager.getHex("element"));
        ctx.bindString("theme.text", () -> ThemeManager.getHex("foreground"));
        ctx.bindString("theme.text_dim", () -> ThemeManager.getHex("text_dim"));
        ctx.bindString("theme.accent", () -> ThemeManager.getHex("accent"));
    }

    @Override
    protected void onPostBuildLayout() {
        if (this.screenToOpen != null) {
            this.mc.displayGuiScreen(this.screenToOpen);
            this.screenToOpen = null;
        }
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        ConfigManager.save();
    }

    private static class LiveEventsTab implements IMenuTab {
        private final XmlNode xmlNode;
        private final XmlContext ctx;

        public LiveEventsTab() {
            xmlNode = ClayXmlEngine.load("ui/live_events_tab.xml");
            ctx = new XmlContext();
            bindThemeContext(ctx);

            ctx.bindAction("refreshEvents", EventFetcher::fetchAsync);

            ctx.bindCustomRenderer("EventList", () -> {
                try (ClayElement wrap = el(decl().layout(layout().widthGrow().heightFit().dirTopToBottom().gap(5)))) {
                    long currentTime = System.currentTimeMillis();
                    if (EventFetcher.activeEvent != null) {
                        PitEvent ae = EventFetcher.activeEvent;
                        long actualEnd = ae.timestamp + ae.eventType.startOffset + ae.eventType.duration;
                        if (currentTime < actualEnd) drawRow(ae, actualEnd - currentTime, true, 999);
                    }
                    if (EventFetcher.cachedEvents != null) {
                        for (int i = 0; i < EventFetcher.cachedEvents.size(); i++) {
                            PitEvent e = EventFetcher.cachedEvents.get(i);
                            long actualStart = e.timestamp + e.eventType.startOffset;
                            if (actualStart >= currentTime) {
                                drawRow(e, actualStart - currentTime, false, i);
                            }
                        }
                    }
                }
            });
        }

        @Override
        public String getName() {
            return "Live Events";
        }

        @Override
        public void draw() {
            if (xmlNode != null) xmlNode.render(ctx);
        }

        private void drawRow(PitEvent e, long timeDiff, boolean isActive, int index) {
            boolean rowHover = pointerOver("LiveRow_" + index);
            try (ClayElement row = el(decl()
                    .id("LiveRow_" + index)
                    .bg(isActive
                            ? ThemeManager.get(rowHover ? "accent_hover" : "accent", 180)
                            : ThemeManager.get(rowHover ? "element" : "background"))
                    .radius(4)
                    .layout(layout().widthGrow().heightFixed(36)
                            .dirLeftToRight()
                            .alignLeft().alignCenterY()
                            .padding(10, 10, 10, 10)
                            .gap(15)))) {

                try (ClayElement icon = el(decl()
                        .image(new ImageConfigBuilder().data(IconCache.get(e.event)).sourceDim(new Dimensions(16, 16)))
                        .layout(layout().widthFixed(16).heightFixed(16)))) {
                }

                text((isActive ? "ACTIVE: " : "") + e.event, txt().size(1).color(ThemeManager.get("foreground")));

                try (ClayElement spacer = el(decl().layout(layout().widthGrow().heightFixed(10)))) {
                }

                text(EventOverlay.formatTimeVerbose(timeDiff), txt().size(1).color(isActive ? ThemeManager.get("success") : ThemeManager.get("text_dim")));
            }
        }
    }

    private static class SocialTab implements IMenuTab {
        private final XmlNode xmlNode;
        private final XmlContext ctx;

        private boolean showFriends = true;
        private String socialInput = "";
        private int socialPage = 0;

        public SocialTab() {
            xmlNode = ClayXmlEngine.load("ui/social_tab.xml");
            ctx = new XmlContext();
            bindThemeContext(ctx);

            ctx.bindInput("socialInput", () -> socialInput, val -> socialInput = val);

            ctx.bindAction("addFriend", () -> {
                if (!socialInput.isEmpty()) {
                    net.minecraftforge.client.ClientCommandHandler.instance.executeCommand(
                            net.minecraft.client.Minecraft.getMinecraft().thePlayer, "/pitfriend " + socialInput);
                    socialInput = "";
                }
            });

            ctx.bindAction("addEnemy", () -> {
                if (!socialInput.isEmpty()) {
                    net.minecraftforge.client.ClientCommandHandler.instance.executeCommand(
                            net.minecraft.client.Minecraft.getMinecraft().thePlayer, "/pitenemy " + socialInput);
                    socialInput = "";
                }
            });

            ctx.bindCustomRenderer("SocialTabs", () -> {
                try (ClayElement wrap = el(decl().layout(layout().widthGrow().heightFit().dirLeftToRight().gap(8)))) {
                    ClayComponents.tab("SubTab_Friends", "Friends (" + PitConfig.social().friends.size() + ")", showFriends, () -> {
                        showFriends = true;
                        socialPage = 0;
                    });
                    ClayComponents.tab("SubTab_Enemies", "Enemies (" + PitConfig.social().enemies.size() + ")", !showFriends, () -> {
                        showFriends = false;
                        socialPage = 0;
                    });
                }
            });

            ctx.bindCustomRenderer("SocialList", () -> {
                try (ClayElement wrap = el(decl().layout(layout().widthGrow().heightFit().dirTopToBottom().gap(6)))) {
                    Map<String, String> targetMap = showFriends ? PitConfig.social().friends : PitConfig.social().enemies;

                    if (targetMap.isEmpty()) {
                        text("List is empty.", txt().size(1).color(ThemeManager.get("text_dim")));
                    } else {
                        List<Map.Entry<String, String>> safeEntries = new ArrayList<>(targetMap.entrySet());

                        int itemsPerPage = 8;
                        int totalItems = safeEntries.size();
                        int totalPages = (int) Math.ceil((double) totalItems / itemsPerPage);
                        if (socialPage >= totalPages && totalPages > 0) socialPage = totalPages - 1;

                        int startIdx = socialPage * itemsPerPage;
                        int endIdx = Math.min(startIdx + itemsPerPage, totalItems);

                        for (int i = startIdx; i < endIdx; i++) {
                            Map.Entry<String, String> entry = safeEntries.get(i);
                            try (ClayElement row = el(decl()
                                    .bg(ThemeManager.get("element"))
                                    .radius(4)
                                    .layout(layout().widthGrow().heightFixed(30)
                                            .padding(10, 10, 0, 0)
                                            .dirLeftToRight()
                                            .alignLeft().alignCenterY()))) {

                                text((showFriends ? "§a" : "§c") + entry.getValue(), txt().size(1));

                                try (ClayElement pad = el(decl().layout(layout().widthGrow().heightFixed(0)))) {
                                }

                                ClayComponents.button("Btn_Del_" + entry.getKey(), "Remove", () -> {
                                    targetMap.remove(entry.getKey());
                                    ConfigManager.save();
                                });
                            }
                        }

                        PitUIUtil.drawPageNav(socialPage, totalPages, "SocialPage", () -> socialPage--, () -> socialPage++);
                    }
                }
            });
        }

        @Override
        public String getName() {
            return "Social Manager";
        }

        @Override
        public void draw() {
            if (xmlNode != null) xmlNode.render(ctx);
        }
    }

    private static class DevToolsTab implements IMenuTab {
        private final XmlNode xmlNode;
        private final XmlContext ctx;

        public DevToolsTab() {
            xmlNode = ClayXmlEngine.load("ui/dev_tools_tab.xml");
            ctx = new XmlContext();
            bindThemeContext(ctx);

            ctx.bindAction("testVis", () -> NotificationManager.add(null, "Mock notification test.", 5000));
            ctx.bindAction("testChat", () -> ChatUtil.simulateChat("§6§l[PitHelper] §eTEST EVENT §7starts in §a0m 0s§7!"));
            ctx.bindAction("testMath", () -> ChatUtil.simulateChat("§d§lQUICK MATHS! §eSolve: (2+8)x5"));
            ctx.bindAction("testDeath", () -> ChatUtil.simulateChat("§c§lDEATH! §7by §c[§4§l81§c] §6TEST_PLAYER §e§lVIEW RECAP"));
        }

        @Override
        public String getName() {
            return "Dev Tools";
        }

        @Override
        public void draw() {
            if (xmlNode != null) xmlNode.render(ctx);
        }
    }
}