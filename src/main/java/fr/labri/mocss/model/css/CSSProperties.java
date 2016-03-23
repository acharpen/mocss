/*
 * This file is part of Mocss.
 *
 * Mocss is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Mocss is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Mocss.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright 2015 Alan Charpentier <alan.charpentier@gmail.com>
 */

package fr.labri.mocss.model.css;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public enum CSSProperties {

    Color("color", CSSPropertyGroups.Color),
    Opacity("opacity", CSSPropertyGroups.Color),

    Background("background", CSSPropertyGroups.Background),
    BackgroundAttachment("background-attachment", CSSPropertyGroups.Background),
    BackgroundBlendMode("background-blend-mode", CSSPropertyGroups.Background),
    BackgroundColor("background-color", CSSPropertyGroups.Background),
    BackgroundImage("background-image", CSSPropertyGroups.Background),
    BackgroundRepeat("background-repeat", CSSPropertyGroups.Background),
    BackgroundClip("backgroumd-clip", CSSPropertyGroups.Background),
    BackgroundOrigin("background-origin", CSSPropertyGroups.Background),
    BackgroundSize("background-size", CSSPropertyGroups.Background),

    Border("border", CSSPropertyGroups.Border),
    BorderBottom("border-bottom", CSSPropertyGroups.Border),
    BorderBottomColor("border-bottom-color", CSSPropertyGroups.Border),
    BorderBottomLeftRadius("border-bottom-left-radius", CSSPropertyGroups.Background),
    BorderBottomRightRadius("border-bottom-right-radius", CSSPropertyGroups.Border),
    BorderBottomStyle("border-bottom-style", CSSPropertyGroups.Border),
    BorderBottomWidth("border-bottom-width", CSSPropertyGroups.Border),
    BorderColor("border-color", CSSPropertyGroups.Border),
    BorderImage("border-image", CSSPropertyGroups.Border),
    BorderImageOutset("border-image-outset", CSSPropertyGroups.Border),
    BorderImageRepeat("border-image-repeat", CSSPropertyGroups.Border),
    BorderImageSlice("border-image-slice", CSSPropertyGroups.Border),
    BorderImageSource("border-image-source", CSSPropertyGroups.Border),
    BorderImageWidth("border-image-width", CSSPropertyGroups.Border),
    BorderLeft("border-left", CSSPropertyGroups.Border),
    BorderLeftColor("border-left-color", CSSPropertyGroups.Border),
    BorderLeftStyle("border-left-style", CSSPropertyGroups.Border),
    BorderLeftWidth("border-left-width", CSSPropertyGroups.Border),
    BorderRadius("border-radius", CSSPropertyGroups.Border),
    BorderRight("border-right", CSSPropertyGroups.Border),
    BorderRightColor("border-right-color", CSSPropertyGroups.Border),
    BorderRightStyle("border-right-style", CSSPropertyGroups.Border),
    BorderRightWidth("border-right-width", CSSPropertyGroups.Border),
    BorderStyle("border-style", CSSPropertyGroups.Border),
    BorderTop("border-top", CSSPropertyGroups.Border),
    BorderTopColor("border-top-color", CSSPropertyGroups.Border),
    BorderTopLeftRadius("border-top-left-radius", CSSPropertyGroups.Border),
    BorderTopRightRadius("border-top-right-radius", CSSPropertyGroups.Border),
    BorderTopStyle("border-top-style", CSSPropertyGroups.Border),
    BorderTopWidth("border-top-width", CSSPropertyGroups.Border),
    BorderWidth("border-width", CSSPropertyGroups.Border),
    BoxDecorationBreak("box-decoration-break", CSSPropertyGroups.Border),
    BoxShadow("BoxShadow", CSSPropertyGroups.Border),

    Bottom("bottom", CSSPropertyGroups.BasicBox),
    Clear("clear", CSSPropertyGroups.BasicBox),
    Clip("clip", CSSPropertyGroups.BasicBox),
    Display("display", CSSPropertyGroups.BasicBox),
    Float("float", CSSPropertyGroups.BasicBox),
    Height("height", CSSPropertyGroups.BasicBox),
    Left("left", CSSPropertyGroups.BasicBox),
    Margin("margin", CSSPropertyGroups.BasicBox),
    MarginBottom("margin-bottom", CSSPropertyGroups.BasicBox),
    MarginLeft("margin-left", CSSPropertyGroups.BasicBox),
    MarginRight("margin-right", CSSPropertyGroups.BasicBox),
    MarginTop("margin-top", CSSPropertyGroups.BasicBox),
    MaxHeight("max-height", CSSPropertyGroups.BasicBox),
    MaxWidth("max-width", CSSPropertyGroups.BasicBox),
    MinHeight("min-height", CSSPropertyGroups.BasicBox),
    MinWidth("min-width", CSSPropertyGroups.BasicBox),
    Overflow("overflow", CSSPropertyGroups.BasicBox),
    OverflowX("overflow-x", CSSPropertyGroups.BasicBox),
    OverflowY("overflow-x", CSSPropertyGroups.BasicBox),
    Padding("padding", CSSPropertyGroups.BasicBox),
    PaddingBottom("padding-bottom", CSSPropertyGroups.BasicBox),
    PaddingLeft("padding-left", CSSPropertyGroups.BasicBox),
    PaddingRight("padding-right", CSSPropertyGroups.BasicBox),
    PaddingTop("padding-top", CSSPropertyGroups.BasicBox),
    Position("position", CSSPropertyGroups.BasicBox),
    Right("right", CSSPropertyGroups.BasicBox),
    Top("top", CSSPropertyGroups.BasicBox),
    Visibility("visibility", CSSPropertyGroups.BasicBox),
    Width("width", CSSPropertyGroups.BasicBox),
    VerticalAlign("vertical-align", CSSPropertyGroups.BasicBox),
    ZIndex("z-index", CSSPropertyGroups.BasicBox),

    AlignContent("align-content", CSSPropertyGroups.FlexibleBox),
    AlignItems("align-items", CSSPropertyGroups.FlexibleBox),
    AlignSelf("align-self", CSSPropertyGroups.FlexibleBox),
    Flex("flex", CSSPropertyGroups.FlexibleBox),
    FlexBasis("flex-basis", CSSPropertyGroups.FlexibleBox),
    FlexDirection("flex-direction", CSSPropertyGroups.FlexibleBox),
    FlexFlow("flex-flow", CSSPropertyGroups.FlexibleBox),
    FlexGow("flex-grow", CSSPropertyGroups.FlexibleBox),
    FlexShrink("flex-shrink", CSSPropertyGroups.FlexibleBox),
    FlexWrap("flex-wrap", CSSPropertyGroups.FlexibleBox),
    JustifyContent("justify-content", CSSPropertyGroups.FlexibleBox),
    Order("order", CSSPropertyGroups.FlexibleBox),

    HangingPunctuation("hanging-punctuation", CSSPropertyGroups.Text),
    Hyphens("hyphens", CSSPropertyGroups.Text),
    LetterSpacing("letter-spacing", CSSPropertyGroups.Text),
    LineBreak("line-break", CSSPropertyGroups.Text),
    LineHeight("line-height", CSSPropertyGroups.Text),
    OverflowWrap("overflow-wrap", CSSPropertyGroups.Text),
    TabSize("tab-size", CSSPropertyGroups.Text),
    TextAlign("text-align", CSSPropertyGroups.Text),
    TextAlignLast("text-align-last", CSSPropertyGroups.Text),
    TextCombineUpright("text-combine-upright", CSSPropertyGroups.Text),
    TextIndent("text-indent", CSSPropertyGroups.Text),
    TextJustify("text-justify", CSSPropertyGroups.Text),
    TextTransform("text-transform", CSSPropertyGroups.Text),
    WhiteSpace("white-space", CSSPropertyGroups.Text),
    WordBreak("word-break", CSSPropertyGroups.Text),
    WordSpacing("word-spacing", CSSPropertyGroups.Text),
    WordWrap("word-wrap", CSSPropertyGroups.Text),
    Direction("direction", CSSPropertyGroups.Text),
    TextOrientation("text-orientation", CSSPropertyGroups.Text),
    UnicodeBidi("unicode-bidi", CSSPropertyGroups.Text),
    WritingMode("writing-mode", CSSPropertyGroups.Text),

    TextDecoration("text-decoration", CSSPropertyGroups.TextDecoration),
    TextDecorationColor("text-decoration-color", CSSPropertyGroups.TextDecoration),
    TextDecorationLine("text-decoration-line", CSSPropertyGroups.TextDecoration),
    TextDecorationStyle("text-decoration-style", CSSPropertyGroups.TextDecoration),
    TextShadow("text-shadow", CSSPropertyGroups.TextDecoration),
    TextUnderlinePosition("text-underline-position", CSSPropertyGroups.TextDecoration),

    Font("font", CSSPropertyGroups.Font),
    FontFamily("font-family", CSSPropertyGroups.Font),
    FontFeatureSettings("font-feature-settings", CSSPropertyGroups.Font),
    FontKerning("font-kerning", CSSPropertyGroups.Font),
    FontLanguageOverride("font-language-override", CSSPropertyGroups.Font),
    FontSize("font-size", CSSPropertyGroups.Font),
    FontSizeAdjust("font-size-adjust", CSSPropertyGroups.Font),
    FontStretch("font-stretch", CSSPropertyGroups.Font),
    FontStyle("font-style", CSSPropertyGroups.Font),
    FontSynthesis("font-synthesis", CSSPropertyGroups.Font),
    FontVariant("font-variant", CSSPropertyGroups.Font),
    FontVariantAlternates("font-variant-alternates", CSSPropertyGroups.Font),
    FontVariantCaps("font-variant-caps", CSSPropertyGroups.Font),
    FontVariantEastAsian("font-variant-east-asian", CSSPropertyGroups.Font),
    FontVariantLigatures("font-variant-ligatures", CSSPropertyGroups.Font),
    FontVariantNumeric("font-variant-numeric", CSSPropertyGroups.Font),
    FontVariantPosition("font-variant-position", CSSPropertyGroups.Font),
    FontWeight("font-weight", CSSPropertyGroups.Font),

    BorderCollapse("border-collapse", CSSPropertyGroups.Table),
    BorderSpacing("border-spacing", CSSPropertyGroups.Table),
    CaptionSide("caption-side", CSSPropertyGroups.Table),
    EmptyCells("empty-cells", CSSPropertyGroups.Table),
    TableLayout("table-layout", CSSPropertyGroups.Table),

    CounterIncrement("counter-increment", CSSPropertyGroups.ListAndCounter),
    CounterReset("counter-reset", CSSPropertyGroups.ListAndCounter),
    ListStyle("list-style", CSSPropertyGroups.ListAndCounter),
    ListStyleImage("list-style-image", CSSPropertyGroups.ListAndCounter),
    ListStylePosition("list-style-position", CSSPropertyGroups.ListAndCounter),
    ListStyleType("list-style-type", CSSPropertyGroups.ListAndCounter),

    Animation("animation", CSSPropertyGroups.Animation),
    AnimationDelay("animation-delay", CSSPropertyGroups.Animation),
    AnimationDirection("animation-direction", CSSPropertyGroups.Animation),
    AnimationDuration("animation-duration", CSSPropertyGroups.Animation),
    AnimationFillMode("animation-fill-mode", CSSPropertyGroups.Animation),
    AnimationIterationCount("animation-iteration-count", CSSPropertyGroups.Animation),
    AnimationName("animation-name", CSSPropertyGroups.Animation),
    AnimationPlayState("animation-play-state", CSSPropertyGroups.Animation),
    AnimationTimingFunction("animation-timing-function", CSSPropertyGroups.Animation),

    BackfaceVisibility("backface-visibility", CSSPropertyGroups.Transform),
    Perspective("perspective", CSSPropertyGroups.Transform),
    PerspectiveOrigin("perspective-origin", CSSPropertyGroups.Transform),
    Transform("transform", CSSPropertyGroups.Transform),
    TransformOrigin("transform-origin", CSSPropertyGroups.Transform),
    TransformStyle("transform-style", CSSPropertyGroups.Transform),

    Transition("transition", CSSPropertyGroups.Transition),
    TransitionProperty("transition-property", CSSPropertyGroups.Transition),
    TransitionDuration("transition-duration", CSSPropertyGroups.Transition),
    TransitionTimingFunction("transition-timing-function", CSSPropertyGroups.Transition),
    TransitionDdelay("transition-delay", CSSPropertyGroups.Transition),

    BoxSizing("box-sizing", CSSPropertyGroups.BasicUserInterface),
    Content("content", CSSPropertyGroups.BasicUserInterface),
    Cursor("cursor", CSSPropertyGroups.BasicUserInterface),
    ImeMode("ime-mode", CSSPropertyGroups.BasicUserInterface),
    NavDown("nav-down", CSSPropertyGroups.BasicUserInterface),
    NavIndex("nav-index", CSSPropertyGroups.BasicUserInterface),
    NavLeft("nav-left", CSSPropertyGroups.BasicUserInterface),
    NavRight("nav-right", CSSPropertyGroups.BasicUserInterface),
    NavUp("nav-up", CSSPropertyGroups.BasicUserInterface),
    Outline("outline", CSSPropertyGroups.BasicUserInterface),
    OutlineColor("outline-color", CSSPropertyGroups.BasicUserInterface),
    OutlineOffset("outline-offset", CSSPropertyGroups.BasicUserInterface),
    OutlineStyle("outline-style", CSSPropertyGroups.BasicUserInterface),
    OutlineWidth("outline-width", CSSPropertyGroups.BasicUserInterface),
    Resize("resize", CSSPropertyGroups.BasicUserInterface),
    TextOverflow("text-overflow", CSSPropertyGroups.BasicUserInterface),

    BreakAfter("break-after", CSSPropertyGroups.MultiColumnLayout),
    BreakBefore("break-before", CSSPropertyGroups.MultiColumnLayout),
    BreakInside("break-inside", CSSPropertyGroups.MultiColumnLayout),
    ColumnCount("column-count", CSSPropertyGroups.MultiColumnLayout),
    ColumnFill("column-fill", CSSPropertyGroups.MultiColumnLayout),
    ColumnGap("column-gap", CSSPropertyGroups.MultiColumnLayout),
    ColumnRule("column-rule", CSSPropertyGroups.MultiColumnLayout),
    ColumnRuleColor("column-rule-color", CSSPropertyGroups.MultiColumnLayout),
    ColumnRuleStyle("column-rule-style", CSSPropertyGroups.MultiColumnLayout),
    ColumnRuleWidth("column-rule-width", CSSPropertyGroups.MultiColumnLayout),
    ColumnSpan("column-span", CSSPropertyGroups.MultiColumnLayout),
    ColumnWidth("column-width", CSSPropertyGroups.MultiColumnLayout),
    Columns("columns", CSSPropertyGroups.MultiColumnLayout),
    Widows("widows", CSSPropertyGroups.MultiColumnLayout),

    Orphans("orphans", CSSPropertyGroups.PagedMedia),
    PageBreakAfter("page-break-after", CSSPropertyGroups.PagedMedia),
    PageBreakBefore("page-break-before", CSSPropertyGroups.PagedMedia),
    PageBreakInside("page-break-inside", CSSPropertyGroups.PagedMedia),

    Marks("marks", CSSPropertyGroups.GeneratedContent),
    Quotes("quotes", CSSPropertyGroups.GeneratedContent),

    Filter("filter", CSSPropertyGroups.FilterEffect),

    ImageOrientation("image-orientation", CSSPropertyGroups.ImageValueAndReplacedContent),
    ImageRendering("image-rendering", CSSPropertyGroups.ImageValueAndReplacedContent),
    ImageResolution("image-resolution", CSSPropertyGroups.ImageValueAndReplacedContent),
    ObjectFit("object-fit", CSSPropertyGroups.ImageValueAndReplacedContent),
    ObjectPosition("object-position", CSSPropertyGroups.ImageValueAndReplacedContent),

    Mask("mask", CSSPropertyGroups.Masking),
    MaskType("mask-type", CSSPropertyGroups.Masking),

    Mark("mark", CSSPropertyGroups.Speech),
    MarkAfter("mark-after", CSSPropertyGroups.Speech),
    MarkBefore("mark-before", CSSPropertyGroups.Speech),
    Phonemes("phonemes", CSSPropertyGroups.Speech),
    Rest("rest", CSSPropertyGroups.Speech),
    RestAfter("rest-after", CSSPropertyGroups.Speech),
    RestBefore("rest-before", CSSPropertyGroups.Speech),
    VoiceBalance("voice-balance", CSSPropertyGroups.Speech),
    VoiceDuration("voice-duration", CSSPropertyGroups.Speech),
    VoicePitch("voice-pitch", CSSPropertyGroups.Speech),
    VoicePitchRange("voice-pitch-range", CSSPropertyGroups.Speech),
    VoiceRate("voice-rate", CSSPropertyGroups.Speech),
    VoiceStress("voice-stress", CSSPropertyGroups.Speech),
    VoiceVolume("voice-volume", CSSPropertyGroups.Speech),

    MarqueeDirection("marquee-direction", CSSPropertyGroups.Marquee),
    MarqueePlayCount("marquee-play-count", CSSPropertyGroups.Marquee),
    MarqueeSpeed("marquee-speed", CSSPropertyGroups.Marquee),
    MarqueeStyle("marquee-style", CSSPropertyGroups.Marquee);

    private final String name;
    private final CSSPropertyGroups group;

    CSSProperties(String name, CSSPropertyGroups group) {
        this.name = name;
        this.group = group;
    }

    public final String getName() {
        return this.name;
    }

    public final CSSPropertyGroups getGroup() {
        return this.group;
    }

    public static boolean belongToSameGroup(Set<String> propertiesName) {
        if (propertiesName.size() == 1) {
            return true;
        }

        List<CSSProperties> cssProperties = new ArrayList<>();
        for (String propertyName : propertiesName) {
            CSSProperties cssProperty = getCSSProperty(propertyName);
            if (cssProperty != null) {
                cssProperties.add(cssProperty);
            }
        }
        if (cssProperties.isEmpty()) {
            return true;
        }

        CSSPropertyGroups referenceGroup = cssProperties.get(0).getGroup();
        for (int i = 1; i < cssProperties.size(); i++) {
            if (!referenceGroup.equals(cssProperties.get(i).getGroup())) {
                return false;
            }
        }
        return true;
    }

    private static CSSProperties getCSSProperty(String propertyName) {
        for (CSSProperties cssProperty : CSSProperties.values()) {
            if (cssProperty.getName().equals(propertyName)) {
                return cssProperty;
            } else {
                List<String> prefixedCssProperties = new ArrayList<>();
                for (VendorPrefixes vendorPrefix : VendorPrefixes.values()) {
                    prefixedCssProperties.add(vendorPrefix.getPrefix() + cssProperty);
                }
                for (String prefixedCssProperty : prefixedCssProperties) {
                    if (prefixedCssProperty.equals(propertyName)) {
                        return cssProperty;
                    }
                }
            }
        }
        return null;
    }

}
