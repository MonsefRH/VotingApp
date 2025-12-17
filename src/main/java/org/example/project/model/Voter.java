package org.example.project.model;

public class Voter {
    private final String id;
    private final String name;
    private final long timestamp;

    public Voter(String id, String name) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Voter ID cannot be null or blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Voter name cannot be null or blank");
        }
        this.id = id;
        this.name = name;
        this.timestamp = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return name + "(" + id + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Voter)) return false;
        Voter other = (Voter) obj;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
