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

import fr.labri.mocss.model.Declaration;

import java.util.List;
import java.util.Set;

public abstract class SslStatement {

    protected Set<Declaration> declarations;
    protected List<SslMixinCall> mixinCalls;

    public SslStatement(Set<Declaration> declarations, List<SslMixinCall> mixinCalls) {
        this.declarations = declarations;
        this.mixinCalls = mixinCalls;
    }

    public Set<Declaration> getDeclarations() {
        return this.declarations;
    }

    public List<SslMixinCall> getMixinCalls() {
        return this.mixinCalls;
    }

    @Override
    public int hashCode() {
        return this.declarations.hashCode() + this.mixinCalls.hashCode();
    }

}
