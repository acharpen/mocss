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

import com.google.common.io.Files;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.ParserProperties;

import java.io.File;
import java.io.PrintStream;
import java.util.function.Consumer;

public class Config {

    @Option(name =  "-i",
            aliases = { "--input" },
            usage = "the css input file",
            required = true)
    private File inputFile;

    @Option(name = "-o",
            aliases = { "--output" },
            usage = "the output file")
    private File outputFile;

    public enum OutputFormat { SCSS }
    @Option(name = "-f",
            aliases = { "--output-format" },
            usage = "language of the output file: ")
    private OutputFormat outputFormat = OutputFormat.SCSS;

    @Option(name = "--groups-filter",
            usage = "generates mixins factorizing common properties.")
    private boolean groupsFilter = false;

    public enum SemanticPreserving { full, slight, none }
    @Option(name = "--keep-semantic",
            usage = "determines whether semantics of the input file has to be preserved.\n" +
                    "full: semantics is preserved and additional mixins are generated to avoid duplication;\n" +
                    "slight: semantics is preserved without new mixins;\n" +
                    "none: semantics is not preserved.",
            metaVar = "VALUE")
    private SemanticPreserving semantic = SemanticPreserving.full;

    @Option(name = "--no-duplicates-into-rule",
            usage = "avoids duplicated declarations in ruleset. "
                    + "If sets, more mixins are generated.")
    private boolean noDuplicatesInRuleset = false;

    @Option(name = "--min-children",
            usage = "avoids mixins used less than VALUE times. " +
                    "Lower the value is, more mixins are generated.",
            metaVar = "VALUE")
    private int childrenMinNb = 2;

    @Option(name = "--min-declarations",
            usage = "avoids mixins introducing less than VALUE declarations. " +
                    "Lower the value is, more mixins are generated.",
            metaVar = "VALUE")
    private int declarationsMinNb = 3;

    @Option(name = "--max-parameters",
            usage = "avoids mixins having more than VALUE parameters. " +
                    "Lower the value is, less mixins are generated.",
            metaVar = "VALUE")
    private int parametersMaxNb = 1;

    @Option(name = "--debug",
            usage = "debug mode")
    private boolean debug = false;

    @Option(name = "-h",
            aliases = { "--help" },
            usage = "help",
            help = true)
    private boolean help = false;


    public boolean initializeFromCommandLine(String[] args) {
        Consumer<CmdLineParser> printUsage = parser -> {
            PrintStream printStream = System.out;
            printStream.println("Usage: <main class> [options]\n Options:");
            parser.printUsage(printStream);
        };

        ParserProperties parserProperties = ParserProperties.defaults();
        parserProperties.withUsageWidth(100);
        CmdLineParser parser = new CmdLineParser(getInstance(), parserProperties);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.out.println(e.getMessage() + "\n");
            printUsage.accept(parser);
            return false;
        }

        if (this.help) {
            printUsage.accept(parser);
            return false;
        } else {
            return checkConfig();
        }
    }

    public void setInputFile(File inputFile) {
        this.inputFile = inputFile;
    }

    public void setOutputFile(File outputFile) {
        this.outputFile = outputFile;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean checkConfig() {
        /*
         * Input File
         */
        if (!this.inputFile.exists()) {
            System.err.println("error: input file does not exist");
            return false;
        }
        if (!this.inputFile.isFile()) {
            System.err.println("error: input file is not a regular file");
            return false;
        }

        /*
         * Output file
         */
        if (this.outputFile == null) {
            String outputFilename = Files.getNameWithoutExtension(this.inputFile.toString())
                    + "." + this.outputFormat.name().toLowerCase();
            this.outputFile = new File(this.inputFile().getParent(), outputFilename);
            System.out.println("info: using \"" + this.outputFile + "\" as output file");
        }
        if (this.outputFile.exists()) {
            System.err.println("error: output file already exists");
            return false;
        }

        return true;
    }

    public File inputFile() {
        return this.inputFile;
    }

    public File outputFile() {
        return this.outputFile;
    }

    public OutputFormat outputFormat() {
        return this.outputFormat;
    }

    public boolean groupsFilter() {
        return this.groupsFilter;
    }

    public boolean preserveSemantic() {
        return this.semantic.equals(SemanticPreserving.full) ||
                this.semantic.equals(SemanticPreserving.slight);
    }

    public boolean preserveSemanticWithoutMixins() {
        return this.semantic.equals(SemanticPreserving.slight);
    }

    public boolean noDuplicatesInRuleset() {
        return this.noDuplicatesInRuleset;
    }

    public int childrenMinNb() {
        return this.childrenMinNb;
    }

    public int declarationsMinNb() {
        return this.declarationsMinNb;
    }

    public int parametersMaxNb() {
        return this.parametersMaxNb;
    }

    public boolean debug() {
        return this.debug;
    }


    private static final Config instance = new Config();

    public static Config getInstance() {
        return instance;
    }

    private Config() {}

}
