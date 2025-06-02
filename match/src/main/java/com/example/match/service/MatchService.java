package com.example.match.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.example.match.model.Match;

@Service
public class MatchService {
    Map<String, Match> matchMap=new ConcurrentHashMap<>();
    AtomicInteger newId=new AtomicInteger(1);
    public String createMatch(String teamA, String teamB) {
        String id = String.valueOf(newId.getAndIncrement());
        Match match = new Match(teamA, teamB);
        matchMap.put(id, match);
        return id;
    }
    

    public int incrementScore(String id,String team) {
        Match match = matchMap.get(id);
        if (match == null){
            return -1;
        }
        if(team.equalsIgnoreCase(match.getTeamA())){
            match.incrementScoreA();
            return 1;
        }
        else if(team.equalsIgnoreCase(match.getTeamB())){
            match.incrementScoreB();
            return 1;
        }
        else{
            return -1;
        }

    }
    public String getMatchDetails(String id) {
        Match match=matchMap.get(id);
        if(match==null){
            return "";
        }
        return match.getMatchScore();
    }
    public boolean endMatch(String id) {
        Match match = matchMap.get(id);
        if (match == null) {
            return false;
        }
        match.endMatch();
        return true;
    }
    public int countActiveMatches() {
        //TODO: Implement a function that returns the number of live matches in functional programming style
       return(int) this.matchMap.values().stream()
        .filter(match -> match.getStatus()==1)
        .count();
        
    }
    public int getGoalCount(String team){
        /*TODO: Implement a function that returns the total number of goals scored
         *by a specific team in all matches.
         *Use the functional programming style.
        */
        return this.matchMap.values().stream()
        .mapToInt(match -> match.getTeamScore(team))
        .sum();  
    }


}
