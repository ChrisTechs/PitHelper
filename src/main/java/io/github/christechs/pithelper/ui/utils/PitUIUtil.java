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

package io.github.christechs.pithelper.ui.utils;

import io.github.christechs.clayj.ClayJ;
import io.github.christechs.clayj.ClayJ.*;
import io.github.christechs.clayj.config.CustomConfigBuilder;
import io.github.christechs.clayj.math.Color;
import io.github.christechs.pithelper.ui.components.ClayComponents;
import net.minecraft.item.ItemStack;

import java.util.function.Consumer;

import static io.github.christechs.clayj.ClayJ.*;

public class PitUIUtil {

    public static void drawPageNav(int currentPage, int totalPages, String idPrefix, Runnable onPrev, Runnable onNext) {
        if (totalPages > 1) {
            try (ClayElement wrap = el(decl().layout(layout()
                    .dirLeftToRight()
                    .alignCenter()
                    .gap(15)
                    .widthGrow().heightFit()
                    .padding(0, 0, 5, 5)))) {

                ClayComponents.button(idPrefix + "_Prev", "< Prev", () -> {
                    if (currentPage > 0) onPrev.run();
                });

                text(
                        "Page " + (currentPage + 1) + " of " + totalPages,
                        txt().size(1).color(new Color(200, 200, 200, 255))
                );

                ClayComponents.button(idPrefix + "_Next", "Next >", () -> {
                    if (currentPage < totalPages - 1) onNext.run();
                });
            }
        }
    }

    public static boolean drawItemSlot(String slotId, ItemStack stack) {
        boolean isHovered = stack != null && ClayJ.pointerOver(slotId);

        try (ClayElement wrap = el(decl().id(slotId)
                .bg(new Color(60, 60, 60, 255))
                .radius(2)
                .layout(layout()
                        .widthFixed(24).heightFixed(24)
                        .alignCenter()))) {
            if (stack != null) {
                try (ClayElement item = el(decl()
                        .custom(new CustomConfigBuilder().customData(stack))
                        .layout(layout().widthFixed(16).heightFixed(16)))) {
                }
            }
        }
        return isHovered;
    }

    public static void drawItemGrid(
            ItemStack[] items,
            int currentPage,
            int itemsPerPage,
            int columns,
            String slotIdPrefix,
            Consumer<ItemStack> onHover,
            Consumer<Integer> onClick) {

        int totalItems = items.length;
        int startIdx = currentPage * itemsPerPage;
        int endIdx = Math.min(startIdx + itemsPerPage, totalItems);
        int itemsToDraw = endIdx - startIdx;
        int rows = (int) Math.ceil(itemsToDraw / (double) columns);

        try (ClayElement wrap = el(decl().layout(layout()
                .widthGrow()
                .dirTopToBottom()
                .gap(4)))) {
            for (int r = 0; r < rows; r++) {
                int finalR = r;
                try (ClayElement rowWrap = el(decl().layout(layout().dirLeftToRight().gap(4)))) {
                    for (int c = 0; c < columns; c++) {
                        int idx = startIdx + (finalR * columns) + c;
                        if (idx < endIdx) {
                            ItemStack stack = items[idx];

                            int relativeIdx = (finalR * columns) + c;
                            String slotId = slotIdPrefix + relativeIdx;

                            if (drawItemSlot(slotId, stack) && onHover != null) {
                                onHover.accept(stack);
                            }
                            if (onClick != null && ClayComponents.isClicked(slotId)) {
                                onClick.accept(idx);
                            }
                        } else {
                            try (ClayElement pad = el(decl().layout(layout().widthFixed(24).heightFixed(24)))) {
                            }
                        }
                    }
                }
            }
        }
    }
}