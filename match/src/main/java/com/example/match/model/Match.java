package com.example.match.model;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;

@Getter
public class Match {
    private final String teamA;
    private final String teamB;
    private AtomicInteger scoreA;
    private AtomicInteger scoreB;
    private MatchStat status;
    public Match (String teamA, String teamB) {
        this.teamA = teamA;
        this.teamB = teamB;
        this.scoreA = new AtomicInteger(0);
        this.scoreB = new AtomicInteger(0);
        this.status=MatchStat.LIVE;
    }
    public void incrementScoreA(){
        this.scoreA.incrementAndGet();
    }
    public void incrementScoreB(){
        this.scoreB.incrementAndGet();
    }
    public String getMatchScore(){
        return teamA+":"+scoreA+" "+teamB+":"+scoreB;
    }
    public int getStatus(){
        if(this.status==MatchStat.LIVE) return 1;
        else return 0;
    }
    public void endMatch(){
        this.status=MatchStat.ENDED;
    }
    public int getTeamScore(String team){
        if(teamA.equalsIgnoreCase(team)) return scoreA.intValue(); 
        else if(teamB.equalsIgnoreCase(team)) return scoreB.intValue();
        return 0;
    }
}

