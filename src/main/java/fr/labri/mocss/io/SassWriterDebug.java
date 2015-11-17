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
import fr.labri.mocss.model.ssl.SslMixinCall;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SassWriterDebug extends SassWriter {

    @Override
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
                                    } else {
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

    @Override
    protected void writeDeclarations(Set<Declaration> declarations, Writer writer) throws IOException {
        for (Declaration declaration : declarations) {
            String property = declaration.getProperty();
            String value = declaration.getValue();
            if (declaration instanceof DeclarationAbstract) {
                value = formatAbstractValue(value);
            } else {
                value = protectValue(value);
            }
            writer.write(String.format("  %s: %s;\n", property, value));
        }
    }

}
