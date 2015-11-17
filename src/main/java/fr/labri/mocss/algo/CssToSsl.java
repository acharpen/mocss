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
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import fr.labri.mocss.Config;
import fr.labri.mocss.algo.fca.*;
import fr.labri.mocss.model.*;
import fr.labri.mocss.model.css.CssRuleset;
import fr.labri.mocss.model.ssl.SslMixin;
import fr.labri.mocss.model.ssl.SslMixinCall;
import fr.labri.mocss.model.ssl.SslRuleset;
import fr.labri.mocss.model.ssl.SslStatement;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CssToSsl {

    private static long mixinsNb = 0;

    public static Pair<List<SslMixin>, List<SslRuleset>> compute(List<CssRuleset> rulesets) throws Exception {
        Context fcaContext = buildContext(rulesets);

        Gsh gsh = new Gsh(fcaContext);
        gsh.compute();

        Lattice lattice = generateLatticeFromConcepts(gsh.getConcepts());
        removedUnusedProperties(lattice);

        if (Config.getInstance().noDuplicatesInRuleset()) {
            spanningArborescence(lattice);
        }

        filterNodes(lattice);

        List<SslStatement> statements = generateStatements(lattice);
        List<SslMixin> generatedMixins = statements.stream()
                .filter(statement -> statement instanceof SslMixin)
                .map(mixin -> (SslMixin) mixin)
                .collect(Collectors.toList());
        List<SslRuleset> generatedRulesets = statements.stream()
                .filter(statement -> statement instanceof SslRuleset)
                .map(ruleset -> (SslRuleset) ruleset)
                .collect(Collectors.toList());

        if (!Config.getInstance().ignoreSemantic()) {
            splitSelectors(generatedMixins, generatedRulesets);
            mergeRulesetsWithSamePosition(generatedRulesets);
            orderGeneratedRulesets(generatedRulesets);
        }

        return new ImmutablePair<>(generatedMixins, generatedRulesets);
    }

    private static Context buildContext(List<CssRuleset> rulesets) {
        Context context = new Context();
        rulesets.forEach(ruleset -> {
            Selector selector = ruleset.getSelector();
            List<DeclarationConcrete> declarations = ruleset.getDeclarations();
            List<Property> properties = declarations.stream()
                    .map(declaration -> new Property(declaration.getProperty()))
                    .collect(Collectors.toList());

            FcaElement fcaElementSelector = new FcaElement(selector);
            context.addEntity(fcaElementSelector);
            declarations.forEach(declaration -> {
                FcaElement fcaElementDeclaration = new FcaElement(declaration);
                context.addAttribute(fcaElementDeclaration);
                context.addRelation(fcaElementSelector, fcaElementDeclaration);
            });
            properties.forEach(property -> {
                FcaElement fcaElementProperty = new FcaElement(property);
                context.addAttribute(fcaElementProperty);
                context.addRelation(fcaElementSelector, fcaElementProperty);
            });
        });
        return context;
    }

    private static Lattice generateLatticeFromConcepts(Set<Concept> concepts) {
        Set<Node> nodes = Sets.newHashSet();
        Map<Concept, Node> mapping = Maps.newHashMap();
        int i = 0;
        for (Concept concept : concepts) {
            Set<Selector> selectors = concept.getSimplifiedExtents().stream()
                    .map(extent -> (Selector) extent.getElement())
                    .collect(Collectors.toSet());
            Set<Declaration> declarations = concept.getIntents().stream()
                    .map(intent -> {
                        ElementWithIdentifier fcaElement = intent.getElement();
                        assert fcaElement instanceof DeclarationConcrete || fcaElement instanceof Property;
                        if (fcaElement instanceof DeclarationConcrete) {
                            return (DeclarationConcrete) fcaElement;
                        } else {
                            Property property = (Property) fcaElement;
                            return new DeclarationAbstract(property, new ValueAbstract(property.getProperty()));
                        }
                    })
                    .collect(Collectors.toSet());
            Node node = new Node(i++, selectors, declarations);
            nodes.add(node);
            mapping.put(concept, node);
        }

        concepts.forEach(concept -> {
            Node node = mapping.get(concept);
            concept.getParents().forEach(parent -> node.getParents().add(mapping.get(parent)));
            concept.getChildren().forEach(child -> node.getChildren().add(mapping.get(child)));
        });
        return new Lattice(nodes);
    }

    private static void removedUnusedProperties(Lattice lattice) {
        lattice.getNodes().forEach(node -> {
            Set<Declaration> abstractDeclarations = node.getDeclarations().stream()
                    .filter(declaration -> declaration instanceof DeclarationAbstract)
                    .collect(Collectors.toSet());
            Set<Declaration> concreteDeclarations = node.getDeclarations().stream()
                    .filter(declaration -> declaration instanceof DeclarationConcrete)
                    .collect(Collectors.toSet());
            Set<Property> instantiatedProperties = concreteDeclarations.stream()
                    .map(Declaration::getPropertyReference)
                    .collect(Collectors.toSet());

            Set<Declaration> newDeclarations = Sets.newHashSet(concreteDeclarations);
            abstractDeclarations.forEach(abstractDeclaration -> {
                Property property = abstractDeclaration.getPropertyReference();
                if (!instantiatedProperties.contains(property)) {
                    newDeclarations.add(abstractDeclaration);
                }
            });
            node.getDeclarations().clear();
            node.getDeclarations().addAll(newDeclarations);
        });
    }

    private static void filterNodes(Lattice lattice) {
        Consumer<Node> handleNode = node -> {
            Set<Node> parents = node.getParents();
            Set<Node> children = node.getChildren();
            if (node.getSelectors().isEmpty()) {
                parents.forEach(parent -> {
                    boolean removed = parent.getChildren().remove(node);
                    assert removed;
                    parent.getChildren().addAll(children);
                });
                children.forEach(child -> {
                    boolean removed = child.getParents().remove(node);
                    assert removed;
                    child.getParents().addAll(parents);
                });
                boolean removed = lattice.getNodes().remove(node);
                assert removed;
            } else {
                parents.forEach(parent -> parent.getChildren().addAll(children));
                children.forEach(child -> {
                    boolean removed = child.getParents().remove(node);
                    assert removed;
                    child.getParents().addAll(parents);
                });
                node.getChildren().clear();
            }
        };

        lattice.topologicalOrder().forEach(node -> {
            int childrenNb = node.getChildren().size();
            Set<Declaration> declarations = node.getSimplifiedDeclarations();
            long concreteDeclarationsNb = declarations.stream()
                    .filter(declaration -> declaration instanceof DeclarationConcrete)
                    .map(declaration -> (DeclarationConcrete) declaration)
                    .count();
            long abstractDeclarationsNb = declarations.stream()
                    .filter(declaration -> declaration instanceof DeclarationAbstract)
                    .map(declaration -> (DeclarationAbstract) declaration)
                    .count();

            if ((childrenNb > 0 && childrenNb < Config.getInstance().childrenMinNb())
                    || concreteDeclarationsNb < Config.getInstance().concreteDeclarationsMinNb()
                    || abstractDeclarationsNb > Config.getInstance().abstractDeclarationsMaxNb()) {
                handleNode.accept(node);
            }
        });
    }

    private static void spanningArborescence(Lattice lattice) {
        Function<Set<Node>, Node> bestParent = parents -> {
            Node selectedParent = null;
            int inheritedDeclarationsMaxNb = -1;
            for (Node parent : parents) {
                int inheritedDeclarationsNb = parent.getAllParents().stream()
                        .mapToInt(concept -> concept.getSimplifiedDeclarations().size())
                        .sum();
                if (inheritedDeclarationsNb > inheritedDeclarationsMaxNb) {
                    selectedParent = parent;
                    inheritedDeclarationsMaxNb = inheritedDeclarationsNb;
                }
            }
            return selectedParent;
        };

        lattice.getRoots().forEach(root -> {
            Set<Node> greatChildren = root.getAllChildren();
            greatChildren.removeAll(root.getChildren());
            greatChildren.remove(root);
            greatChildren.stream()
                    .filter(node -> node.getParents().size() >= 2)
                    .forEach(node -> {
                        Node selectedParent = bestParent.apply(node.getParents());

                        Set<Node> otherParents = Sets.newHashSet(node.getParents());
                        otherParents.remove(selectedParent);
                        otherParents.stream()
                                .filter(otherParent -> otherParent.getAllParents().contains(root))
                                .forEach(otherParent -> {
                                    otherParent.getChildren().remove(node);
                                    node.getParents().remove(otherParent);
                                });
                    });
        });

        // Remove unused nodes
        List<Node> orderedNodes = lattice.topologicalOrder();
        Collections.reverse(orderedNodes);
        List<Node> oldNodes = Lists.newArrayList();
        orderedNodes.forEach(node -> {
            if (node.getChildren().isEmpty() && node.getSelectors().isEmpty()) {
                node.getParents().forEach(parent -> parent.getChildren().remove(node));
                oldNodes.add(node);
            }
        });
        lattice.getNodes().removeAll(oldNodes);
    }

    private static List<SslStatement> generateStatements(Lattice lattice) {
        List<SslStatement> statements = Lists.newArrayList();
        Map<Node, SslMixin> mappingNodeMixin = Maps.newHashMap();

        lattice.topologicalOrder().forEach(node -> {
            Set<Property> properties = node.getSimplifiedDeclarations().stream()
                    .filter(declaration -> declaration instanceof DeclarationAbstract)
                    .map(declaration -> declaration.getPropertyReference())
                    .collect(Collectors.toSet());
            Set<Declaration> declarations = node.getSimplifiedDeclarations().stream()
                    .filter(declaration -> declaration instanceof DeclarationConcrete)
                    .collect(Collectors.toSet());
            Map<Property, Declaration> declarationsData = node.getDeclarations().stream()
                    .filter(declaration -> declaration instanceof DeclarationConcrete)
                    .collect(Collectors.toMap(Declaration::getPropertyReference, declaration -> declaration));

            List<SslMixinCall> mixinCalls = Lists.newArrayList();
            List<Property> parameters = Lists.newLinkedList(properties);

            node.getParents().forEach(currentParent -> {
                SslMixin mixinParent = mappingNodeMixin.get(currentParent);
                assert mixinParent != null;
                List<Declaration> mixinCallParameters = Lists.newArrayList();
                mixinParent.getParameters().forEach(parameter -> {
                    if (declarationsData.containsKey(parameter)) {
                        mixinCallParameters.add(declarationsData.get(parameter));
                        declarations.remove(declarationsData.get(parameter));
                    } else {
                        mixinCallParameters.add(new DeclarationAbstract(parameter, new ValueAbstract(parameter.getProperty())));
                        if (!parameters.contains(parameter)) {
                            parameters.add(parameter);
                        }
                    }
                });
                mixinCalls.add(new SslMixinCall(mixinParent, mixinCallParameters));
            });

            declarations.addAll(properties.stream()
                    .map(property -> new DeclarationAbstract(property, new ValueAbstract(property.getProperty())))
                    .collect(Collectors.toSet()));
            Set<Selector> selectors = node.getSelectors();

            if (node.getChildren().isEmpty()) {
                assert parameters.isEmpty();
                SslRuleset newRuleset = new SslRuleset(selectors, declarations, mixinCalls);
                statements.add(newRuleset);
            } else {
                SslMixin newMixin = new SslMixin(mixinName("m"), parameters, declarations, mixinCalls);
                statements.add(newMixin);
                mappingNodeMixin.put(node, newMixin);
                if (!selectors.isEmpty()) {
                    SslRuleset newRuleset = new SslRuleset(selectors, new SslMixinCall(newMixin));
                    statements.add(newRuleset);
                }
            }
        });
        return statements;
    }

    private static void splitSelectors(List<SslMixin> generatedMixins, List<SslRuleset> generatedRulesets) {
        BiConsumer<SslRuleset, List<SslRuleset>> splitWithoutMixin = (ruleset, rulesets) -> {
            ruleset.getSelectors().forEach(selector -> {
                rulesets.add(new SslRuleset(selector, ruleset.getDeclarations(), ruleset.getMixinCalls()));
            });
        };
        Function<SslRuleset, Function<List<SslRuleset>, Consumer<List<SslMixin>>>> splitWithMixin =
                ruleset -> rulesets -> mixins -> {
                    SslMixin newMixin = new SslMixin(mixinName("s"), ruleset.getDeclarations(), ruleset.getMixinCalls());
                    mixins.add(newMixin);
                    ruleset.getSelectors().forEach(selector -> {
                        rulesets.add(new SslRuleset(selector, new SslMixinCall(newMixin)));
                    });
                };

        List<SslRuleset> oldRulesets = Lists.newArrayList();
        List<SslRuleset> newRulesets = Lists.newArrayList();
        List<SslMixin> newMixins = Lists.newArrayList();

        generatedRulesets.stream()
                .filter(ruleset -> ruleset.getSelectors().size() > 1)
                .forEach(ruleset -> {
                    oldRulesets.add(ruleset);
                    Set<Declaration> declarations = ruleset.getDeclarations();
                    long concreteDeclarationsNb = declarations.stream()
                            .filter(declaration -> declaration instanceof DeclarationConcrete)
                            .map(declaration -> (DeclarationConcrete) declaration)
                            .count();
                    long abstractDeclarationsNb = declarations.stream()
                            .filter(declaration -> declaration instanceof DeclarationAbstract)
                            .map(declaration -> (DeclarationAbstract) declaration)
                            .count();

                    if (declarations.isEmpty() && ruleset.getMixinCalls().size() == 1) {
                        splitWithoutMixin.accept(ruleset, newRulesets);
                    } else if (concreteDeclarationsNb < Config.getInstance().concreteDeclarationsMinNb()
                            || abstractDeclarationsNb > Config.getInstance().abstractDeclarationsMaxNb()) {
                        splitWithoutMixin.accept(ruleset, newRulesets);
                    } else {
                        splitWithMixin.apply(ruleset).apply(newRulesets).accept(newMixins);
                    }
                });
        generatedRulesets.removeAll(oldRulesets);
        generatedRulesets.addAll(newRulesets);
        generatedMixins.addAll(newMixins);
    }

    private static void mergeRulesetsWithSamePosition(List<SslRuleset> generatedRulesets) {
        Map<Integer, SslRuleset> mappingPositionRulesets = Maps.newHashMap();
        List<SslRuleset> oldRulesets = Lists.newArrayList();
        generatedRulesets.forEach(ruleset -> {
            int position = ruleset.getUniqueSelector().getPosition().getLineNumber();
            if (mappingPositionRulesets.containsKey(position)) {
                mappingPositionRulesets.get(position).getSelectors().add(ruleset.getUniqueSelector());
                oldRulesets.add(ruleset);
            } else {
                mappingPositionRulesets.put(position, ruleset);
            }
        });
        generatedRulesets.removeAll(oldRulesets);
    }

    private static void orderGeneratedRulesets(List<SslRuleset> generatedRulesets) {
        Collections.sort(generatedRulesets,
                (SslRuleset r1, SslRuleset r2) -> {
                    int firstRulesetPosition = r1.getOneSelector().getPosition().getLineNumber();
                    int secondRulesetPosition = r2.getOneSelector().getPosition().getLineNumber();
                    if (firstRulesetPosition < secondRulesetPosition) return -1;
                    else if (firstRulesetPosition == secondRulesetPosition) return 0;
                    else return 1;
                });
    }

    private static String mixinName(String prefix) {
        mixinsNb++;
        return prefix + mixinsNb;
    }

}
