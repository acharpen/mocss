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

import com.google.common.collect.Sets;
import fr.labri.mocss.model.Declaration;
import fr.labri.mocss.model.Selector;

import java.util.Iterator;
import java.util.Set;

public class Node {

    private int identifier;
    private Set<Node> parents;
    private Set<Node> children;
    private Set<Selector> selectors;
    private Set<Selector> allSelectors;
    private Set<Declaration> declarations;

    public Node(int identifier, Set<Selector> selectors, Set<Selector> allSelectors, Set<Declaration> declarations) {
        this.identifier = identifier;
        this.selectors = selectors;
        this.allSelectors = allSelectors;
        this.declarations = declarations;
        this.parents = Sets.newHashSet();
        this.children = Sets.newHashSet();
    }

    public Set<Selector> getSelectors() {
        return this.selectors;
    }

    public Set<Selector> getallSelectors() {
        return this.allSelectors;
    }

    public Set<Declaration> getDeclarations() {
        return this.declarations;
    }

    public Set<Declaration> getSimplifiedDeclarations() {
        Set<Declaration> result = Sets.newHashSet();
        this.declarations.forEach(declaration -> {
            boolean leafDeclaration = true;
            for (Node parent : this.parents) {
                if (parent.declarations.contains(declaration)) {
                    leafDeclaration = false;
                }
            }
            if (leafDeclaration) {
                result.add(declaration);
            }
        });
        return result;
    }

    public Set<Node> getParents() {
        return this.parents;
    }

    public Set<Node> getChildren() {
        return this.children;
    }

    public Set<Node> getAllParents() {
        Set<Node> result = Sets.newHashSet();
        Set<Node> tmp = Sets.newHashSet();
        tmp.add(this);
        while (!tmp.isEmpty()) {
            Node node = pickOne(tmp);
            if (!result.contains(node)) {
                result.add(node);
                tmp.addAll(node.getParents());
            }
        }
        return result;
    }

    public Set<Node> getAllChildren() {
        Set<Node> result = Sets.newHashSet();
        Set<Node> tmp = Sets.newHashSet();
        tmp.add(this);
        while (!tmp.isEmpty()) {
            Node node = pickOne(tmp);
            if (!result.contains(node)) {
                result.add(node);
                tmp.addAll(node.getChildren());
            }
        }
        return result;
    }

    private Node pickOne(Set<Node> nodes) {
        if (nodes.isEmpty()) {
            return null;
        }
        Iterator<Node> it = nodes.iterator();
        Node tmp = it.next();
        it.remove();
        return tmp;
    }

    @Override
    public String toString() {
        return "(" + this.selectors + "," + this.declarations + ")";
    }

    @Override
    public int hashCode() {
        return this.identifier;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj == this) {
            return true;
        } else if (!(obj instanceof Node)) {
            return false;
        } else {
            final Node other = (Node) obj;
            return other.identifier == this.identifier;
        }
    }

}
