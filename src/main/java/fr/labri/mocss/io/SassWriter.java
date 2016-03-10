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

import fr.labri.mocss.model.Declaration;
import fr.labri.mocss.model.DeclarationAbstract;
import fr.labri.mocss.model.Selector;
import fr.labri.mocss.model.ssl.SslMixin;
import fr.labri.mocss.model.ssl.SslMixinCall;
import fr.labri.mocss.model.ssl.SslRuleset;
import fr.labri.mocss.model.ssl.SslStatement;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SassWriter extends SslWriter {

    @Override
    public void writeMixins(List<SslMixin> mixins, Writer writer) throws IOException {
        for (SslMixin mixin : mixins) {
            writer.write("// Declarations are factorized from the following selectors:\n");
            for (Selector selector : mixin.getSelectors()) {
                writer.write(String.format("// %s (line %d)\n",
                        selector.getSelector(), selector.getPosition().getLineNumber()));
            }
            String identifier = mixin.getName();
            if (mixin.hasParameters()) {
                String parametersAsString = StringUtils.join(
                        mixin.getParameters().stream()
                                .map(property -> "$" + property.getProperty()).collect(Collectors.toList()),
                        ", ");
                writer.write(String.format("@mixin %s(%s)", identifier, parametersAsString));
            } else {
                writer.write(String.format("@mixin %s", identifier));
            }
            writeStatementContent(mixin, writer);
        }
    }

    @Override
    public void writeRulesets(List<SslRuleset> rulesets, Writer writer) throws IOException {
        for (SslRuleset ruleset : rulesets) {
            String selectorsAsString = StringUtils.join(
                    ruleset.getSelectors().stream()
                            .map(Selector::getSelector).collect(Collectors.toList()),
                    ", ");
            writer.write(selectorsAsString);
            writeStatementContent(ruleset, writer);
        }
    }

    protected void writeStatementContent(SslStatement statement, Writer writer) throws IOException {
        writer.write(" {\n");
        writeMixinCalls(statement.getMixinCalls(), writer);
        writeDeclarations(statement.getDeclarations(), writer);
        writer.write("}\n");
    }

    protected void writeMixinCalls(List<SslMixinCall> mixinCalls, Writer writer) throws IOException {
        for (SslMixinCall mixinCall : mixinCalls) {
            String identifier = mixinCall.getMixin().getName();
            if (mixinCall.getMixin().hasParameters()) {
                String parametersAsString = StringUtils.join(
                        mixinCall.getParameters().stream()
                                .map(declaration -> {
                                    String value = declaration.getValue();
                                    if (declaration instanceof DeclarationAbstract) {
                                        value = formatAbstractValue(value);
                                    } else if (value.contains(",")) {
                                        value = protectValue(value);
                                    }
                                    return value;
                                })
                                .collect(Collectors.toList()),
                        ", ");
                writer.write(String.format("  @include %s(%s);\n", identifier, parametersAsString));
            } else {
                writer.write(String.format("  @include %s;\n", identifier));
            }
        }
    }

    protected void writeDeclarations(Set<Declaration> declarations, Writer writer) throws IOException {
        for (Declaration declaration : declarations) {
            String property = declaration.getProperty();
            String value = declaration.getValue();
            if (declaration instanceof DeclarationAbstract) {
                value = formatAbstractValue(value);
            }
            writer.write(String.format("  %s: %s;\n", property, value));
        }
    }

    protected String formatAbstractValue(String value) {
        return "$" + value;
    }

    protected String protectValue(String value) {
        return "#{'" + value + "'}";
    }

}
