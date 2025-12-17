
import org.example.project.model.Candidate;
import org.example.project.model.Vote;
import org.example.project.model.Voter;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ModelTests {

    @Test
    void candidate_ValidConstruction() {
        Candidate c = new Candidate("C1", "Alice");
        assertEquals("C1", c.getId());
        assertEquals("Alice", c.getName());
        assertEquals("Alice(C1)", c.toString());
    }

    @Test
    void candidate_InvalidId_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Candidate("", "Alice"));
        assertThrows(IllegalArgumentException.class, () -> new Candidate(null, "Alice"));
    }

    @Test
    void candidate_InvalidName_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new Candidate("C1", ""));
        assertThrows(IllegalArgumentException.class, () -> new Candidate("C1", null));
    }

    @Test
    void candidate_EqualsAndHashCode_BasedOnId() {
        Candidate c1 = new Candidate("C1", "Alice");
        Candidate c2 = new Candidate("C1", "Bob");
        Candidate c3 = new Candidate("C2", "Alice");

        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        assertNotEquals(c1, c3);
    }

    // === Vote ===

    @Test
    void vote_ValidConstruction() {
        Vote v = new Vote("V1", "John", "C1", "Alice");
        assertNotNull(v.getTimestamp());
        assertEquals("John", v.getVoterName());
        assertEquals("Alice", v.getCandidateName());
    }

    @Test
    void vote_InvalidFields_ThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Vote("", "John", "C1", "Alice"));
        assertThrows(IllegalArgumentException.class, () -> new Vote("V1", null, "C1", "Alice"));
        assertThrows(IllegalArgumentException.class, () -> new Vote("V1", "John", "", "Alice"));
        assertThrows(IllegalArgumentException.class, () -> new Vote("V1", "John", "C1", null));
    }

    // === Voter ===

    @Test
    void voter_ValidConstruction() {
        Voter v = new Voter("V1", "John");
        assertEquals("John", v.getName());
        assertNotNull(v.getTimestamp());
    }

    @Test
    void voter_InvalidFields_ThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Voter("", "John"));
        assertThrows(IllegalArgumentException.class, () -> new Voter("V1", null));
    }

    @Test
    void voter_EqualsAndHashCode_BasedOnId() {
        Voter v1 = new Voter("V1", "John");
        Voter v2 = new Voter("V1", "Jane");
        assertEquals(v1, v2);
        assertEquals(v1.hashCode(), v2.hashCode());
    }
}