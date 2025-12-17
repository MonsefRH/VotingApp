package org.example.project.repo;


import org.example.project.model.Voter;

import java.util.*;

public interface VoterRepository {
    void register(Voter voter);
    List<Voter> findAll();
    Voter findById(String id);
    boolean isRegistered(String id);
    void clear();
}
