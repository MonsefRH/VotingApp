package org.example.project.strategy;


import org.example.project.model.Vote;

import java.util.List;
import java.util.Map;

/**
 * Strategy Pattern - Interface pour les algorithmes de dépouillement.
 *
 * PROBLÈME RÉSOLU :
 * Avant : Algorithme de comptage en dur dans SpaghettiVotingApp.count()
 * Après  : Stratégies séparées et interchangeables
 *
 * Justification pédagogique :
 * - Permet d'implémenter plusieurs algorithmes de comptage
 * - Découple l'algorithme de la logique métier
 * - Facilite les tests unitaires (tester chaque stratégie isolément)
 * - Respecte le Liskov Substitution Principle
 *
 * Exemples d'implémentations :
 * - Plurality (majorité simple) : "Winner takes all"
 * - Ranked Choice (vote alternatif) : "Instant runoff voting"
 * - Borda Count (vote pondéré) : "Points based voting"
 */
public interface CountingStrategy {

    /**
     * Compte les votes selon la stratégie spécifique.
     *
     * @param votes la liste des votes à compter
     * @return une map [candidateId -> nombre de votes]
     */
    Map<String, Integer> count(List<Vote> votes);

    /**
     * Retourne le nom lisible de la stratégie.
     */
    String getName();
}
