package org.example.project.repo;

import org.example.project.model.Vote;

import java.util.*;

public class InMemoryVoteRepository implements VoteRepository {

    private final List<Vote> voteStore = Collections.synchronizedList(new ArrayList<>());
    private final Set<String> voterIds = Collections.synchronizedSet(new HashSet<>());

    @Override
    public void save(Vote vote) {
        if (vote == null) {
            throw new IllegalArgumentException("Vote cannot be null");
        }
        voteStore.add(vote);
        voterIds.add(vote.getVoterId());
    }

    @Override
    public List<Vote> findAll() {
        return new ArrayList<>(voteStore);
    }

    @Override
    public int count() {
        return voteStore.size();
    }

    @Override
    public void clear() {
        voteStore.clear();
        voterIds.clear();
    }

    @Override
    public boolean hasVoted(String voterId) {
        if (voterId == null) {
            return false;
        }
        return voterIds.contains(voterId);
    }
}
