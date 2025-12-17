package org.example.project;


import org.example.project.factory.RepositoryFactory;
import org.example.project.model.*;
import org.example.project.observer.*;
import org.example.project.service.VoteService;
import org.example.project.strategy.*;

import java.util.*;


public class VotingApp {

    private final VoteService service;
    private final Scanner scanner;

    public VotingApp(VoteService service) {
        this(service, new Scanner(System.in));
    }
    public VotingApp(VoteService service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
        initialize();
    }

    private void initialize() {
        // Ajouter les candidats par défaut
        service.addCandidate("C1", "Alice");
        service.addCandidate("C2", "Bob");

        service.addListener(new LoggingVoteListener());
        service.addListener(new AuditVoteListener());
    }

    public void start() {
        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║       VOTING SYSTEM (Refactored)          ║");
        System.out.println("║   Commands: vote, count, add, reset, exit ║");
        System.out.println("╚════════════════════════════════════════════╝\n");

        boolean running = true;
        while (running) {
            System.out.print(">>> ");
            String cmd = scanner.nextLine().trim().toLowerCase();

            try {
                switch (cmd) {
                    case "vote" -> handleVote();
                    case "count" -> handleCount();
                    case "add" -> handleAddCandidate();
                    case "reset" -> handleReset();
                    case "list" -> handleListCandidates();
                    case "voters" -> handleListVoters();
                    case "exit" -> {
                        System.out.println("Goodbye!");
                        running = false;
                    }
                    default -> System.out.println("❌ Unknown command. Try: vote, count, add, list, voters, reset, exit");
                }
            } catch (Exception e) {
                System.err.printf("❌ Error: %s%n", e.getMessage());
            }
        }

    }

    private void handleVote() {
        System.out.print("Enter voter name: ");
        String voterName = scanner.nextLine().trim();

        if (voterName.isBlank()) {
            System.out.println("❌ Voter name cannot be empty");
            return;
        }

        String voterId = "V_" + System.currentTimeMillis();

        service.registerVoter(voterId, voterName);

        // Afficher les candidats
        System.out.println("\n📋 Candidates:");
        for (Candidate c : service.getCandidates()) {
            System.out.printf("  - %s (ID: %s)%n", c.getName(), c.getId());
        }

        System.out.print("\nEnter candidate ID to vote for: ");
        String candidateId = scanner.nextLine().trim();

        service.castVote(voterId, candidateId);
    }

    private void handleCount() {
        if (service.getTotalVoteCount() == 0) {
            System.out.println("⚠️  No votes yet.");
            return;
        }

        System.out.println("\n📊 Results (Plurality Strategy):");
        Map<String, Integer> results = service.countVotes(new PluralityCountingStrategy());
        displayResults(results);

        // Afficher le gagnant
        Candidate winner = service.getWinner(new PluralityCountingStrategy());
        if (winner != null) {
            System.out.printf("\n🏆 Winner: %s%n", winner.getName());
        }

        System.out.println("\n📊 Results (Ranked Choice Strategy):");
        Map<String, Integer> rankedResults = service.countVotes(new RankedChoiceCountingStrategy());
        displayResults(rankedResults);

        Candidate rankedWinner = service.getWinner(new RankedChoiceCountingStrategy());
        if (rankedWinner != null) {
            System.out.printf("\n🏆 Winner (Ranked): %s%n", rankedWinner.getName());
        }
    }


    private void displayResults(Map<String, Integer> results) {
        results.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .forEach(e -> {
                    Candidate c = null;
                    for (Candidate candidate : service.getCandidates()) {
                        if (candidate.getId().equals(e.getKey())) {
                            c = candidate;
                            break;
                        }
                    }
                    if (c != null) {
                        System.out.printf("  %s: %d votes%n", c.getName(), e.getValue());
                    }
                });

        int total = results.values().stream().mapToInt(Integer::intValue).sum();
        System.out.printf("  Total: %d votes%n", total);
    }

    private void handleAddCandidate() {
        System.out.print("Enter candidate ID: ");
        String id = scanner.nextLine().trim();

        System.out.print("Enter candidate name: ");
        String name = scanner.nextLine().trim();

        service.addCandidate(id, name);
    }


    private void handleListCandidates() {
        List<Candidate> candidates = service.getCandidates();
        if (candidates.isEmpty()) {
            System.out.println("⚠️  No candidates.");
            return;
        }

        System.out.println("\n📋 Candidates:");
        for (Candidate c : candidates) {
            System.out.printf("  - %s (ID: %s)%n", c.getName(), c.getId());
        }
    }

    private void handleListVoters() {
        List<Voter> voters = service.getVoters();
        if (voters.isEmpty()) {
            System.out.println("⚠️  No voters.");
            return;
        }

        System.out.println("\n👥 Registered Voters:");
        for (Voter v : voters) {
            System.out.printf("  - %s (ID: %s)%n", v.getName(), v.getId());
        }
    }

    private void handleReset() {
        System.out.print("Are you sure? (yes/no): ");
        String confirm = scanner.nextLine().trim().toLowerCase();

        if (confirm.equals("yes")) {
            service.reset();
            initialize();
            System.out.println("✓ System reset");
        }
    }


    public static void main(String[] args) {
        // Factory Pattern : Créer les repositories
        var voteRepository = RepositoryFactory.createVoteRepository("memory");
        var candidateRepository = RepositoryFactory.createCandidateRepository("memory");
        var voterRepository = RepositoryFactory.createVoterRepository("memory");

        // Créer le service avec injection de dépendances
        var service = new VoteService(voteRepository, candidateRepository, voterRepository);

        // Lancer l'application
        var app = new VotingApp(service, new Scanner(System.in));
        app.start();
    }
}