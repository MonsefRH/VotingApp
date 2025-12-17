package pattern;


import org.example.project.model.*;
import org.example.project.observer.*;
import org.example.project.repo.*;
import org.example.project.service.VoteService;
import org.example.project.strategy.*;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests unitaires pour VoteService.
 * Couvre : Factory, Strategy, Observer patterns.
 */
class VoteServiceTest {

    private VoteService service;
    private InMemoryVoteRepository voteRepository;
    private InMemoryCandidateRepository candidateRepository;
    private InMemoryVoterRepository voterRepository;

    @BeforeEach
    void setUp() {
        voteRepository = new InMemoryVoteRepository();
        candidateRepository = new InMemoryCandidateRepository();
        voterRepository = new InMemoryVoterRepository();
        service = new VoteService(voteRepository, candidateRepository, voterRepository);

        // Ajouter les candidats de test
        service.addCandidate("C1", "Alice");
        service.addCandidate("C2", "Bob");
        service.addCandidate("C3", "Charlie");
    }

    // ========== TESTS CANDIDATS ==========

    @Test
    void testAddCandidate() {
        service.addCandidate("C4", "Diana");

        assertEquals(4, service.getCandidates().size());
        assertTrue(candidateRepository.exists("C4"));
    }

    @Test
    void testAddCandidateWithNullId() {
        assertThrows(IllegalArgumentException.class,
                () -> service.addCandidate(null, "Diana"));
    }

    @Test
    void testGetCandidates() {
        assertEquals(3, service.getCandidates().size());
    }

    // ========== TESTS ÉLECTEURS ==========

    @Test
    void testRegisterVoter() {
        service.registerVoter("V1", "John");

        assertTrue(voterRepository.isRegistered("V1"));
    }

    @Test
    void testGetVoters() {
        service.registerVoter("V1", "John");
        service.registerVoter("V2", "Jane");

        assertEquals(2, service.getVoters().size());
    }

    // ========== TESTS VOTES ==========

    @Test
    void testCastVote() {
        service.registerVoter("V1", "John");

        service.castVote("V1", "C1");

        assertEquals(1, service.getTotalVoteCount());
        assertTrue(service.hasVoted("V1"));
    }

    @Test
    void testCastVoteWithUnregisteredVoter() {
        assertThrows(IllegalArgumentException.class,
                () -> service.castVote("UNKNOWN", "C1"));
    }

    @Test
    void testCastVoteForNonExistentCandidate() {
        service.registerVoter("V1", "John");

        assertThrows(IllegalArgumentException.class,
                () -> service.castVote("V1", "UNKNOWN"));
    }

    @Test
    void testVoterCannotVoteTwice() {
        service.registerVoter("V1", "John");
        service.castVote("V1", "C1");

        assertThrows(IllegalStateException.class,
                () -> service.castVote("V1", "C2"));
    }

    @Test
    void testHasVoted() {
        service.registerVoter("V1", "John");

        assertFalse(service.hasVoted("V1"));
        service.castVote("V1", "C1");
        assertTrue(service.hasVoted("V1"));
    }

    @Test
    void testCountVotesWithPlurality() {
        // Arrange
        service.registerVoter("V1", "John");
        service.registerVoter("V2", "Jane");
        service.registerVoter("V3", "Bob");

        service.castVote("V1", "C1");
        service.castVote("V2", "C1");
        service.castVote("V3", "C2");

        // Act
        Map<String, Integer> results = service.countVotes(new PluralityCountingStrategy());

        // Assert
        assertEquals(2, results.get("C1"));
        assertEquals(1, results.get("C2"));
    }

    @Test
    void testGetWinner() {
        service.registerVoter("V1", "John");
        service.registerVoter("V2", "Jane");
        service.registerVoter("V3", "Bob");

        service.castVote("V1", "C1");
        service.castVote("V2", "C1");
        service.castVote("V3", "C2");

        Candidate winner = service.getWinner(new PluralityCountingStrategy());

        assertNotNull(winner);
        assertEquals("Alice", winner.getName());
    }

    // ========== TESTS OBSERVATEURS ==========

    @Test
    void testVoteNotification() {
        AuditVoteListener auditListener = new AuditVoteListener();
        service.addListener(auditListener);

        service.registerVoter("V1", "John");
        service.castVote("V1", "C1");

        assertEquals(1, auditListener.getVoteCount());
    }

    @Test
    void testMultipleListeners() {
        AuditVoteListener audit1 = new AuditVoteListener();
        AuditVoteListener audit2 = new AuditVoteListener();
        service.addListener(audit1);
        service.addListener(audit2);

        service.registerVoter("V1", "John");
        service.castVote("V1", "C1");

        assertEquals(1, audit1.getVoteCount());
        assertEquals(1, audit2.getVoteCount());
    }

    @Test
    void testRemoveListener() {
        AuditVoteListener auditListener = new AuditVoteListener();
        service.addListener(auditListener);

        service.registerVoter("V1", "John");
        service.castVote("V1", "C1");
        assertEquals(1, auditListener.getVoteCount());

        service.removeListener(auditListener);

        service.registerVoter("V2", "Jane");
        service.castVote("V2", "C2");

        assertEquals(1, auditListener.getVoteCount());  // Pas incrementé
    }

    // ========== TESTS STRATÉGIES ==========

    @Test
    void testPluralityVsRankedChoice() {
        service.registerVoter("V1", "John");
        service.registerVoter("V2", "Jane");
        service.registerVoter("V3", "Bob");

        service.castVote("V1", "C1");
        service.castVote("V2", "C1");
        service.castVote("V3", "C2");

        Map<String, Integer> plurality = service.countVotes(new PluralityCountingStrategy());
        Map<String, Integer> ranked = service.countVotes(new RankedChoiceCountingStrategy());

        // Résultats peuvent être différents selon la stratégie
        assertNotNull(plurality);
        assertNotNull(ranked);
    }

    // ========== TESTS CYCLE DE VIE ==========

    @Test
    void testReset() {
        service.registerVoter("V1", "John");
        service.castVote("V1", "C1");

        assertEquals(1, service.getTotalVoteCount());

        service.reset();

        assertEquals(0, service.getTotalVoteCount());
        assertEquals(0, service.getVoters().size());
        assertEquals(0, service.getCandidates().size());
    }

    @Test
    void testCompleteWorkflow() {
        // Arrange
        AuditVoteListener auditListener = new AuditVoteListener();
        service.addListener(auditListener);

        // Act
        for (int i = 1; i <= 5; i++) {
            service.registerVoter("V" + i, "Voter" + i);
            service.castVote("V" + i, i <= 3 ? "C1" : "C2");
        }

        // Assert
        assertEquals(5, service.getTotalVoteCount());
        assertEquals(3, service.countVotes(new PluralityCountingStrategy()).get("C1"));
        assertEquals(2, service.countVotes(new PluralityCountingStrategy()).get("C2"));
        assertEquals(5, auditListener.getVoteCount());
    }
    @Test
    void testAddCandidateWithValidData() {
        service.addCandidate("C4", "Diana");
        assertEquals(4, service.getCandidates().size());
    }

    @Test
    void testGetCandidatesNotEmpty() {
        List<Candidate> candidates = service.getCandidates();
        assertFalse(candidates.isEmpty());
        assertEquals(3, candidates.size());
    }

    @Test
    void testRegisterVoterSuccess() {
        service.registerVoter("V99", "TestVoter");
        assertTrue(voterRepository.isRegistered("V99"));
    }

    @Test
    void testAllVotesReturned() {
        service.registerVoter("V1", "Voter1");
        service.castVote("V1", "C1");

        List<Vote> allVotes = service.getAllVotes();
        assertEquals(1, allVotes.size());
    }
}
