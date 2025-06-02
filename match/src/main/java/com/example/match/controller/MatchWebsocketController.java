package com.example.match.controller;

import java.util.Map;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.example.match.model.Message;
import com.example.match.service.MatchService;

/*TODO: Implement the MatchWebsocketController class that handles the websocket connections
* and messages as follows:
    * 1. handle updating the score of a specific match and send the updated score to subscribers
    * 2. Handle end match messages and send notification with match end to match subscribers
    Subscribers expect you to send a Message object in both cases
Your endpoints/topics should be named appropriately
    */
@Controller
public class MatchWebsocketController {
    private final MatchService matchService;
    
    public MatchWebsocketController(MatchService matchService) {
        this.matchService = matchService;
    }
   
    @MessageMapping("/updateScore/{matchId}")
    @SendTo("/topic/updateScore/{matchId}")
    public Message updateScore(@DestinationVariable String matchId, @Payload Map<String, String> payload) {
        String team = payload.get("team");
        System.out.println("Received update score message for match " + matchId + " with team " + team);
        
        int status = matchService.incrementScore(matchId, team);
        if (status == -1) {
            return new Message("Invalid match ID or team name.", false);
        }
        return new Message(matchService.getMatchDetails(matchId), false);
    }
    @MessageMapping("/endMatch/{matchId}")
    @SendTo("/topic/endMatch/{matchId}")
    public Message endMatch(@DestinationVariable String matchId) {
        matchService.endMatch(matchId);
        String messageEnd="Match " + matchId + " has ended "+
                "with score: " + matchService.getMatchDetails(matchId);
        return new Message(messageEnd, true);
    }
    
    
}
