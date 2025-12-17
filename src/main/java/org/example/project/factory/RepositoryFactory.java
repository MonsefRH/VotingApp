package org.example.project.factory;


import org.example.project.repo.*;

/**
 * Factory Pattern - Crée des instances de repositories.
 *
 * PROBLÈME RÉSOLU :
 * Avant : Hardcoding des collections globales dans SpaghettiVotingApp
 * Après  : Factory crée les repositories de manière flexible
 *
 * Justification pédagogique :
 * - Découple la création de l'implémentation concrète
 * - Permet d'ajouter facilement "file", "jdbc" sans modifier le code client
 * - Centralise la logique d'instanciation
 * - Facilite les tests (injection de mock)
 *
 * Avantages SOLID :
 * ✓ Open/Closed Principle (ouvert à extension)
 * ✓ Dependency Inversion (dépend d'interfaces, pas implémentations)
 */
public class RepositoryFactory {

    /**
     * Crée une instance de VoteRepository selon le type spécifié.
     *
     * @param type le type de repository ("memory", "file", "jdbc")
     * @return une instance de VoteRepository
     * @throws IllegalArgumentException si le type est inconnu
     */
    public static VoteRepository createVoteRepository(String type) {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Repository type cannot be null or blank");
        }

        return switch (type.toLowerCase()) {
            case "memory" -> new InMemoryVoteRepository();
            // Futures implémentations :
            // case "file" -> new FileVoteRepository();
            // case "jdbc" -> new JdbcVoteRepository();
            default -> throw new IllegalArgumentException("Unknown vote repository type: " + type);
        };
    }

    /**
     * Crée une instance de CandidateRepository.
     */
    public static CandidateRepository createCandidateRepository(String type) {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Repository type cannot be null or blank");
        }

        return switch (type.toLowerCase()) {
            case "memory" -> new InMemoryCandidateRepository();
            default -> throw new IllegalArgumentException("Unknown candidate repository type: " + type);
        };
    }

    /**
     * Crée une instance de VoterRepository.
     */
    public static VoterRepository createVoterRepository(String type) {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Repository type cannot be null or blank");
        }

        return switch (type.toLowerCase()) {
            case "memory" -> new InMemoryVoterRepository();
            default -> throw new IllegalArgumentException("Unknown voter repository type: " + type);
        };
    }
}
