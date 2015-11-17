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

package fr.labri.mocss.model;

import fr.labri.mocss.algo.fca.ElementWithIdentifier;

public class Property extends ElementWithIdentifier {

    private final String property;

    public Property(String property) {
        this.property = property;
    }

    public String getProperty() {
        return this.property;
    }

    @Override
    public String getIdentifier() {
        return this.property;
    }

    @Override
    public int hashCode() {
        return this.property.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj == this) {
            return true;
        } else if (!(obj instanceof Property)) {
            return false;
        } else {
            final Property other = (Property) obj;
            return other.property.equals(this.property);
        }
    }

}
