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

import fr.labri.mocss.algo.CssToSsl;
import fr.labri.mocss.io.IoUtils;
import fr.labri.mocss.io.SassWriter;
import fr.labri.mocss.io.SslWriter;
import fr.labri.mocss.model.css.CssRuleset;
import fr.labri.mocss.model.ssl.SslMixin;
import fr.labri.mocss.model.ssl.SslRuleset;
import fr.labri.mocss.parser.css.Css3Parser;
import fr.labri.mocss.parser.css.CssParser;
import fr.labri.mocss.parser.css.CssParsingException;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class App {

    public static void main(String[] args) {
        /* Initialize configuration from command line arguments */
        if (!Config.getInstance().initializeFromCommandLine(args)) {
            IoUtils.exitOnSuccess();
        }

        /* Parse the css input file */
        CssParser cssParser = new Css3Parser();
        try {
            cssParser.parse(Config.getInstance().inputFile());
            System.out.println(">> Context: ");
            String cssFileInfo = String.format("%s: {rules: %d; selectors: %d; declarations: %d}",
                    Config.getInstance().inputFile().getName(),
                    cssParser.rulesNb(),
                    cssParser.selectorsNb(),
                    cssParser.declarationsNb());
            System.out.println(cssFileInfo);
        } catch (CssParsingException e) {
            System.err.println("error: failed to parse input file");
            IoUtils.printErrorAndExit(e);
        }
        cssParser.removeDuplicates();

        /* Extract mixins */
        Pair<List<SslMixin>, List<SslRuleset>> statements = null;
        try {
            statements = CssToSsl.compute(cssParser.getRulesets());
            System.out.print(">> Results: ");
            int mixinsNb = statements.getLeft().size();
            if (mixinsNb < 2) {
                System.out.println(mixinsNb+ " mixin generated");
            } else {
                System.out.println(mixinsNb + " mixins generated");
            }
            statements.getLeft().forEach(mixin -> {
                String line = String.format("Mixin %s: {parameters: %d; declarations: %d; uses: %d}",
                        mixin.getName(),
                        mixin.getParameters().size(),
                        mixin.getDeclarations().size(),
                        mixin.getSelectors().size());
                System.out.println(line);
            });
        } catch (Exception e) {
            System.err.println("error: failed to extract mixins");
            IoUtils.printErrorAndExit(e);
        }

        /* Save the mixins according to the chosen mode */
        SslWriter sslWriter = new SassWriter();
        switch (Config.getInstance().outputFormat()) {
            case SCSS:
                try {
                    sslWriter.writeGeneratedCode(statements, Config.getInstance().outputFile());
                } catch (IOException e) {
                    IoUtils.printErrorAndExit(e);
                }
                break;
        }

        /* Save unhandled css */
        if (!cssParser.getUnknownRules().isEmpty()) {
            try {
                sslWriter.writeUnhandledCode(cssParser.getUnknownRules(), Config.getInstance().outputFile());
            } catch (IOException e) {
                IoUtils.printErrorAndExit(e);
            }
        }
    }

}
