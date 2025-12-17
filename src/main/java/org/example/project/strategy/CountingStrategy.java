package org.example.project.strategy;


import org.example.project.model.Vote;

import java.util.List;
import java.util.Map;

public interface CountingStrategy {

    Map<String, Integer> count(List<Vote> votes);

    String getName();
}
