package org.example.project.strategy;

import org.example.project.model.Vote;

import java.util.*;

/**
 * Stratégie Ranked Choice (vote alternatif).
 *
 * Algorithme simplifié : Les votes sont pondérés par leur ordre temporel.
 * Plus un vote est ancien, moins il a de poids.
 *
 * Formule : poids = position_dans_l'ordre (1, 2, 3, ...)
 *
 * Avantage : Tient compte de la chronologie des votes
 * Inconvénient : Plus complexe à comprendre
 *
 * Exemple :
 * - Alice (vote 1): 1 point + Alice (vote 3): 3 points = 4 points
 * - Bob (vote 2): 2 points = 2 points
 * => Gagnant : Alice (4 points)
 *
 * Note : Une implémentation complète de ranked choice inclurait
 * des préférences multiples par voter, ce qui n'est pas le cas ici.
 */
public class RankedChoiceCountingStrategy implements CountingStrategy {

    @Override
    public Map<String, Integer> count(List<Vote> votes) {
        Map<String, Integer> results = new HashMap<>();

        // Trier par timestamp pour respecter l'ordre chronologique
        List<Vote> sortedVotes = new ArrayList<>(votes);
        sortedVotes.sort(Comparator.comparingLong(Vote::getTimestamp));

        // Pondération : position dans l'ordre
        for (int i = 0; i < sortedVotes.size(); i++) {
            Vote vote = sortedVotes.get(i);
            int weight = i + 1;  // Poids commence à 1
            results.merge(vote.getCandidateId(), weight, Integer::sum);
        }

        return results;
    }

    @Override
    public String getName() {
        return "Ranked Choice (Weighted by Order)";
    }
}
