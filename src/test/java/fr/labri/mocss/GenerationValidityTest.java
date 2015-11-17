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

package fr.labri.mocss;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.vaadin.sass.internal.ScssStylesheet;
import fr.labri.mocss.algo.CssToSsl;
import fr.labri.mocss.io.IoUtils;
import fr.labri.mocss.io.SassWriterDebug;
import fr.labri.mocss.io.SslWriter;
import fr.labri.mocss.model.DeclarationConcrete;
import fr.labri.mocss.model.Selector;
import fr.labri.mocss.model.css.CssRuleset;
import fr.labri.mocss.model.ssl.SslMixin;
import fr.labri.mocss.model.ssl.SslRuleset;
import fr.labri.mocss.parser.css.Css3Parser;
import fr.labri.mocss.parser.css.CssParser;
import fr.labri.mocss.parser.css.CssParsingException;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

public class GenerationValidityTest {

    private Config config;
    private File sassGeneratedFile;
    private File cssGeneratedFile;

    public GenerationValidityTest() {
        File cssInputFile = new File("src/main/resources/test.css");

        String directory = cssInputFile.getParent();
        this.sassGeneratedFile = new File(directory, "/generatedSass.scss");
        this.cssGeneratedFile = new File(directory, "/generatedCss.css");

        this.config = Config.getInstance();
        this.config.setDebug(true);
        this.config.setInputFile(cssInputFile);
        this.config.setOutputFile(this.sassGeneratedFile);
        this.config.setIgnoreSemantic(false);
        this.config.setNoDuplicatesInRuleset(false);
    }

    @Before
    public void before() {
        if (!this.config.checkConfig()) {
            IoUtils.exitOnError();
        }
    }

    @Test
    public void test() {
        /*
         * Read initial css code
         */
        CssParser cssParserInputFile = new Css3Parser();
        try {
            cssParserInputFile.parse(Config.getInstance().inputFile());
            cssParserInputFile.removeDuplicates();
        } catch (CssParsingException e) {
            IoUtils.printErrorAndExit(e);
        }


        /*
         * Generate sass code from initial css code
         */
        Pair<List<SslMixin>, List<SslRuleset>> statements = null;
        try {
            statements = CssToSsl.compute(cssParserInputFile.getRulesets());
        } catch (Exception e) {
            IoUtils.printErrorAndExit(e);
        }


        /*
         * Write generated sass code
         */
        try {
            SslWriter sslWriter = new SassWriterDebug();
            sslWriter.writeGeneratedCode(statements, Config.getInstance().outputFile());
        } catch (IOException e) {
            IoUtils.printErrorAndExit(e);
        }


        /*
         * Generate css code from generated sass code
         */
        try {
            ScssStylesheet stylesheet = ScssStylesheet.get(this.sassGeneratedFile.toString());
            stylesheet.compile();
            Files.write(this.cssGeneratedFile.toPath(), stylesheet.printState().getBytes());
        } catch (Exception e) {
            IoUtils.printErrorAndExit(e);
        }


        /*
         * Read generated css code
         */
        CssParser cssParserGeneratedFile = new Css3Parser();
        try {
            cssParserGeneratedFile.parse(this.cssGeneratedFile);
            if (!Config.getInstance().noDuplicatesInRuleset()) {
                cssParserGeneratedFile.removeDuplicates();
            }
        } catch (CssParsingException e) {
            IoUtils.printErrorAndExit(e);
        }


        /*
         * Compare initial css file and generated css file,
         * according to the configuration
         */
        List<CssRuleset> initialRulesets = cssParserInputFile.getRulesets();
        List<CssRuleset> generatedRulesets = cssParserGeneratedFile.getRulesets();

        if (Config.getInstance().ignoreSemantic()) {
            Function<List<CssRuleset>, Map<String, List<DeclarationConcrete>>> mergeIdenticalSelectors = rulesets -> {
                Map<String, List<DeclarationConcrete>> result = new HashMap<>();
                rulesets.forEach(ruleset -> {
                    String selector = ruleset.getSelector().getSelector();
                    List<DeclarationConcrete> declarations = ruleset.getDeclarations();
                    if (!result.containsKey(selector)) {
                        result.put(selector, Lists.newArrayList());
                    }
                    result.get(selector).addAll(declarations);
                });
                return result;
            };

            Map<String, List<DeclarationConcrete>> initialData = mergeIdenticalSelectors.apply(initialRulesets);
            Map<String, List<DeclarationConcrete>> generatedData = mergeIdenticalSelectors.apply(generatedRulesets);

            assertEquals(initialData.keySet(), generatedData.keySet());

            initialData.forEach((initialSelector, initialDeclarations) -> {
                List<DeclarationConcrete> generatedDeclarations = generatedData.get(initialSelector);

                if (Config.getInstance().noDuplicatesInRuleset()) {
                    assertEquals(initialDeclarations.size(), generatedDeclarations.size());
                }

                assertEquals(Sets.newHashSet(initialDeclarations), Sets.newHashSet(generatedDeclarations));
            });
        }
        else {
            Consumer<List<CssRuleset>> rulesetsSorting = r -> {
                Collections.sort(r, (CssRuleset r1, CssRuleset r2) -> {
                    Selector s1 = r1.getSelector();
                    Selector s2 = r2.getSelector();
                    int firstRulesetPosition = s1.getPosition().getLineNumber();
                    int secondRulesetPosition = s2.getPosition().getLineNumber();
                    if (firstRulesetPosition < secondRulesetPosition) {
                        return -1;
                    } else if (firstRulesetPosition == secondRulesetPosition) {
                        return s1.getSelector().compareTo(s2.getSelector());
                    } else {
                        return 1;
                    }
                });
            };

            assertEquals(initialRulesets.size(), generatedRulesets.size());

            rulesetsSorting.accept(initialRulesets);
            rulesetsSorting.accept(generatedRulesets);

            for (int i = 0; i < initialRulesets.size(); i++) {
                CssRuleset initialRuleset = initialRulesets.get(i);
                CssRuleset generatedRuleset = generatedRulesets.get(i);

                String initialSelector = initialRuleset.getSelector().getSelector();
                String generatedSelector = generatedRuleset.getSelector().getSelector();

                List<DeclarationConcrete> initialDeclarations = initialRuleset.getDeclarations();
                List<DeclarationConcrete> generatedDeclarations = generatedRuleset.getDeclarations();

                assertEquals(initialSelector, generatedSelector);

                if (Config.getInstance().noDuplicatesInRuleset()) {
                    assertEquals(initialDeclarations.size(), generatedDeclarations.size());
                }

                assertEquals(Sets.newHashSet(initialDeclarations), Sets.newHashSet(generatedDeclarations));
            }
        }
    }

    @After
    public void after() {
        this.cssGeneratedFile.delete();
        this.sassGeneratedFile.delete();
    }

}
