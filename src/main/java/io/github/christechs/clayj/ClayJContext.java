/*
 * This is free and unencumbered software released into the public domain.
 *
 * See the LICENSE.md file for more information, or visit <https://unlicense.org/>
 */

package io.github.christechs.clayj;

import io.github.christechs.clayj.config.*;
import io.github.christechs.clayj.core.*;
import io.github.christechs.clayj.core.ClayJFunctions.ErrorHandler;
import io.github.christechs.clayj.core.ClayJFunctions.MeasureTextFunction;
import io.github.christechs.clayj.core.ClayJFunctions.QueryScrollOffsetFunction;
import io.github.christechs.clayj.enums.ClayJError;
import io.github.christechs.clayj.math.Dimensions;
import io.github.christechs.clayj.math.Vector2;

import java.util.Arrays;

public final class ClayJContext {

    public static final int MAX_SCROLL_CONTAINERS = 32;
    private static final int DEFAULT_INITIAL_CAPACITY = 8192;
    private static final int DEFAULT_MEASURE_CAPACITY = 8192;

    public final boolean fixedCapacity;
    public final MousePointerData pointerInfo = new MousePointerData();
    public final ScrollContainerDataInternal[] scrollContainerDatas;
    final ElementDeclBuilder rootDeclBuilder = new ElementDeclBuilder();
    public int maxElementCount;
    public int maxMeasureTextCacheWordCount;
    public Vector2 scratchVector = new Vector2(0f, 0f);
    public Dimensions scratchDimensions = new Dimensions(0f, 0f);
    public Dimensions layoutDimensions = new Dimensions(0f, 0f);
    public LayoutElement[] layoutElements;
    public RenderCommand[] renderCommands;
    public int[] openLayoutElementStack;
    public int[] layoutElementChildrenBuffer;
    public int[] layoutElementChildren;
    public int[] openClipElementStack;
    public int[] layoutElementClipElementIds;
    public LayoutConfigBuilder[] layoutConfigs;
    public SharedConfigBuilder[] sharedElementConfigs;
    public BorderConfigBuilder[] borderElementConfigs;
    public FloatingConfigBuilder[] floatingElementConfigs;
    public ScrollConfigBuilder[] scrollElementConfigs;
    public ImageConfigBuilder[] imageElementConfigs;
    public TextConfigBuilder[] textElementConfigs;
    public CustomConfigBuilder[] customElementConfigs;
    public LayoutElementHashMapItem[] layoutElementHashMapItemPool;
    public int[] layoutElementsHashBuckets;
    public LayoutElementTreeRoot[] layoutElementTreeRoots;
    public MeasureTextCacheItem[] measureTextHashMapInternal;
    public int[] measureTextHashMapInternalFreeList;
    public int[] measureTextHashMap;
    public MeasuredWord[] measuredWords;
    public int[] measuredWordsFreeList;
    public TextElementData[] textElementData;
    public WrappedTextLine[] wrappedTextLines;
    public int[] imageElementPointers;
    public ElementId[] pointerOverIds;
    public LayoutElementTreeNode[] layoutElementTreeNodes;
    public boolean[] treeNodeVisited;
    public int[] resizableBuffer;
    public ElementDeclBuilder[] transientDecls;
    public LayoutConfigBuilder[] transientLayouts;
    public TextConfigBuilder[] transientTexts;
    public CustomConfigBuilder[] transientCustoms;
    public ElementId[] transientIds;
    public BorderConfigBuilder[] transientBorders;
    public ScrollConfigBuilder[] transientScrolls;
    public FloatingConfigBuilder[] transientFloatings;
    public ImageConfigBuilder[] transientImages;
    public SharedConfigBuilder[] transientShareds;
    public int transientDeclsLength = 0;
    public int transientLayoutsLength = 0;
    public int transientTextsLength = 0;
    public int transientCustomsLength = 0;
    public int transientIdsLength = 0;
    public int transientBordersLength = 0;
    public int transientScrollsLength = 0;
    public int transientFloatingsLength = 0;
    public int transientImagesLength = 0;
    public int transientSharedsLength = 0;

    public MeasureTextFunction measureTextFunction;
    public ErrorHandler errorHandler = (errorType) -> System.err.println("ClayJ Error [" + errorType + "]: " + errorType.getDefaultMessage());
    public QueryScrollOffsetFunction queryScrollOffsetFunction;
    public boolean externalScrollHandlingEnabled = false;

    public boolean maxElementsExceeded = false;
    public boolean maxRenderCommandsExceeded = false;
    public boolean maxTextMeasureCacheExceeded = false;
    public boolean textMeasurementFunctionNotSet = false;
    public int generation = 0;
    public int dynamicElementIndex = 0;
    public int layoutElementsLength = 0;
    public int renderCommandsLength = 0;
    public int openLayoutElementStackLength = 0;
    public int layoutElementChildrenBufferLength = 0;
    public int layoutElementChildrenLength = 0;
    public int openClipElementStackLength = 0;
    public int layoutConfigsLength = 0;
    public int sharedElementConfigsLength = 0;
    public int borderElementConfigsLength = 0;
    public int floatingElementConfigsLength = 0;
    public int scrollElementConfigsLength = 0;
    public int imageElementConfigsLength = 0;
    public int textElementConfigsLength = 0;
    public int customElementConfigsLength = 0;
    public int layoutElementHashMapItemPoolLength = 0;
    public int layoutElementTreeRootsLength = 0;
    public int measureTextHashMapInternalLength = 0;
    public int measureTextHashMapInternalFreeListLength = 0;
    public int measuredWordsLength = 0;
    public int measuredWordsFreeListLength = 0;
    public int textElementDataLength = 0;
    public int wrappedTextLinesLength = 0;
    public int imageElementPointersLength = 0;
    public int pointerOverIdsLength = 0;
    public int scrollContainerDatasLength = 0;
    public int resizableBufferLength = 0;

    public ClayJContext(int maxElementCount, int maxMeasureTextCacheWordCount) {
        this(maxElementCount, maxMeasureTextCacheWordCount, true);
    }

    private ClayJContext(int maxElementCount, int maxMeasureTextCacheWordCount, boolean fixedCapacity) {
        this.maxElementCount = maxElementCount;
        this.maxMeasureTextCacheWordCount = maxMeasureTextCacheWordCount;
        this.fixedCapacity = fixedCapacity;

        scrollContainerDatas = new ScrollContainerDataInternal[MAX_SCROLL_CONTAINERS];

        layoutElements = new LayoutElement[maxElementCount];
        renderCommands = new RenderCommand[maxElementCount];

        openLayoutElementStack = new int[maxElementCount];
        layoutElementChildrenBuffer = new int[maxElementCount];
        layoutElementChildren = new int[maxElementCount];
        openClipElementStack = new int[maxElementCount];
        layoutElementClipElementIds = new int[maxElementCount];
        resizableBuffer = new int[maxElementCount];
        treeNodeVisited = new boolean[maxElementCount];

        layoutConfigs = new LayoutConfigBuilder[maxElementCount];
        sharedElementConfigs = new SharedConfigBuilder[maxElementCount];
        borderElementConfigs = new BorderConfigBuilder[maxElementCount];
        floatingElementConfigs = new FloatingConfigBuilder[maxElementCount];
        scrollElementConfigs = new ScrollConfigBuilder[maxElementCount];
        imageElementConfigs = new ImageConfigBuilder[maxElementCount];
        textElementConfigs = new TextConfigBuilder[maxElementCount];
        customElementConfigs = new CustomConfigBuilder[maxElementCount];

        layoutElementHashMapItemPool = new LayoutElementHashMapItem[maxElementCount];
        layoutElementsHashBuckets = new int[maxElementCount];
        Arrays.fill(layoutElementsHashBuckets, -1);

        layoutElementTreeRoots = new LayoutElementTreeRoot[maxElementCount];
        textElementData = new TextElementData[maxElementCount];
        wrappedTextLines = new WrappedTextLine[maxElementCount];
        layoutElementTreeNodes = new LayoutElementTreeNode[maxElementCount];

        imageElementPointers = new int[maxElementCount];
        pointerOverIds = new ElementId[maxElementCount];

        measureTextHashMapInternal = new MeasureTextCacheItem[maxMeasureTextCacheWordCount];
        measureTextHashMapInternal[0] = new MeasureTextCacheItem();
        measureTextHashMapInternalFreeList = new int[maxMeasureTextCacheWordCount];
        measureTextHashMap = new int[maxMeasureTextCacheWordCount];
        measuredWords = new MeasuredWord[maxMeasureTextCacheWordCount];
        measuredWordsFreeList = new int[maxMeasureTextCacheWordCount];
        measureTextHashMapInternalLength = 1;

        int transientSize = Math.max(16, maxElementCount / 2);
        transientDecls = new ElementDeclBuilder[transientSize];
        transientLayouts = new LayoutConfigBuilder[transientSize];
        transientTexts = new TextConfigBuilder[transientSize];
        transientCustoms = new CustomConfigBuilder[transientSize];
        transientIds = new ElementId[transientSize];
        transientBorders = new BorderConfigBuilder[transientSize];
        transientScrolls = new ScrollConfigBuilder[transientSize];
        transientFloatings = new FloatingConfigBuilder[transientSize];
        transientImages = new ImageConfigBuilder[transientSize];
        transientShareds = new SharedConfigBuilder[transientSize];
    }

    public static ClayJContext createDynamic() {
        return new ClayJContext(DEFAULT_INITIAL_CAPACITY, DEFAULT_MEASURE_CAPACITY, false);
    }

    public static ClayJContext createFixed(int exactCapacity) {
        return new ClayJContext(exactCapacity, exactCapacity, true);
    }

    public void ensureCapacity(int needed) {
        if (needed <= maxElementCount) return;
        if (fixedCapacity) {
            maxElementsExceeded = true;
            if (errorHandler != null) errorHandler.handleError(ClayJError.ELEMENTS_CAPACITY_EXCEEDED);
            return;
        }

        int newCap = Math.max(needed, maxElementCount + (maxElementCount >> 1));

        layoutElements = Arrays.copyOf(layoutElements, newCap);
        renderCommands = Arrays.copyOf(renderCommands, newCap);
        openLayoutElementStack = Arrays.copyOf(openLayoutElementStack, newCap);
        layoutElementChildrenBuffer = Arrays.copyOf(layoutElementChildrenBuffer, newCap);
        layoutElementChildren = Arrays.copyOf(layoutElementChildren, newCap);
        openClipElementStack = Arrays.copyOf(openClipElementStack, newCap);
        layoutElementClipElementIds = Arrays.copyOf(layoutElementClipElementIds, newCap);
        layoutConfigs = Arrays.copyOf(layoutConfigs, newCap);
        sharedElementConfigs = Arrays.copyOf(sharedElementConfigs, newCap);
        borderElementConfigs = Arrays.copyOf(borderElementConfigs, newCap);
        floatingElementConfigs = Arrays.copyOf(floatingElementConfigs, newCap);
        scrollElementConfigs = Arrays.copyOf(scrollElementConfigs, newCap);
        imageElementConfigs = Arrays.copyOf(imageElementConfigs, newCap);
        textElementConfigs = Arrays.copyOf(textElementConfigs, newCap);
        customElementConfigs = Arrays.copyOf(customElementConfigs, newCap);
        layoutElementHashMapItemPool = Arrays.copyOf(layoutElementHashMapItemPool, newCap);
        layoutElementTreeRoots = Arrays.copyOf(layoutElementTreeRoots, newCap);
        textElementData = Arrays.copyOf(textElementData, newCap);
        wrappedTextLines = Arrays.copyOf(wrappedTextLines, newCap);
        layoutElementTreeNodes = Arrays.copyOf(layoutElementTreeNodes, newCap);
        imageElementPointers = Arrays.copyOf(imageElementPointers, newCap);
        pointerOverIds = Arrays.copyOf(pointerOverIds, newCap);
        resizableBuffer = Arrays.copyOf(resizableBuffer, newCap);
        treeNodeVisited = Arrays.copyOf(treeNodeVisited, newCap);

        int[] newBuckets = new int[newCap];
        Arrays.fill(newBuckets, -1);
        for (int i = 0; i < layoutElementHashMapItemPoolLength; i++) {
            LayoutElementHashMapItem item = layoutElementHashMapItemPool[i];
            if (item != null && item.elementId.id != 0) {
                int bucket = (item.elementId.id & 0x7FFFFFFF) % newCap;
                item.nextIndex = newBuckets[bucket];
                newBuckets[bucket] = i;
            }
        }
        layoutElementsHashBuckets = newBuckets;
        maxElementCount = newCap;
    }

    public void resetEphemeral() {
        maxElementsExceeded = false;
        maxRenderCommandsExceeded = false;
        maxTextMeasureCacheExceeded = false;
        textMeasurementFunctionNotSet = false;

        layoutElementsLength = 0;
        renderCommandsLength = 0;
        openLayoutElementStackLength = 0;
        layoutElementChildrenBufferLength = 0;
        layoutElementChildrenLength = 0;
        openClipElementStackLength = 0;
        layoutElementTreeRootsLength = 0;
        layoutConfigsLength = 0;
        sharedElementConfigsLength = 0;
        borderElementConfigsLength = 0;
        floatingElementConfigsLength = 0;
        scrollElementConfigsLength = 0;
        imageElementConfigsLength = 0;
        textElementConfigsLength = 0;
        customElementConfigsLength = 0;
        textElementDataLength = 0;
        wrappedTextLinesLength = 0;
        imageElementPointersLength = 0;
        dynamicElementIndex = 0;

        transientDeclsLength = 0;
        transientLayoutsLength = 0;
        transientTextsLength = 0;
        transientCustomsLength = 0;
        transientIdsLength = 0;
        transientBordersLength = 0;
        transientScrollsLength = 0;
        transientFloatingsLength = 0;
        transientImagesLength = 0;
        transientSharedsLength = 0;
    }

    public ElementDeclBuilder takeDecl() {
        if (transientDeclsLength >= transientDecls.length) {
            transientDecls = Arrays.copyOf(transientDecls, transientDecls.length * 2);
        }
        int idx = transientDeclsLength++;
        if (transientDecls[idx] == null) transientDecls[idx] = new ElementDeclBuilder();
        ElementDeclBuilder b = transientDecls[idx];
        b.reset();
        return b;
    }

    public LayoutConfigBuilder takeLayout() {
        if (transientLayoutsLength >= transientLayouts.length) {
            transientLayouts = Arrays.copyOf(transientLayouts, transientLayouts.length * 2);
        }
        int idx = transientLayoutsLength++;
        if (transientLayouts[idx] == null) transientLayouts[idx] = new LayoutConfigBuilder();
        LayoutConfigBuilder b = transientLayouts[idx];
        b.reset();
        return b;
    }

    public TextConfigBuilder takeText() {
        if (transientTextsLength >= transientTexts.length) {
            transientTexts = Arrays.copyOf(transientTexts, transientTexts.length * 2);
        }
        int idx = transientTextsLength++;
        if (transientTexts[idx] == null) transientTexts[idx] = new TextConfigBuilder();
        TextConfigBuilder b = transientTexts[idx];
        b.reset();
        return b;
    }

    public CustomConfigBuilder takeCustom() {
        if (transientCustomsLength >= transientCustoms.length) {
            transientCustoms = Arrays.copyOf(transientCustoms, transientCustoms.length * 2);
        }
        int idx = transientCustomsLength++;
        if (transientCustoms[idx] == null) transientCustoms[idx] = new CustomConfigBuilder();
        CustomConfigBuilder b = transientCustoms[idx];
        b.reset();
        return b;
    }

    public ElementId takeId() {
        if (transientIdsLength >= transientIds.length) {
            transientIds = Arrays.copyOf(transientIds, transientIds.length * 2);
        }
        int idx = transientIdsLength++;
        if (transientIds[idx] == null) transientIds[idx] = new ElementId();
        ElementId id = transientIds[idx];
        id.reset();
        return id;
    }

    public LayoutElementHashMapItem getHashMapItem(int id) {
        if (id == 0) return null;
        int bucket = (id & 0x7FFFFFFF) % layoutElementsHashBuckets.length;
        int index = layoutElementsHashBuckets[bucket];

        while (index != -1) {
            LayoutElementHashMapItem item = layoutElementHashMapItemPool[index];
            if (item.elementId.id == id) return item;
            index = item.nextIndex;
        }
        return null;
    }

    public LayoutElementHashMapItem addHashMapItem(ElementId elementId, LayoutElement layoutElement, int elementIndex, int idAlias) {
        int bucket = (elementId.id & 0x7FFFFFFF) % layoutElementsHashBuckets.length;
        int prevIndex = -1;
        int currentIndex = layoutElementsHashBuckets[bucket];

        while (currentIndex != -1) {
            LayoutElementHashMapItem item = layoutElementHashMapItemPool[currentIndex];
            if (item.elementId.id == elementId.id) {
                if (item.generation < this.generation) {
                    item.elementId.set(elementId);
                    item.generation = this.generation;
                    item.layoutElement = layoutElement;
                    item.idAlias = idAlias;
                    item.onHoverFunction = null;
                    item.elementIndex = elementIndex;
                    return item;
                } else {
                    if (errorHandler != null) errorHandler.handleError(ClayJError.DUPLICATE_ID);
                    return item;
                }
            }
            prevIndex = currentIndex;
            currentIndex = item.nextIndex;
        }

        if (layoutElementHashMapItemPoolLength >= layoutElementHashMapItemPool.length) {
            if (fixedCapacity) {
                if (errorHandler != null) errorHandler.handleError(ClayJError.ARENA_CAPACITY_EXCEEDED);
                return null;
            } else {
                ensureCapacity(maxElementCount + (maxElementCount >> 1));
                bucket = (elementId.id & 0x7FFFFFFF) % layoutElementsHashBuckets.length;
                prevIndex = -1;
                currentIndex = layoutElementsHashBuckets[bucket];
                while (currentIndex != -1) {
                    prevIndex = currentIndex;
                    currentIndex = layoutElementHashMapItemPool[currentIndex].nextIndex;
                }
            }
        }

        int newItemIndex = layoutElementHashMapItemPoolLength++;
        if (layoutElementHashMapItemPool[newItemIndex] == null)
            layoutElementHashMapItemPool[newItemIndex] = new LayoutElementHashMapItem();
        LayoutElementHashMapItem newItem = layoutElementHashMapItemPool[newItemIndex];
        newItem.reset();

        newItem.elementId.set(elementId);
        newItem.layoutElement = layoutElement;
        newItem.generation = this.generation;
        newItem.idAlias = idAlias;
        newItem.elementIndex = elementIndex;

        if (prevIndex != -1) layoutElementHashMapItemPool[prevIndex].nextIndex = newItemIndex;
        else layoutElementsHashBuckets[bucket] = newItemIndex;

        return newItem;
    }

    public LayoutElement openLayoutElement() {
        return layoutElements[openLayoutElementStack[openLayoutElementStackLength - 1]];
    }

    public LayoutElement openParentLayoutElement() {
        return layoutElements[openLayoutElementStack[openLayoutElementStackLength - 2]];
    }
}