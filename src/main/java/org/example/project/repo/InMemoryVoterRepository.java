package org.example.project.repo;

import org.example.project.model.Voter;

import java.util.*;

public class InMemoryVoterRepository implements VoterRepository {

    private final Map<String, Voter> voterStore =
            Collections.synchronizedMap(new LinkedHashMap<>());

    @Override
    public void register(Voter voter) {
        if (voter == null) {
            throw new IllegalArgumentException("Voter cannot be null");
        }
        voterStore.put(voter.getId(), voter);
    }

    @Override
    public List<Voter> findAll() {
        return new ArrayList<>(voterStore.values());
    }

    @Override
    public Voter findById(String id) {
        if (id == null) {
            return null;
        }
        return voterStore.get(id);
    }

    @Override
    public boolean isRegistered(String id) {
        if (id == null) {
            return false;
        }
        return voterStore.containsKey(id);
    }

    @Override
    public void clear() {
        voterStore.clear();
    }
}