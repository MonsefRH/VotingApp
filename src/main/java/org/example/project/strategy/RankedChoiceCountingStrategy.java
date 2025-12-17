package org.example.project.strategy;

import org.example.project.model.Vote;

import java.util.*;

public class RankedChoiceCountingStrategy implements CountingStrategy {

    @Override
    public Map<String, Integer> count(List<Vote> votes) {
        Map<String, Integer> results = new HashMap<>();

        List<Vote> sortedVotes = new ArrayList<>(votes);
        sortedVotes.sort(Comparator.comparingLong(Vote::getTimestamp));

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
