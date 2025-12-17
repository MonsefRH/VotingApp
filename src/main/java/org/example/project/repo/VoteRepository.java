package org.example.project.repo;


import java.util.List;
import org.example.project.model.Vote;

public interface VoteRepository {

    void save(Vote vote);
    List<Vote> findAll();
    int count();
    void clear();
    boolean hasVoted(String voterId);
}
