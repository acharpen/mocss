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
 * Copyright 2015 Jean-Rémy Falleri <jr.falleri@gmail.com>
 */

package fr.labri.mocss.algo.fca;

public final class FcaElement {

    private final ElementWithIdentifier element;

    public FcaElement(ElementWithIdentifier element) {
        this.element = element;
    }

    public ElementWithIdentifier getElement() {
        return this.element;
    }

    public String getIdentifier() {
        return this.element.getIdentifier();
    }

    @Override
    public String toString() {
        return getIdentifier();
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
        } else if (!(obj instanceof FcaElement)) {
            return false;
        } else {
            final FcaElement other = (FcaElement) obj;
            return other.getIdentifier().equals(getIdentifier());
        }
    }

}
