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

public class Gsh {

    private Context context;
    private Set<Concept> concepts;

    public Gsh(Context context) {
        this.context = context;
    }

    public void compute() {
        this.concepts = Sets.newHashSet();

        this.context.getEntities().forEach(entity -> this.concepts.add(u(entity)));
        this.context.getAttributes().forEach(attribute -> this.concepts.add(v(attribute)));

        Set<Concept> addedConcepts = Sets.newHashSet();
        this.concepts.forEach(concept -> add(concept, addedConcepts));
    }

    private Concept u(FcaElement entity) {
        Concept concept = new Concept();
        concept.getIntents().addAll(this.context.getAttributes(entity));
        Set<FcaElement> extents = Sets.newHashSet();
        extents.addAll(this.context.getEntities());
        this.context.getAttributes(entity).forEach(attribute ->
                extents.retainAll(this.context.getEntities(attribute))
        );
        concept.getExtents().addAll(extents);
        return concept;
    }

    private Concept v(FcaElement attribute) {
        Concept concept = new Concept();
        concept.getExtents().addAll(this.context.getEntities(attribute));
        Set<FcaElement> intents = Sets.newHashSet();
        intents.addAll(this.context.getAttributes());
        this.context.getEntities(attribute).forEach(entity ->
            intents.retainAll(this.context.getAttributes(entity))
        );
        concept.getIntents().addAll(intents);
        return concept;
    }

    private void add(Concept concept, Set<Concept> addedConcepts) {
        if (!addedConcepts.contains(concept)) {
            Set<Concept> allParents = allGreaters(concept);
            allParents.forEach(parent -> {
                if (!addedConcepts.contains(parent)) {
                    add(parent, addedConcepts);
                }
            });
            addedConcepts.add(concept);
            Set<Concept> smallestParents = selectSmallest(allParents);
            smallestParents.forEach(concept::addParent);
        }
    }

    private Set<Concept> allGreaters(Concept concept) {
        Set<Concept> allGreaters = Sets.newHashSet();
        this.concepts.forEach(candidate -> {
            if (candidate != concept && candidate.isGreaterThan(concept)) {
                allGreaters.add(candidate);
            }
        });
        return allGreaters;
    }

    private Set<Concept> selectSmallest(Set<Concept> concepts) {
        Set<Concept> smallests = Sets.newHashSet();
        concepts.forEach(concept -> push(concept, smallests));
        return smallests;
    }

    private void push(Concept concept, Set<Concept> smallests) {
        Set<Concept> swap = Sets.newHashSet();
        for (Concept current : smallests) {
            if (current.isSmallerThan(concept)) {
                return;
            } else if (concept.isSmallerThan(current)) {
                swap.add(current);
            }
        }
        if (swap.size() > 0) {
            smallests.removeAll(swap);
        }
        smallests.add(concept);
    }

    public Set<Concept> getConcepts() {
        return this.concepts;
    }

}
