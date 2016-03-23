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

import fr.labri.mocss.algo.Lattice;
import fr.labri.mocss.algo.Node;

import java.util.Set;
import java.util.function.Consumer;

public abstract class FilteringNodesAlgorithm {

    public void filter(Lattice lattice) {
        Consumer<Node> handleNode = node -> {
            Set<Node> parents = node.getParents();
            Set<Node> children = node.getChildren();
            if (node.getSelectors().isEmpty()) {
                parents.forEach(parent -> {
                    boolean removed = parent.getChildren().remove(node);
                    assert removed;
                });
                children.forEach(child -> {
                    boolean removed = child.getParents().remove(node);
                    assert removed;
                });
                boolean removed = lattice.getNodes().remove(node);
                assert removed;
            } else {
                children.forEach(child -> {
                    boolean removed = child.getParents().remove(node);
                    assert removed;
                });
                node.getChildren().clear();
            }
            parents.forEach(parent -> {
                children.forEach(child -> {
                    Set<Node> descendants = parent.getAllChildren();
                    if (!descendants.contains(child)) {
                        parent.getChildren().add(child);
                        child.getParents().add(parent);
                    }
                });
            });
        };

        lattice.topologicalOrder().forEach(node -> {
            if (!isValidNode(node)) {
                handleNode.accept(node);
            }
        });
    }

    protected abstract boolean isValidNode(Node node);

}
