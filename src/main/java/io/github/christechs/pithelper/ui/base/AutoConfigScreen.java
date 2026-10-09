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

package io.github.christechs.pithelper.ui.base;

import io.github.christechs.clayj.ClayJ.ClayElement;
import io.github.christechs.config.ConfigButton;
import io.github.christechs.config.ConfigCategoryData;
import io.github.christechs.config.ConfigElement;
import io.github.christechs.config.ConfigManager;
import io.github.christechs.pithelper.config.PitConfig;
import io.github.christechs.pithelper.ui.components.ClayComponents;
import io.github.christechs.pithelper.ui.components.ClayConfigUI;
import io.github.christechs.pithelper.ui.utils.PitUIUtil;
import io.github.christechs.pithelper.ui.utils.ThemeManager;
import io.github.christechs.pithelper.ui.xml.ClayXmlEngine;
import io.github.christechs.pithelper.ui.xml.XmlContext;
import io.github.christechs.pithelper.ui.xml.nodes.XmlNode;

import java.util.ArrayList;
import java.util.List;

import static io.github.christechs.clayj.ClayJ.*;

public abstract class AutoConfigScreen extends ClayScreen {

    protected final List<IMenuTab> tabs = new ArrayList<>();
    protected IMenuTab activeTab;
    protected String title;
    protected String globalSearchQuery = "";
    protected String categorySearchQuery = "";

    protected XmlNode xmlRoot;
    protected XmlNode searchResultsXml;
    protected XmlContext xmlContext;

    public AutoConfigScreen(String title) {
        this.title = title;
        if (ThemeManager.THEMES.size() <= 1) {
            ThemeManager.loadThemesFromZip();
            PitConfig.general().activeTheme = ThemeManager.THEMES.containsKey(PitConfig.general().activeTheme)
                    ? PitConfig.general().activeTheme : "Challenger Deep";
            ThemeManager.activeTheme = ThemeManager.THEMES.get(PitConfig.general().activeTheme);
        }
    }

    public void addTab(IMenuTab tab) {
        tabs.add(tab);
        if (activeTab == null) activeTab = tab;
    }

    @Override
    public void initGui() {
        super.initGui();

        xmlRoot = ClayXmlEngine.load("ui/auto_config.xml");
        searchResultsXml = ClayXmlEngine.load("ui/search_results.xml");
        xmlContext = new XmlContext();

        xmlContext.bindString("theme.bg", () -> ThemeManager.getHex("background"));
        xmlContext.bindString("theme.sidebar", () -> ThemeManager.getHex("sidebar"));
        xmlContext.bindString("theme.element", () -> ThemeManager.getHex("element"));
        xmlContext.bindString("theme.text", () -> ThemeManager.getHex("foreground"));
        xmlContext.bindString("theme.text_dim", () -> ThemeManager.getHex("text_dim"));
        xmlContext.bindString("theme.accent", () -> ThemeManager.getHex("accent"));

        xmlContext.bindString("menuTitle", () -> this.title);
        xmlContext.bindInput("globalSearchQuery", () -> globalSearchQuery, val -> globalSearchQuery = val);

        xmlContext.bindCustomRenderer("TabList", () -> {
            try (ClayElement wrap = el(decl().layout(layout().widthGrow().heightFit().dirTopToBottom().gap(8)))) {
                if (!globalSearchQuery.isEmpty()) {
                    ClayComponents.tab("Tab_SearchResults", "Search Results", true, () -> {
                    });
                } else {
                    for (int i = 0; i < tabs.size(); i++) {
                        IMenuTab tab = tabs.get(i);
                        ClayComponents.tab("Tab_" + i, tab.getName(), activeTab == tab, () -> {
                            activeTab = tab;
                            categorySearchQuery = "";
                        });
                    }
                }
            }
        });

        xmlContext.bindCustomRenderer("ActiveContent", this::drawMainContent);

        xmlContext.bindCustomRenderer("ResultList", () -> {
            try (ClayElement wrap = el(decl().layout(layout().widthGrow().heightFit().dirTopToBottom().gap(12)))) {
                boolean foundAny = false;
                for (ConfigCategoryData cat : ConfigManager.CATEGORIES) {
                    for (ConfigElement el : cat.elements) {
                        if (ClayConfigUI.elementMatches(el, globalSearchQuery)) {
                            foundAny = true;
                            ClayConfigUI.drawConfigElement(el, globalSearchQuery);
                        }
                    }
                }
                if (!foundAny) {
                    text("No results found for '" + globalSearchQuery + "'", txt().size(1).color(ThemeManager.get("text_dim")));
                }
                try (ClayElement pad = el(decl().layout(layout().widthGrow().heightFixed(15)))) {
                }
            }
        });
    }

    public void loadConfigCategories() {
        for (ConfigCategoryData category : ConfigManager.CATEGORIES) {
            boolean hasVisibleElements = false;
            for (ConfigElement el : category.elements) {
                if (!el.hidden) {
                    hasVisibleElements = true;
                    break;
                }
            }
            if (hasVisibleElements) addTab(new ConfigCategoryTab(category));
        }
    }

    @Override
    protected void buildLayout(int mouseX, int mouseY, float deltaTime) {
        float winWidth = Math.min(650, this.width - 20);
        float winHeight = Math.min(400, this.height - 20);

        try (ClayElement root = el(decl()
                .id("ScreenRoot")
                .layout(layout()
                        .widthGrow().heightGrow()
                        .alignCenter()))) {

            try (ClayElement inner = el(decl().layout(layout().widthFixed(winWidth).heightFixed(winHeight)))) {
                if (xmlRoot != null) xmlRoot.render(xmlContext);
            }
        }

        onPostBuildLayout();
    }

    protected void onPostBuildLayout() {
    }

    private void drawMainContent() {
        if (!globalSearchQuery.isEmpty()) {
            if (searchResultsXml != null) searchResultsXml.render(xmlContext);
        } else if (activeTab != null) {
            activeTab.draw();
        }
    }

    public class ConfigCategoryTab implements IMenuTab {
        private static final int ITEMS_PER_PAGE = 10;
        public final ConfigCategoryData category;
        private final XmlNode tabXml;
        private final XmlContext ctx;
        private int currentPage = 0;

        public ConfigCategoryTab(ConfigCategoryData category) {
            this.category = category;
            this.tabXml = ClayXmlEngine.load("ui/category_tab.xml");
            this.ctx = new XmlContext();

            ctx.bindString("categoryName", () -> category.name);
            ctx.bindString("wrapperId", () -> "ScrollWrapper_" + category.id);
            ctx.bindString("scrollId", () -> "Scroll_" + category.id);

            ctx.bindInput("categorySearchQuery", () -> categorySearchQuery, val -> {
                categorySearchQuery = val;
                currentPage = 0;
            });

            ctx.bindCustomRenderer("ElementList", () -> {
                try (ClayElement wrap = el(decl().layout(layout().widthGrow().heightFit().dirTopToBottom().gap(12)))) {

                    List<ConfigElement> filtered = new ArrayList<>();
                    for (ConfigElement element : category.elements) {
                        if (ClayConfigUI.elementMatches(element, categorySearchQuery)) {
                            filtered.add(element);
                        }
                    }

                    if (filtered.isEmpty()) {
                        text("No results found in this category.", txt().size(1).color(ThemeManager.get("text_dim")));
                    } else {
                        List<ConfigElement> normalElements = new ArrayList<>();
                        List<ConfigElement> paginatedElements = new ArrayList<>();

                        for (ConfigElement el : filtered) {
                            if (category.name.equals("Themes") && el instanceof ConfigButton) {
                                paginatedElements.add(el);
                            } else {
                                normalElements.add(el);
                            }
                        }

                        for (ConfigElement el : normalElements) {
                            ClayConfigUI.drawConfigElement(el, categorySearchQuery);
                        }

                        if (!paginatedElements.isEmpty()) {
                            int totalPages = (int) Math.ceil((double) paginatedElements.size() / ITEMS_PER_PAGE);
                            if (currentPage >= totalPages && totalPages > 0) currentPage = totalPages - 1;

                            int startIdx = currentPage * ITEMS_PER_PAGE;
                            int endIdx = Math.min(startIdx + ITEMS_PER_PAGE, paginatedElements.size());

                            for (int i = startIdx; i < endIdx; i++) {
                                ClayConfigUI.drawConfigElement(paginatedElements.get(i), categorySearchQuery);
                            }

                            PitUIUtil.drawPageNav(currentPage, totalPages, "CategoryPageNav",
                                    () -> currentPage--,
                                    () -> currentPage++);
                        }
                    }
                    try (ClayElement pad = el(decl().layout(layout().widthGrow().heightFixed(15)))) {
                    }
                }
            });
        }

        @Override
        public String getName() {
            return category.name;
        }

        @Override
        public void draw() {
            if (tabXml != null) tabXml.render(ctx);
        }
    }
}