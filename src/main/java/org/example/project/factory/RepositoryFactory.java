package org.example.project.factory;


import org.example.project.repo.*;

public class RepositoryFactory {

    public static VoteRepository createVoteRepository(String type) {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Repository type cannot be null or blank");
        }

        return switch (type.toLowerCase()) {
            case "memory" -> new InMemoryVoteRepository();
            default -> throw new IllegalArgumentException("Unknown vote repository type: " + type);
        };
    }


    public static CandidateRepository createCandidateRepository(String type) {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Repository type cannot be null or blank");
        }

        return switch (type.toLowerCase()) {
            case "memory" -> new InMemoryCandidateRepository();
            default -> throw new IllegalArgumentException("Unknown candidate repository type: " + type);
        };
    }


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
