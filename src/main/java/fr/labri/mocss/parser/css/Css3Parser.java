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

package fr.labri.mocss.parser.css;

import com.google.common.collect.Lists;
import com.helger.commons.charset.CCharset;
import com.helger.css.ECSSVersion;
import com.helger.css.ICSSSourceLocationAware;
import com.helger.css.decl.*;
import com.helger.css.decl.visit.CSSVisitor;
import com.helger.css.decl.visit.ICSSVisitor;
import com.helger.css.reader.CSSReader;
import com.helger.css.writer.CSSWriterSettings;
import fr.labri.mocss.model.*;
import fr.labri.mocss.model.css.CssRuleset;
import fr.labri.mocss.model.css.CssUnknownRule;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;

public class Css3Parser extends CssParser {

    @Override
    public void parse(File cssFile) throws CssParsingException {
        final CascadingStyleSheet data = CSSReader.readFromFile(cssFile, CCharset.CHARSET_UTF_8_OBJ, ECSSVersion.CSS30);
        if (data == null) {
            throw new CssParsingException();
        }
        CSSVisitor.visitCSS(data, new CssVisitor());
    }

    private class CssVisitor implements ICSSVisitor {

        private List<Selector> currentSelectors;
        private List<DeclarationConcrete> currentDeclarations;
        private boolean ignoreCssStatement;
        private String ignoredCssStatement;

        @Override
        public void begin() {
            this.currentSelectors = Lists.newArrayList();
            this.currentDeclarations = Lists.newArrayList();
            this.ignoreCssStatement = false;
            this.ignoredCssStatement = null;
            rulesNb = 0;
            declarationsNb = 0;
            selectorsNb = 0;
        }

        @Override
        public void onBeginStyleRule(@Nonnull CSSStyleRule cssStyleRule) {}

        @Override
        public void onEndStyleRule(@Nonnull CSSStyleRule cssStyleRule) {
            if (!this.ignoreCssStatement) {
                if (!this.currentDeclarations.isEmpty()) {
                    rulesNb++;
                    this.currentSelectors.forEach(currentSelector ->
                                    rulesets.add(new CssRuleset(currentSelector, this.currentDeclarations, getPosition(cssStyleRule)))
                    );
                    this.currentDeclarations.clear();
                }
                this.currentSelectors.clear();
            }
        }

        @Override
        public void onStyleRuleSelector(@Nonnull CSSSelector cssSelector) {
            if (!this.ignoreCssStatement) {
                selectorsNb++;
                String selector = cssSelector.getAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0);
                assert !selector.isEmpty();
                currentSelectors.add(new Selector(selector, getPosition(cssSelector)));
            }
        }

        @Override
        public void onDeclaration(@Nonnull CSSDeclaration cssDeclaration) {
            if (!this.ignoreCssStatement) {
                declarationsNb++;
                String property = cssDeclaration.getProperty();
                String value = cssDeclaration.getExpression().getAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0);
                assert !property.isEmpty() && !value.isEmpty();
                value = value.replaceAll("'", "\"");
                if (cssDeclaration.isImportant()) {
                    value += " !important";
                }
                currentDeclarations.add(new DeclarationConcrete(new Property(property), new ValueConcrete(value), getPosition(cssDeclaration)));
            }
        }

        @Override
        public void onBeginMediaRule(@Nonnull CSSMediaRule cssMediaRule) {
            if (!this.ignoreCssStatement) {
                this.ignoreCssStatement = true;
                this.ignoredCssStatement = "media";
            }
        }

        @Override
        public void onEndMediaRule(@Nonnull CSSMediaRule cssMediaRule) {
            if (!this.ignoreCssStatement || this.ignoredCssStatement.equals("media")) {
                this.ignoreCssStatement = false;
                this.ignoredCssStatement = null;
                String content = cssMediaRule.getAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0);
                unknownRules.add(new CssUnknownRule(content, getPosition(cssMediaRule)));
            }
        }

        @Override
        public void onBeginKeyframesRule(@Nonnull CSSKeyframesRule cssKeyframesRule) {
            if (!this.ignoreCssStatement) {
                this.ignoreCssStatement = true;
                this.ignoredCssStatement = "keyframes";
            }
        }

        @Override
        public void onBeginKeyframesBlock(@Nonnull CSSKeyframesBlock cssKeyframesBlock) {}

        @Override
        public void onEndKeyframesBlock(@Nonnull CSSKeyframesBlock cssKeyframesBlock) {}

        @Override
        public void onEndKeyframesRule(@Nonnull CSSKeyframesRule cssKeyframesRule) {
            if (!this.ignoreCssStatement || this.ignoredCssStatement.equals("keyframes")) {
                this.ignoreCssStatement = false;
                this.ignoredCssStatement = null;
                String content = cssKeyframesRule.getAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0);
                unknownRules.add(new CssUnknownRule(content, getPosition(cssKeyframesRule)));
            }
        }

        @Override
        public void onBeginFontFaceRule(@Nonnull CSSFontFaceRule cssFontFaceRule) {
            if (!this.ignoreCssStatement) {
                this.ignoreCssStatement = true;
                this.ignoredCssStatement = "font-face";
            }
        }

        @Override
        public void onEndFontFaceRule(@Nonnull CSSFontFaceRule cssFontFaceRule) {
            if (!this.ignoreCssStatement || this.ignoredCssStatement.equals("font-face")) {
                this.ignoreCssStatement = false;
                this.ignoredCssStatement = null;
                String content = cssFontFaceRule.getAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0);
                unknownRules.add(new CssUnknownRule(content, getPosition(cssFontFaceRule)));
            }
        }

        @Override
        public void onBeginPageRule(@Nonnull CSSPageRule cssPageRule) {
            if (!this.ignoreCssStatement) {
                this.ignoreCssStatement = true;
                this.ignoredCssStatement = "page";
            }
        }

        @Override
        public void onEndPageRule(@Nonnull CSSPageRule cssPageRule) {
            if (!this.ignoreCssStatement || this.ignoredCssStatement.equals("page")) {
                this.ignoreCssStatement = false;
                this.ignoredCssStatement = null;
                String content = cssPageRule.getAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0);
                unknownRules.add(new CssUnknownRule(content, getPosition(cssPageRule)));
            }
        }

        @Override
        public void onBeginSupportsRule(@Nonnull CSSSupportsRule cssSupportsRule) {
            if (!this.ignoreCssStatement) {
                this.ignoreCssStatement = true;
                this.ignoredCssStatement = "supports";
            }
        }

        @Override
        public void onEndSupportsRule(@Nonnull CSSSupportsRule cssSupportsRule) {
            if (!this.ignoreCssStatement || this.ignoredCssStatement.equals("supports")) {
                this.ignoreCssStatement = false;
                this.ignoredCssStatement = null;
                String content = cssSupportsRule.getAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0);
                unknownRules.add(new CssUnknownRule(content, getPosition(cssSupportsRule)));
            }
        }

        @Override
        public void onBeginViewportRule(@Nonnull CSSViewportRule cssViewportRule) {
            if (!this.ignoreCssStatement) {
                this.ignoreCssStatement = true;
                this.ignoredCssStatement = "viewport";
            }
        }

        @Override
        public void onEndViewportRule(@Nonnull CSSViewportRule cssViewportRule) {
            if (!this.ignoreCssStatement || this.ignoredCssStatement.equals("viewport")) {
                this.ignoreCssStatement = false;
                this.ignoredCssStatement = null;
                String content = cssViewportRule.getAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0);
                unknownRules.add(new CssUnknownRule(content, getPosition(cssViewportRule)));
            }
        }

        @Override
        public void onUnknownRule(@Nonnull CSSUnknownRule cssUnknownRule) {
            if (!this.ignoreCssStatement) {
                String content = cssUnknownRule.getAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0);
                unknownRules.add(new CssUnknownRule(content, getPosition(cssUnknownRule)));
            }

        }

        @Override
        public void onImport(@Nonnull CSSImportRule cssImportRule) {
            if (!this.ignoreCssStatement) {
                String content = cssImportRule.getAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0);
                unknownRules.add(new CssUnknownRule(content, getPosition(cssImportRule)));
            }
        }

        @Override
        public void onNamespace(@Nonnull CSSNamespaceRule cssNamespaceRule) {
            if (!this.ignoreCssStatement) {
                String content = cssNamespaceRule.getAsCSSString(new CSSWriterSettings(ECSSVersion.CSS30), 0);
                unknownRules.add(new CssUnknownRule(content, getPosition(cssNamespaceRule)));
            }
        }

        @Override
        public void end() {}


        private int getLineNumber(ICSSSourceLocationAware sourceLocation) {
            assert sourceLocation.getSourceLocation() != null;
            return sourceLocation.getSourceLocation().getFirstTokenBeginLineNumber();
        }

        private int getColumnNumber(ICSSSourceLocationAware sourceLocation) {
            assert sourceLocation.getSourceLocation() != null;
            return sourceLocation.getSourceLocation().getFirstTokenBeginColumnNumber();
        }

        private Position getPosition(ICSSSourceLocationAware sourceLocation) {
            return new Position(getLineNumber(sourceLocation), getColumnNumber(sourceLocation));
        }

    }
}