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

public abstract class Declaration extends ElementWithIdentifier {

    private final Property property;
    private final Value value;

    public Declaration(Property property, Value value) {
        this.property = property;
        this.value = value;
    }

    public Property getPropertyReference() {
        return this.property;
    }

    public String getProperty() {
        return this.property.getProperty();
    }

    public Value getValueReference() {
        return this.value;
    }

    public String getValue() {
        return this.value.getValue();
    }

    @Override
    public String getIdentifier() {
        return this.property.getProperty() + ":" + this.value.getValue();
    }

    @Override
    public int hashCode() {
        return getIdentifier().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj == this) {
            return true;
        } else if (!(obj instanceof Declaration)) {
            return false;
        } else {
            final Declaration other = (Declaration) obj;
            return other.getIdentifier().equals(getIdentifier());
        }
    }

}
