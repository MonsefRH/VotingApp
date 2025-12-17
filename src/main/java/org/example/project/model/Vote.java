package org.example.project.model;

public class Vote {
    private final String voterId;
    private final String voterName;
    private final String candidateId;
    private final String candidateName;
    private final long timestamp;

    public Vote(String voterId, String voterName, String candidateId, String candidateName) {
        if (voterId == null || voterId.isBlank()) {
            throw new IllegalArgumentException("Voter ID cannot be null or blank");
        }
        if (voterName == null || voterName.isBlank()) {
            throw new IllegalArgumentException("Voter name cannot be null or blank");
        }
        if (candidateId == null || candidateId.isBlank()) {
            throw new IllegalArgumentException("Candidate ID cannot be null or blank");
        }
        if (candidateName == null || candidateName.isBlank()) {
            throw new IllegalArgumentException("Candidate name cannot be null or blank");
        }

        this.voterId = voterId;
        this.voterName = voterName;
        this.candidateId = candidateId;
        this.candidateName = candidateName;
        this.timestamp = System.currentTimeMillis();
    }

    public String getVoterId() {
        return voterId;
    }

    public String getVoterName() {
        return voterName;
    }

    public String getCandidateId() {
        return candidateId;
    }

    public String getCandidateName() {
        return candidateName;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("Vote{voter=%s, candidate=%s, time=%d}",
                voterName, candidateName, timestamp);
    }
}
