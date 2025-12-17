package org.example.project;

import com.github.stefanbirkner.systemlambda.SystemLambda;
import org.example.project.factory.RepositoryFactory;
import org.example.project.service.VoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class VotingAppTest {

    private VoteService service;

    @BeforeEach
    void setUp() {
        var voteRepo = RepositoryFactory.createVoteRepository("memory");
        var candidateRepo = RepositoryFactory.createCandidateRepository("memory");
        var voterRepo = RepositoryFactory.createVoterRepository("memory");
        service = new VoteService(voteRepo, candidateRepo, voterRepo);
        // Ne créez PAS VotingApp ici, car on va en créer un nouveau à chaque test
        // avec un Scanner simulé
    }

    @Test
    void testAddCandidateViaCLI() throws Exception {
        String input = """
            add
            C3
            Charlie
            list
            exit
            """;

        String output = SystemLambda.tapSystemOutNormalized(() -> {
            Scanner testScanner = new Scanner(input);  // Scanner qui lit l'input simulé

            VotingApp app = new VotingApp(service, testScanner);
            app.start();
        });

        assertTrue(output.contains("Charlie"));
        assertTrue(output.contains("C3"));
        assertTrue(output.contains("Candidates:"));
        assertTrue(output.contains("Alice")); // candidats par défaut
        assertTrue(output.contains("Bob"));
    }

    @Test
    void testVoteFlow() throws Exception {
        String input = """
            vote
            John Doe
            C1
            count
            exit
            """;

        String output = SystemLambda.tapSystemOutNormalized(() -> {
            Scanner testScanner = new Scanner(input);

            VotingApp app = new VotingApp(service, testScanner);
            app.start();
        });

        assertTrue(output.contains("John Doe"));
        assertTrue(output.contains("Alice: 1 votes"));
        assertTrue(output.contains("Results (Plurality Strategy)"));
        assertTrue(output.contains("Winner: Alice"));
    }

    @Test
    void testResetFlow() throws Exception {
        // Étape 1 : ajouter un vote
        String voteInput = """
            vote
            Alice Voter
            C1
            exit
            """;

        SystemLambda.withTextFromSystemIn(voteInput.lines().toArray(String[]::new))
                .execute(() -> {
                    Scanner scanner = new Scanner(voteInput);
                    new VotingApp(service, scanner).start();
                });

        // Étape 2 : vérifier le reset
        String resetInput = """
            count
            reset
            yes
            count
            exit
            """;

        String output = SystemLambda.tapSystemOutNormalized(() -> {
            Scanner scanner = new Scanner(resetInput);
            new VotingApp(service, scanner).start();
        });

        assertTrue(output.contains("Alice: 1 votes"), "Doit afficher le vote avant reset");
        assertTrue(output.contains("System reset"));
        assertTrue(output.contains("No votes yet"));
    }

    @Test
    void testUnknownCommand() throws Exception {
        String input = """
            hello
            exit
            """;

        String output = SystemLambda.tapSystemOutNormalized(() -> {
            Scanner testScanner = new Scanner(input);
            new VotingApp(service, testScanner).start();
        });

        assertTrue(output.contains("Unknown command"));
    }
}