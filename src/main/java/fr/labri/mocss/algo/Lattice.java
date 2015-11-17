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

package fr.labri.mocss.algo;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Lattice {

    private Set<Node> nodes;

    public Lattice(Set<Node> nodes) {
        this.nodes = Sets.newHashSet(nodes);
    }

    public Set<Node> getNodes() {
        return this.nodes;
    }

    public Set<Node> getRoots() {
        return this.nodes.stream()
                .filter(node -> node.getParents().isEmpty())
                .collect(Collectors.toSet());
    }

    public Set<Node> getLeaves() {
        return this.nodes.stream()
                .filter(node -> node.getChildren().isEmpty())
                .collect(Collectors.toSet());
    }

    public List<Node> topologicalOrder() {
        List<Node> result = Lists.newLinkedList();
        Set<Node> visitedNodes = Sets.newHashSet();
        this.nodes.forEach(node -> topologicalOrderAux(node, visitedNodes, result));
        return result;
    }

    private void topologicalOrderAux(Node node, Set<Node> visitedNodes, List<Node> topologicalOrder) {
        if (!visitedNodes.contains(node)) {
            for (Node parent : node.getParents()) {
                topologicalOrderAux(parent, visitedNodes, topologicalOrder);
            }
            visitedNodes.add(node);
            topologicalOrder.add(node);
        }
    }

}
