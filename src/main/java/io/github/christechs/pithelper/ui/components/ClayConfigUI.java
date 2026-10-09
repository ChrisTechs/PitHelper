package io.github.christechs.pithelper.ui.components;

import io.github.christechs.clayj.ClayJ.*;
import io.github.christechs.clayj.config.ImageConfigBuilder;
import io.github.christechs.clayj.config.LayoutConfigBuilder;
import io.github.christechs.clayj.math.Dimensions;
import io.github.christechs.config.*;
import io.github.christechs.pithelper.ui.utils.ThemeManager;

import static io.github.christechs.clayj.ClayJ.*;

public class ClayConfigUI {

    public static boolean elementMatches(ConfigElement element, String query) {
        if (query == null || query.trim().isEmpty()) return true;
        if (element.hidden) return false;

        String q = query.toLowerCase();
        if (element.name.toLowerCase().contains(q)) return true;
        if (element.description != null && element.description.toLowerCase().contains(q)) return true;

        if (element instanceof ConfigGroup) {
            for (ConfigElement child : ((ConfigGroup) element).elements) {
                if (elementMatches(child, q)) return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static void drawConfigElement(ConfigElement element, String query) {
        if (element.hidden || !elementMatches(element, query)) return;

        if (element instanceof ConfigOption) {
            ConfigOption<?> opt = (ConfigOption<?>) element;
            if (opt.get() instanceof Boolean) configToggle((ConfigOption<Boolean>) opt);
            else if (opt.get() instanceof Integer) configSpinner((ConfigOption<Integer>) opt);
            else if (opt.get() instanceof String) configTextInput((ConfigOption<String>) opt);
        } else if (element instanceof ConfigGroup) {
            drawConfigGroup((ConfigGroup) element, query);
        } else if (element instanceof ConfigButton) {
            ConfigButton btn = (ConfigButton) element;
            try (ClayElement wrap = el(decl().layout(layout().widthGrow().heightFit().dirTopToBottom().gap(4)))) {
                text(btn.name, txt().size(1).color(ThemeManager.get("foreground")));
                if (btn.description != null && !btn.description.isEmpty()) {
                    text(btn.description, txt().size(1).color(ThemeManager.get("text_dim")));
                }
                ClayComponents.button("CfgBtn_" + btn.id, btn.buttonLabel, btn.action);
            }
        } else if (element instanceof ConfigDisplay) {
            ConfigDisplay disp = (ConfigDisplay) element;
            try (ClayElement wrap = el(decl()
                    .bg(ThemeManager.get("element"))
                    .radius(6)
                    .layout(layout().widthGrow().heightFit().padding(10, 10)))) {
                text(disp.name + ": §e" + disp.displayValue.get(), txt().size(1).color(ThemeManager.get("foreground")));
            }
        }
    }

    public static void drawConfigGroup(ConfigGroup group, String query) {
        boolean isSearching = query != null && !query.trim().isEmpty();
        if (isSearching) group.expanded = true;

        try (ClayElement wrap = el(decl()
                .id("Grp_" + group.id)
                .bg(ThemeManager.get("element"))
                .radius(6)
                .layout(layout()
                        .widthGrow().heightFit()
                        .dirTopToBottom()
                        .padding(10, 10)
                        .gap(10)))) {
            renderGroupHeader(group);
            if (group.expanded) renderGroupBody(group, query);
        }
    }

    private static void renderGroupHeader(ConfigGroup group) {
        try (ClayElement header = el(decl().layout(layout()
                .widthGrow().heightFit()
                .dirLeftToRight()
                .alignLeft().alignCenterY()
                .gap(10)))) {

            ClayComponents.button(
                    "Btn_Toggle_" + group.id,
                    group.expanded ? "▼" : "▶",
                    () -> group.expanded = !group.expanded
            );

            if (group.icon != null) {
                try (ClayElement icon = el(decl()
                        .image(new ImageConfigBuilder()
                                .data(group.icon)
                                .sourceDim(new Dimensions(16, 16)))
                        .layout(layout().widthFixed(16).heightFixed(16)))) {
                }
            }

            if (group.masterToggle != null) {
                configToggle(group.masterToggle);
            } else {
                text(group.name, txt().size(1).color(ThemeManager.get("foreground")));
            }
        }
    }

    private static void renderGroupBody(ConfigGroup group, String query) {
        if (group.description != null && !group.description.isEmpty()) {
            text(group.description, txt().size(1).color(ThemeManager.get("text_dim")));
        }

        try (ClayElement body = el(decl().layout(layout().widthGrow().heightFit().dirTopToBottom().gap(8).padding(15, 0)))) {
            boolean groupExplicitMatch = group.name.toLowerCase().contains(query == null ? "" : query.toLowerCase());
            for (ConfigElement child : group.elements) {
                drawConfigElement(child, groupExplicitMatch ? "" : query);
            }
        }
    }

    public static void configTextInput(ConfigOption<String> option) {
        try (ClayElement wrap = el(decl().layout(layout().widthGrow().heightFit().dirTopToBottom().gap(4)))) {
            text(option.name, txt().size(1).color(ThemeManager.get("foreground")));
            ClayComponents.textInput("Txt_" + option.id, option.name, option.get(), 200, option::set);
        }
    }

    public static void configToggle(ConfigOption<Boolean> option) {
        String id = "Tgl_" + option.id;
        boolean value = option.get();

        try (ClayElement wrap = el(decl().layout(layout()
                .widthGrow().heightFit()
                .dirLeftToRight()
                .gap(10)
                .alignLeft().alignCenterY()))) {

            if (ClayComponents.isClicked(id)) option.set(!value);

            LayoutConfigBuilder toggleLayout = layout().widthFixed(32).heightFixed(16).padding(2, 0).alignCenterY();
            if (value) toggleLayout.alignRight();
            else toggleLayout.alignLeft();

            try (ClayElement tgl = el(decl()
                    .id(id)
                    .bg(value ? ThemeManager.get("success") : ThemeManager.get("background"))
                    .radius(8)
                    .layout(toggleLayout))) {

                try (ClayElement thumb = el(decl()
                        .bg(ThemeManager.get("foreground"))
                        .radius(6)
                        .layout(layout().widthFixed(12).heightFixed(12)))) {
                }
            }

            try (ClayElement txtWrap = el(decl().layout(layout().widthGrow().heightFit().dirTopToBottom()))) {
                text(option.name, txt().size(1).color(ThemeManager.get("foreground")));
                if (option.description != null && !option.description.isEmpty()) {
                    text(option.description, txt().size(1).color(ThemeManager.get("text_dim")));
                }
            }
        }
    }

    public static void configSpinner(ConfigOption<Integer> option) {
        String id = "Spn_" + option.id;
        int value = option.get();

        try (ClayElement wrap = el(decl().layout(layout()
                .widthGrow().heightFit()
                .dirLeftToRight()
                .gap(8)
                .alignLeft().alignCenterY()))) {

            text(option.name + ":", txt().size(1).color(ThemeManager.get("foreground")));

            ClayComponents.button(id + "_down", "-", () -> {
                if (value > option.min) option.set(value - 1);
            });

            try (ClayElement val = el(decl().layout(layout().widthFixed(20).heightFixed(20).alignCenter()))) {
                text(String.valueOf(value), txt().size(1).color(ThemeManager.get("foreground")));
            }

            ClayComponents.button(id + "_up", "+", () -> {
                if (value < option.max) option.set(value + 1);
            });
        }
    }
}