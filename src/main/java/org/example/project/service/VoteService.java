package org.example.project.service;


import org.example.project.model.*;
import org.example.project.repo.*;
import org.example.project.strategy.CountingStrategy;
import org.example.project.observer.VoteListener;

import java.util.*;

public class VoteService {

    private final VoteRepository voteRepository;
    private final CandidateRepository candidateRepository;
    private final VoterRepository voterRepository;
    private final List<VoteListener> listeners = Collections.synchronizedList(new ArrayList<>());

    public VoteService(VoteRepository voteRepository,
                       CandidateRepository candidateRepository,
                       VoterRepository voterRepository) {
        if (voteRepository == null) {
            throw new IllegalArgumentException("VoteRepository cannot be null");
        }
        if (candidateRepository == null) {
            throw new IllegalArgumentException("CandidateRepository cannot be null");
        }
        if (voterRepository == null) {
            throw new IllegalArgumentException("VoterRepository cannot be null");
        }

        this.voteRepository = voteRepository;
        this.candidateRepository = candidateRepository;
        this.voterRepository = voterRepository;
    }


    public void addCandidate(String id, String name) {
        if (id == null || id.isBlank() || name == null || name.isBlank()) {
            throw new IllegalArgumentException("ID and name cannot be null or blank");
        }

        Candidate candidate = new Candidate(id, name);
        candidateRepository.add(candidate);
        System.out.printf("✓ Candidate added: %s%n", name);
    }


    public List<Candidate> getCandidates() {
        return candidateRepository.findAll();
    }

    public void registerVoter(String id, String name) {
        if (id == null || id.isBlank() || name == null || name.isBlank()) {
            throw new IllegalArgumentException("ID and name cannot be null or blank");
        }

        Voter voter = new Voter(id, name);
        voterRepository.register(voter);
    }

    public List<Voter> getVoters() {
        return voterRepository.findAll();
    }

    public boolean hasVoted(String voterId) {
        return voteRepository.hasVoted(voterId);
    }

    public void castVote(String voterId, String candidateId) {
        if (voterId == null || voterId.isBlank()) {
            throw new IllegalArgumentException("Voter ID cannot be null or blank");
        }
        if (candidateId == null || candidateId.isBlank()) {
            throw new IllegalArgumentException("Candidate ID cannot be null or blank");
        }

        Voter voter = voterRepository.findById(voterId);
        if (voter == null) {
            throw new IllegalArgumentException("Voter not registered: " + voterId);
        }

        Candidate candidate = candidateRepository.findById(candidateId);
        if (candidate == null) {
            throw new IllegalArgumentException("Candidate does not exist: " + candidateId);
        }

        if (hasVoted(voterId)) {
            throw new IllegalStateException("Voter " + voter.getName() + " has already voted!");
        }

        // Créer et sauvegarder le vote
        Vote vote = new Vote(voterId, voter.getName(), candidateId, candidate.getName());
        voteRepository.save(vote);

        // Notifier les observateurs (Observer pattern)
        notifyListeners(vote);
    }

    public Map<String, Integer> countVotes(CountingStrategy strategy) {
        if (strategy == null) {
            throw new IllegalArgumentException("Counting strategy cannot be null");
        }
        return strategy.count(voteRepository.findAll());
    }

    public Candidate getWinner(CountingStrategy strategy) {
        Map<String, Integer> results = countVotes(strategy);

        if (results.isEmpty()) {
            return null;  // Aucun vote
        }

        String winnerId = results.entrySet().stream()
                .max(Comparator.comparingInt(Map.Entry::getValue))
                .map(Map.Entry::getKey)
                .orElse(null);

        return candidateRepository.findById(winnerId);
    }

    public int getTotalVoteCount() {
        return voteRepository.count();
    }


    public List<Vote> getAllVotes() {
        return voteRepository.findAll();
    }


    public void addListener(VoteListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }


    public void removeListener(VoteListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners(Vote vote) {
        for (VoteListener listener : new ArrayList<>(listeners)) {
            try {
                listener.onVote(vote);
            } catch (Exception e) {
                System.err.printf("Error notifying listener: %s%n", e.getMessage());
            }
        }
    }

    public void reset() {
        voteRepository.clear();
        candidateRepository.clear();
        voterRepository.clear();
        System.out.println("[SYSTEM] All data cleared");
    }
}
