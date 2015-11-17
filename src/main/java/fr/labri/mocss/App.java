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
import fr.labri.mocss.model.ssl.SslMixin;
import fr.labri.mocss.model.ssl.SslRuleset;
import fr.labri.mocss.parser.css.Css3Parser;
import fr.labri.mocss.parser.css.CssParser;
import fr.labri.mocss.parser.css.CssParsingException;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
        } catch (CssParsingException e) {
            System.err.println("error: failed to parse input file");
            IoUtils.printErrorAndExit(e);
        }
        cssParser.removeDuplicates();

        /* Extract mixins */
        Pair<List<SslMixin>, List<SslRuleset>> statements = null;
        try {
            statements = CssToSsl.compute(cssParser.getRulesets());
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
            String parentDirectory = Config.getInstance().outputFile().getParent();
            if (parentDirectory == null) parentDirectory = ".";
            File unknownRulesOutput = new File(parentDirectory, "unhandledCss.css");
            try {
                sslWriter.writeUnhandledCode(cssParser.getUnknownRules(), unknownRulesOutput);
            } catch (IOException e) {
                IoUtils.printErrorAndExit(e);
            }
        }
    }

}
