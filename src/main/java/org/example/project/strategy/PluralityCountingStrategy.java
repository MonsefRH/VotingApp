package org.example.project.strategy;


import org.example.project.model.Vote;

import java.util.*;

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
