package org.example.project.observer;


import org.example.project.model.Vote;

public interface VoteListener {

    void onVote(Vote vote);
}
