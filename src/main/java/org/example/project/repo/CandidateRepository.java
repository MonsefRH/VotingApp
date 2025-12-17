package org.example.project.repo;


import org.example.project.model.Candidate;

import java.util.*;


public interface CandidateRepository {

    void add(Candidate candidate);
    List<Candidate> findAll();
    Candidate findById(String id);
    boolean exists(String id);
    void clear();
}