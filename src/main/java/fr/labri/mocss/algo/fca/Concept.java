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

import com.google.common.collect.Sets;

import java.util.Set;

public class Concept {

    private Set<Concept> parents;
    private Set<Concept> children;
    private Set<FcaElement> extents;
    private Set<FcaElement> intents;

    public Concept() {
        this.parents = Sets.newHashSet();
        this.children = Sets.newHashSet();
        this.extents = Sets.newHashSet();
        this.intents = Sets.newHashSet();
    }

    public void addParent(Concept concept) {
        this.parents.add(concept);
        concept.children.add(this);
    }

    public void addChild(Concept concept) {
        this.children.add(concept);
        concept.parents.add(this);
    }

    public void removeParent(Concept concept) {
        this.parents.remove(concept);
        concept.children.remove(this);
    }

    public void removeChild(Concept concept) {
        this.children.remove(concept);
        concept.parents.remove(this);
    }

    public Set<Concept> getParents() {
        return this.parents;
    }

    public Set<Concept> getChildren() {
        return this.children;
    }

    public Set<FcaElement> getExtents() {
        return this.extents;
    }

    public Set<FcaElement> getIntents() {
        return this.intents;
    }

    public Set<FcaElement> getSimplifiedExtents() {
        Set<FcaElement> simplifiedExtents = Sets.newHashSet();
        this.getExtents().forEach(entity -> {
            boolean leafEntity = true;
            for (Concept child : this.children) {
                if (child.getExtents().contains(entity)) {
                    leafEntity = false;
                }
            }
            if (leafEntity) {
                simplifiedExtents.add(entity);
            }
        });
        return simplifiedExtents;
    }

    public boolean isGreaterThan(Concept concept) {
        return this.extents.containsAll(concept.extents);
    }

    public boolean isSmallerThan(Concept concept) {
        return concept.extents.containsAll(this.extents);
    }

    public boolean isEntity() {
        return getSimplifiedExtents().size() == 1;
    }

    public boolean isEntityFusion() {
        return getSimplifiedExtents().size() > 1;
    }

    public boolean isNewEntity() {
        return getSimplifiedExtents().size() < 1;
    }

    @Override
    public int hashCode() {
        return this.intents.hashCode() + this.extents.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj == this) {
            return true;
        } else if (!(obj instanceof Concept)) {
            return false;
        } else {
            final Concept other = (Concept) obj;
            return other.intents.equals(this.intents) && other.extents.equals(this.extents);
        }
    }

}
