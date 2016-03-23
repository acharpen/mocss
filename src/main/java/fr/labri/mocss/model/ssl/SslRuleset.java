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
import com.google.common.collect.Sets;
import fr.labri.mocss.model.Declaration;
import fr.labri.mocss.model.Selector;

import java.util.List;
import java.util.Set;

public class SslRuleset extends SslStatement {

    private Set<Selector> selectors;

    public SslRuleset(Set<Selector> selectors, Set<Declaration> declarations, List<SslMixinCall> mixinCalls) {
        super(declarations, mixinCalls);
        this.selectors = selectors;
    }

    public SslRuleset(Set<Selector> selectors, SslMixinCall mixinCall) {
        this(selectors, Sets.newHashSet(), Lists.newArrayList(mixinCall));
    }

    public SslRuleset(Selector selector, Set<Declaration> declarations, List<SslMixinCall> mixinCalls) {
        this(Sets.newHashSet(selector), declarations, mixinCalls);
    }

    public SslRuleset(Selector selector, SslMixinCall mixinCall) {
        this(Sets.newHashSet(selector), Sets.newHashSet(), Lists.newArrayList(mixinCall));
    }

    public Set<Selector> getSelectors() {
        return this.selectors;
    }

    public Selector getOneSelector() {
        return this.selectors.iterator().next();
    }

    public Selector getUniqueSelector() {
        if (this.selectors.size() != 1) {
            throw new RuntimeException();
        }
        return this.selectors.iterator().next();
    }

    @Override
    public int hashCode() {
        return super.hashCode() + this.selectors.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj == this) {
            return true;
        } else if (!(obj instanceof SslRuleset)) {
            return false;
        } else {
            final SslRuleset other = (SslRuleset) obj;
            boolean selectorEq = other.selectors.equals(this.selectors);
            boolean declarationsEq = other.declarations.equals(this.declarations);
            boolean mixinCallsEq = other.mixinCalls.equals(this.mixinCalls);
            return selectorEq && declarationsEq && mixinCallsEq;
        }
    }

}
