package org.example.project.observer;


import org.example.project.model.Vote;

/**
 * Implémentation d'observateur qui enregistre les votes en console.
 * Remplace l'affichage console en dur de SpaghettiVotingApp.
 */
public class LoggingVoteListener implements VoteListener {

    @Override
    public void onVote(Vote vote) {
        System.out.printf("[LOG] ✓ Vote recorded: %s voted for %s%n",
                vote.getVoterName(), vote.getCandidateName());
    }
}
