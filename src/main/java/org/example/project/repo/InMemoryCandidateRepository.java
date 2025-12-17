package org.example.project.repo;

import org.example.project.model.Candidate;

import java.util.*;

public class InMemoryCandidateRepository implements CandidateRepository {

    private final Map<String, Candidate> candidateStore =
            Collections.synchronizedMap(new LinkedHashMap<>());

    @Override
    public void add(Candidate candidate) {
        if (candidate == null) {
            throw new IllegalArgumentException("Candidate cannot be null");
        }
        candidateStore.put(candidate.getId(), candidate);
    }

    @Override
    public List<Candidate> findAll() {
        return new ArrayList<>(candidateStore.values());
    }

    @Override
    public Candidate findById(String id) {
        if (id == null) {
            return null;
        }
        return candidateStore.get(id);
    }

    @Override
    public boolean exists(String id) {
        if (id == null) {
            return false;
        }
        return candidateStore.containsKey(id);
    }

    @Override
    public void clear() {
        candidateStore.clear();
    }
}
