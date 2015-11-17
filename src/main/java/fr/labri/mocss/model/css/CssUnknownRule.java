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

import fr.labri.mocss.model.ElementWithPosition;
import fr.labri.mocss.model.Position;

public class CssUnknownRule implements ElementWithPosition {

    private final String content;
    private final Position position;

    public CssUnknownRule(String content, Position position) {
        this.content = content;
        this.position = position;
    }

    public String getContent() {
        return this.content;
    }

    @Override
    public Position getPosition() {
        return this.position;
    }

    @Override
    public int hashCode() {
        return this.content.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj == this) {
            return true;
        } else if (!(obj instanceof CssUnknownRule)) {
            return false;
        } else {
            final CssUnknownRule other = (CssUnknownRule) obj;
            return other.content.equals(this.content);
        }
    }

}
