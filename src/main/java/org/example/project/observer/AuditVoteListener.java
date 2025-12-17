package org.example.project.observer;


import org.example.project.model.Vote;

import java.util.*;

/**
 * Implémentation d'observateur pour l'audit.
 * Enregistre tous les votes pour traçabilité et détection de fraude.
 *
 * PROBLÈME RÉSOLU :
 * Avant : Pas de vérification des votes en double
 * Après  : Détecte et enregistre les tentatives de vote en double
 */
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

    /**
     * Récupère le journal d'audit.
     */
    public List<Vote> getAuditLog() {
        return new ArrayList<>(auditLog);
    }

    /**
     * Retourne le nombre de votes audités.
     */
    public int getVoteCount() {
        return auditLog.size();
    }

    /**
     * Récupère le nombre de votes par électeur.
     */
    public Map<String, Integer> getVoterVoteCount() {
        return new HashMap<>(voterVoteCount);
    }

    /**
     * Réinitialise le journal d'audit.
     */
    public void clearAudit() {
        auditLog.clear();
        voterVoteCount.clear();
    }
}
