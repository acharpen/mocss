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

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.Map;
import java.util.Set;

public class Context {

    private Set<FcaElement> entities;
    private Set<FcaElement> attributes;
    private Map<FcaElement, Set<FcaElement>> relations;
    private Map<FcaElement, Set<FcaElement>> reverseRelations;

    public Context() {
        this.entities = Sets.newLinkedHashSet();
        this.attributes = Sets.newLinkedHashSet();
        this.relations = Maps.newLinkedHashMap();
        this.reverseRelations = Maps.newLinkedHashMap();
    }

    public void addEntity(FcaElement entity) {
        this.entities.add(entity);
    }

    public void addAttribute(FcaElement attribute) {
        this.attributes.add(attribute);
    }

    public void addRelation(FcaElement entity, FcaElement attribute) {
        if (!this.relations.containsKey(entity)) {
            this.relations.put(entity, Sets.newHashSet());
        }
        this.relations.get(entity).add(attribute);

        if (!this.reverseRelations.containsKey(attribute)) {
            this.reverseRelations.put(attribute, Sets.newHashSet());
        }
        this.reverseRelations.get(attribute).add(entity);
    }

    public Set<FcaElement> getEntities() {
        return this.entities;
    }

    public Set<FcaElement> getAttributes() {
        return this.attributes;
    }

    public Set<FcaElement> getEntities(FcaElement attribute) {
        if (this.reverseRelations.containsKey(attribute)) {
            return this.reverseRelations.get(attribute);
        } else {
            return Sets.newHashSet();
        }
    }

    public Set<FcaElement> getAttributes(FcaElement entity) {
        if (this.relations.containsKey(entity)) {
            return this.relations.get(entity);
        } else {
            return Sets.newHashSet();
        }
    }

    public void print() {
        System.out.println("Entities:");
        this.entities.forEach(entity -> System.out.println("\t" + entity.getIdentifier()));
        System.out.println();

        System.out.println("Attributes:");
        this.attributes.forEach(attribute -> System.out.println("\t" + attribute.getIdentifier()));
        System.out.println();

        System.out.println("Relations:");
        this.relations.forEach((entity, attributes) ->
            attributes.forEach(attribute ->
                            System.out.println("\t" + entity.getIdentifier() + " <=> " + attribute.getIdentifier())
            )
        );
        System.out.println();

        System.out.println("Reverse relations:");
        this.reverseRelations.forEach((entity, attributes) -> {
            attributes.forEach(attribute ->
                            System.out.println("\t" + entity.getIdentifier() + " <=> " + attribute.getIdentifier())
            );
        });
    }

    @Override
    public int hashCode() {
        return this.entities.hashCode() + this.attributes.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        } else if (obj == this) {
            return true;
        } else if (!(obj instanceof Context)) {
            return false;
        } else {
            final Context other = (Context) obj;
            return other.entities.equals(this.entities) && other.attributes.equals(this.attributes);
        }
    }

}
