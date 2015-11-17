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

package fr.labri.mocss.model.ssl;

import com.google.common.collect.Lists;
import fr.labri.mocss.model.Declaration;
import fr.labri.mocss.model.Property;

import java.util.List;
import java.util.Set;

public class SslMixin extends SslStatement {

    private String name;
    private List<Property> parameters;

    private SslMixin(List<Property> parameters, Set<Declaration> declarations, List<SslMixinCall> mixinCalls) {
        super(declarations, mixinCalls);
        this.parameters = parameters;
    }

    public SslMixin(String name, List<Property> parameters, Set<Declaration> declarations, List<SslMixinCall> mixinCalls) {
        this(parameters, declarations, mixinCalls);
        this.name = name;
    }

    public SslMixin(String name, Set<Declaration> declarations, List<SslMixinCall> mixinCalls) {
        this(name, Lists.newLinkedList(), declarations, mixinCalls);
    }

    public String getName() {
        return this.name;
    }

    public List<Property> getParameters() {
        return this.parameters;
    }

    public boolean hasParameters() {
        return !this.parameters.isEmpty();
    }

    @Override
    public int hashCode() {
        return super.hashCode() + this.parameters.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj == this) {
            return true;
        } else if (!(obj instanceof SslMixin)) {
            return false;
        } else {
            final SslMixin other = (SslMixin) obj;
            boolean parametersEq = other.parameters.equals(this.parameters);
            boolean declarationsEq = other.declarations.equals(this.declarations);
            boolean mixinCallsEq = other.mixinCalls.equals(this.mixinCalls);
            return parametersEq && declarationsEq && mixinCallsEq;
        }
    }

}
