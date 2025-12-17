package org.example.project.model;


public class Candidate {
    private final String id;
    private final String name;

    public Candidate(String id, String name) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Candidate ID cannot be null or blank");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Candidate name cannot be null or blank");
        }
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + "(" + id + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Candidate)) return false;
        Candidate other = (Candidate) obj;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
