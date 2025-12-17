package service;


import org.example.project.factory.RepositoryFactory;
import org.example.project.model.Vote;
import org.example.project.observer.*;
import org.example.project.repo.*;
import org.example.project.service.VoteService;
import org.example.project.strategy.*;
import org.junit.jupiter.api.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests spécifiques aux 3 patterns utilisés.
 */
class PatternTests {

    @Nested
    @DisplayName("Factory Pattern Tests")
    class FactoryPatternTests {

        @Test
        void testFactoryCreatesVoteRepository() {
            VoteRepository repo = RepositoryFactory.createVoteRepository("memory");

            assertNotNull(repo);
            assertInstanceOf(InMemoryVoteRepository.class, repo);
        }

        @Test
        void testFactoryCreatesCandidateRepository() {
            CandidateRepository repo = RepositoryFactory.createCandidateRepository("memory");

            assertNotNull(repo);
            assertInstanceOf(InMemoryCandidateRepository.class, repo);
        }

        @Test
        void testFactoryCreatesVoterRepository() {
            VoterRepository repo = RepositoryFactory.createVoterRepository("memory");

            assertNotNull(repo);
            assertInstanceOf(InMemoryVoterRepository.class, repo);
        }

        @Test
        void testFactoryWithUnknownTypeThrowsException() {
            assertThrows(IllegalArgumentException.class,
                    () -> RepositoryFactory.createVoteRepository("unknown"));
        }

        @Test
        void testFactoryCaseInsensitive() {
            VoteRepository repo1 = RepositoryFactory.createVoteRepository("MEMORY");
            VoteRepository repo2 = RepositoryFactory.createVoteRepository("Memory");

            assertNotNull(repo1);
            assertNotNull(repo2);
        }
    }

    // ╔════════════════════════════════════════════════════════╗
    // ║   STRATEGY PATTERN - Tests                             ║
    // ╚════════════════════════════════════════════════════════╝

    @Nested
    @DisplayName("Strategy Pattern Tests")
    class StrategyPatternTests {

        private VoteService service;

        @BeforeEach
        void setUp() {
            service = new VoteService(
                    new InMemoryVoteRepository(),
                    new InMemoryCandidateRepository(),
                    new InMemoryVoterRepository()
            );

            service.addCandidate("C1", "Alice");
            service.addCandidate("C2", "Bob");
            service.addCandidate("C3", "Charlie");
        }

        @Test
        void testPluralityStrategy() {
            // Arrange
            service.registerVoter("V1", "Voter1");
            service.registerVoter("V2", "Voter2");
            service.registerVoter("V3", "Voter3");

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
        void testRankedChoiceStrategy() {
            // Arrange
            service.registerVoter("V1", "Voter1");
            service.registerVoter("V2", "Voter2");
            service.registerVoter("V3", "Voter3");

            service.castVote("V1", "C1");
            service.castVote("V2", "C1");
            service.castVote("V3", "C2");

            // Act
            Map<String, Integer> results = service.countVotes(new RankedChoiceCountingStrategy());

            // Assert
            // C1: 1 (première) + 2 (deuxième) = 3
            // C2: 3 (troisième) = 3
            assertEquals(3, results.get("C1"));
            assertEquals(3, results.get("C2"));
        }

        @Test
        void testStrategySwapping() {
            // Arrange - Ajouter plusieurs votes
            for (int i = 1; i <= 10; i++) {
                service.registerVoter("V" + i, "Voter" + i);
                String candidateId = i <= 5 ? "C1" : (i <= 8 ? "C2" : "C3");
                service.castVote("V" + i, candidateId);
            }

            CountingStrategy plurality = new PluralityCountingStrategy();
            CountingStrategy ranked = new RankedChoiceCountingStrategy();

            // Act & Assert - Résultats différents selon la stratégie
            Map<String, Integer> pluralityResults = service.countVotes(plurality);
            Map<String, Integer> rankedResults = service.countVotes(ranked);

            assertNotEquals(pluralityResults.get("C1"), rankedResults.get("C1"));
        }

        @Test
        void testStrategyGetName() {
            CountingStrategy plurality = new PluralityCountingStrategy();
            CountingStrategy ranked = new RankedChoiceCountingStrategy();

            assertNotNull(plurality.getName());
            assertNotNull(ranked.getName());
            assertNotEquals(plurality.getName(), ranked.getName());
        }
    }

    // ╔════════════════════════════════════════════════════════╗
    // ║   OBSERVER PATTERN - Tests                             ║
    // ╚════════════════════════════════════════════════════════╝

    @Nested
    @DisplayName("Observer Pattern Tests")
    class ObserverPatternTests {

        private VoteService service;

        @BeforeEach
        void setUp() {
            service = new VoteService(
                    new InMemoryVoteRepository(),
                    new InMemoryCandidateRepository(),
                    new InMemoryVoterRepository()
            );

            service.addCandidate("C1", "Alice");
            service.addCandidate("C2", "Bob");
        }

        @Test
        void testAuditListenerObserves() {
            // Arrange
            AuditVoteListener auditListener = new AuditVoteListener();
            service.addListener(auditListener);

            // Act
            service.registerVoter("V1", "John");
            service.registerVoter("V2", "Jane");
            service.castVote("V1", "C1");
            service.castVote("V2", "C1");

            // Assert
            assertEquals(2, auditListener.getVoteCount());
            assertEquals(2, auditListener.getAuditLog().size());
        }

        @Test
        void testLoggingListenerObserves() {
            LoggingVoteListener loggingListener = new LoggingVoteListener();
            service.addListener(loggingListener);

            service.registerVoter("V1", "John");

            assertDoesNotThrow(() -> {
                service.castVote("V1", "C1");
            });
        }

        @Test
        void testMultipleObserversNotified() {
            // Arrange
            AuditVoteListener audit1 = new AuditVoteListener();
            AuditVoteListener audit2 = new AuditVoteListener();
            service.addListener(audit1);
            service.addListener(audit2);

            // Act
            service.registerVoter("V1", "John");
            service.castVote("V1", "C1");

            // Assert
            assertEquals(1, audit1.getVoteCount());
            assertEquals(1, audit2.getVoteCount());
        }

        @Test
        void testObserverCanBeRemoved() {
            // Arrange
            AuditVoteListener auditListener = new AuditVoteListener();
            service.addListener(auditListener);

            service.registerVoter("V1", "John");
            service.castVote("V1", "C1");
            assertEquals(1, auditListener.getVoteCount());

            // Act
            service.removeListener(auditListener);

            service.registerVoter("V2", "Jane");
            service.castVote("V2", "C2");

            // Assert - Le listener ne reçoit pas la deuxième notification
            assertEquals(1, auditListener.getVoteCount());
        }

        @Test
        void testObserverDetectsDuplicateVotes() {
            // Arrange
            AuditVoteListener auditListener = new AuditVoteListener();
            service.addListener(auditListener);

            service.registerVoter("V1", "John");
            service.castVote("V1", "C1");

            // Act & Assert - Tentative de vote en double
            assertThrows(IllegalStateException.class,
                    () -> service.castVote("V1", "C2"));
        }
    }
}