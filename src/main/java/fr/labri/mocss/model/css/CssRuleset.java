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

package fr.labri.mocss.model.css;

import com.google.common.collect.Lists;
import fr.labri.mocss.model.DeclarationConcrete;
import fr.labri.mocss.model.ElementWithPosition;
import fr.labri.mocss.model.Position;
import fr.labri.mocss.model.Selector;

import java.util.List;

public class CssRuleset implements ElementWithPosition {

    private final Selector selector;
    private final List<DeclarationConcrete> declarations;
    private final Position position;

    public CssRuleset(Selector selector, List<DeclarationConcrete> declarations, Position position) {
        this.selector = selector;
        this.declarations = Lists.newArrayList(declarations);
        this.position = position;
    }

    public Selector getSelector() {
        return this.selector;
    }

    public List<DeclarationConcrete> getDeclarations() {
        return this.declarations;
    }

    @Override
    public Position getPosition() {
        return this.position;
    }

    @Override
    public int hashCode() {
        return this.selector.hashCode() + this.declarations.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj == this) {
            return true;
        } else if (!(obj instanceof CssRuleset)) {
            return false;
        } else {
            final CssRuleset other = (CssRuleset) obj;
            return other.selector.equals(this.selector) && other.declarations.equals(this.declarations);
        }
    }

}
