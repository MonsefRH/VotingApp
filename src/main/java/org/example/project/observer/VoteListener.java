package org.example.project.observer;


import org.example.project.model.Vote;

/**
 * Observer Pattern - Interface pour les observateurs de votes.
 *
 * PROBLÈME RÉSOLU :
 * Avant : Affichage console en dur dans SpaghettiVotingApp
 * Après  : Listeners découplés notifiés automatiquement
 *
 * Justification pédagogique :
 * - Découple la logique de vote de la notification
 * - Permet d'ajouter plusieurs observateurs sans modifier le service
 * - Respecte le Single Responsibility Principle
 * - Facilite les tests (mock de listeners)
 *
 * Implémentations :
 * - LoggingVoteListener (logs console)
 * - AuditVoteListener (audit trail, détecte fraude)
 * - DuplicateVoteListener (prévient votes en double)
 */
public interface VoteListener {

    /**
     * Appelé quand un vote est enregistré.
     *
     * @param vote le vote qui vient d'être enregistré
     */
    void onVote(Vote vote);
}
