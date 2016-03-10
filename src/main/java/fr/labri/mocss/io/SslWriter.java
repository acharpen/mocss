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

package fr.labri.mocss.io;

import fr.labri.mocss.model.ElementWithPositionComparator;
import fr.labri.mocss.model.css.CssUnknownRule;
import fr.labri.mocss.model.ssl.SslMixin;
import fr.labri.mocss.model.ssl.SslRuleset;
import org.apache.commons.lang3.tuple.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;

public abstract class SslWriter {

    public void writeGeneratedCode(Pair<List<SslMixin>, List<SslRuleset>> statements, File outputFile) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(outputFile.toPath(), Charset.defaultCharset(),
                StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {
            writeMixins(statements.getLeft(), writer);
            writer.write("\n");
            writeRulesets(statements.getRight(), writer);
        } catch (IOException e) {
            System.err.println("error: failed to write file: " + outputFile);
            throw e;
        }
    }

    public void writeUnhandledCode(List<CssUnknownRule> unknownRules, File outputFile) throws IOException {
        Collections.sort(unknownRules, new ElementWithPositionComparator());
        try (BufferedWriter writer = Files.newBufferedWriter(outputFile.toPath(), Charset.defaultCharset(),
                StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.APPEND)) {
            writer.write("\n// Unhandled css code\n");
            for (CssUnknownRule unknownRule : unknownRules) {
                writer.write(unknownRule.getContent());
                writer.write("\n");
            }
        } catch (IOException e) {
            System.err.println("error: failed to write file: " + outputFile);
            throw e;
        }
    }

    public abstract void writeMixins(List<SslMixin> mixins, Writer writer) throws IOException;

    public abstract void writeRulesets(List<SslRuleset> rulesets, Writer writer) throws IOException;

}
