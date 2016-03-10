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

package fr.labri.mocss.parser.css;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import fr.labri.mocss.model.DeclarationConcrete;
import fr.labri.mocss.model.ElementWithPositionComparator;
import fr.labri.mocss.model.Property;
import fr.labri.mocss.model.Selector;
import fr.labri.mocss.model.css.CssRuleset;
import fr.labri.mocss.model.css.CssUnknownRule;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class CssParser {

    protected List<CssRuleset> rulesets;
    protected List<CssUnknownRule> unknownRules;
    protected int rulesNb;
    protected int selectorsNb;
    protected int declarationsNb;

    public CssParser() {
        this.rulesets = Lists.newArrayList();
        this.unknownRules = Lists.newArrayList();
    }

    public List<CssRuleset> getRulesets() {
        return this.rulesets;
    }

    public List<CssUnknownRule> getUnknownRules() {
        return this.unknownRules;
    }

    public void removeDuplicates() {
        /* Remove duplicated declarations inside a ruleset */
        this.rulesets.forEach(currentRuleset -> {
            List<DeclarationConcrete> declarations = currentRuleset.getDeclarations();
            Collections.sort(declarations, new ElementWithPositionComparator());
            Collections.reverse(declarations);
            List<Property> existingProperties = Lists.newArrayList();
            List<DeclarationConcrete> uniqueDeclarations = Lists.newArrayList();
            declarations.forEach(currentDeclaration -> {
                if (!existingProperties.contains(currentDeclaration.getPropertyReference())) {
                    existingProperties.add(currentDeclaration.getPropertyReference());
                    uniqueDeclarations.add(currentDeclaration);
                }
            });
            currentRuleset.getDeclarations().clear();
            currentRuleset.getDeclarations().addAll(uniqueDeclarations);
        });

        /* Remove duplicated declarations among rulesets with the same selector */
        Collections.sort(this.rulesets, new ElementWithPositionComparator());
        Collections.reverse(this.rulesets);
        Map<Selector, Set<Property>> mappingSelectorProperties = Maps.newHashMap();
        this.rulesets.forEach(currentRuleset -> {
            Selector selector = currentRuleset.getSelector();
            if (mappingSelectorProperties.containsKey(selector)) {
                Set<Property> existingProperties = mappingSelectorProperties.get(selector);
                Set<DeclarationConcrete> uniqueDeclarations = Sets.newHashSet();
                currentRuleset.getDeclarations().forEach(declaration -> {
                    if (!existingProperties.contains(declaration.getPropertyReference())) {
                        existingProperties.add(declaration.getPropertyReference());
                        uniqueDeclarations.add(declaration);
                    }
                });
                currentRuleset.getDeclarations().clear();
                currentRuleset.getDeclarations().addAll(uniqueDeclarations);
            } else {
                Set<Property> properties = currentRuleset.getDeclarations().stream()
                        .map(declaration -> declaration.getPropertyReference())
                        .collect(Collectors.toSet());
                mappingSelectorProperties.put(selector, Sets.newHashSet(properties));
            }
        });
    }

    public int rulesNb() {
        return this.rulesNb;
    }

    public int selectorsNb() {
        return this.selectorsNb;
    }

    public int declarationsNb() {
        return this.declarationsNb;
    }

    public abstract void parse(File cssFile) throws CssParsingException;

}
