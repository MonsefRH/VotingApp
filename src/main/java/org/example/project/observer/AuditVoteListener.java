package org.example.project.observer;


import org.example.project.model.Vote;

import java.util.*;

public class AuditVoteListener implements VoteListener {

    private final List<Vote> auditLog = Collections.synchronizedList(new ArrayList<>());
    private final Map<String, Integer> voterVoteCount =
            Collections.synchronizedMap(new HashMap<>());

    @Override
    public void onVote(Vote vote) {
        auditLog.add(vote);

        // Compter les votes par électeur
        voterVoteCount.merge(vote.getVoterId(), 1, Integer::sum);

        // Alerter si un électeur vote plusieurs fois (fraude potentielle)
        if (voterVoteCount.get(vote.getVoterId()) > 1) {
            System.out.printf("[AUDIT] ⚠️  FRAUD ATTEMPT: %s tried to vote %d times%n",
                    vote.getVoterName(), voterVoteCount.get(vote.getVoterId()));
        } else {
            System.out.printf("[AUDIT] 📝 Vote #%d recorded%n", auditLog.size());
        }
    }

    public List<Vote> getAuditLog() {
        return new ArrayList<>(auditLog);
    }

    public int getVoteCount() {
        return auditLog.size();
    }

    public Map<String, Integer> getVoterVoteCount() {
        return new HashMap<>(voterVoteCount);
    }

    public void clearAudit() {
        auditLog.clear();
        voterVoteCount.clear();
    }
}
