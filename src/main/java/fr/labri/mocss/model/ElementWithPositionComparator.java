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

import java.util.Comparator;

public class ElementWithPositionComparator implements Comparator<ElementWithPosition> {

    @Override
    public int compare(ElementWithPosition obj1, ElementWithPosition obj2) {
        int obj1Position = obj1.getLineNumber();
        int obj2Position = obj2.getLineNumber();
        if (obj1Position < obj2Position) return -1;
        else if (obj1Position == obj2Position) return 0;
        else return 1;
    }

}
