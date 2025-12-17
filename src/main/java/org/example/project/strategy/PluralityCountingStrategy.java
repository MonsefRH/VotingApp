package org.example.project.strategy;


import org.example.project.model.Vote;

import java.util.*;

/**
 * Stratégie Plurality (majorité simple).
 *
 * Algorithme : Le candidat avec le plus de votes gagne.
 * Chaque vote compte pour 1 point.
 *
 * Avantage : Simple et direct (comme dans l'app original)
 * Inconvénient : Peut marginaliser les candidats minoritaires
 *
 * Exemple :
 * - Alice: 3 votes
 * - Bob: 2 votes
 * - Charlie: 1 vote
 * => Gagnant : Alice (3 votes)
 */
public class PluralityCountingStrategy implements CountingStrategy {

    @Override
    public Map<String, Integer> count(List<Vote> votes) {
        Map<String, Integer> results = new HashMap<>();

        for (Vote vote : votes) {
            results.merge(vote.getCandidateId(), 1, Integer::sum);
        }

        return results;
    }

    @Override
    public String getName() {
        return "Plurality (Simple Majority)";
    }
}
