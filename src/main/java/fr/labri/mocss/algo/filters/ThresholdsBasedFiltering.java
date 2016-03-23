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

package fr.labri.mocss.algo.filters;

import fr.labri.mocss.Config;
import fr.labri.mocss.algo.Node;
import fr.labri.mocss.model.DeclarationAbstract;

public class ThresholdsBasedFiltering extends FilteringNodesAlgorithm {

    @Override
    protected boolean isValidNode(Node node) {
        int childrenNb = node.getChildren().size();
        long declarationsNb = node.getSimplifiedDeclarations().size();
        long parametersNb = node.getDeclarations().stream()
                .filter(declaration -> declaration instanceof DeclarationAbstract)
                .count();
        if ((childrenNb > 0 && childrenNb < Config.getInstance().childrenMinNb())
                || declarationsNb < Config.getInstance().declarationsMinNb()
                || parametersNb > Config.getInstance().parametersMaxNb()) {
            return false;
        } else {
            return true;
        }
    }

}
