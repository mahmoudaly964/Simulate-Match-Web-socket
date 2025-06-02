package com.example.matchclient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandler;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;

public class MatchWebsocketHandler {
StompSession stompSession;
private SockJsClient sockJsClient;
    public void connectToWebSocket() {
        try {
        //  TODO: Complete the code to create a WebSocket client        
            List<Transport> transports = Collections.singletonList(new WebSocketTransport(new StandardWebSocketClient()));
             sockJsClient=new SockJsClient(transports);
            WebSocketStompClient stompClient = new WebSocketStompClient(sockJsClient);
            List<MessageConverter> converters = new ArrayList<>();
            converters.add(new MappingJackson2MessageConverter());
            converters.add(new StringMessageConverter());
            stompClient.setMessageConverter(new CompositeMessageConverter(converters));
             String url = "ws://localhost:8080/ws"; // WebSocket endpoint
            StompSessionHandler sessionHandler = new MyStompSessionHandler();
            stompSession = stompClient.connect(url, sessionHandler).get();
            System.out.println("Connected to WebSocket server");
        } catch (Exception e) {
            System.out.println("Websocket connection failed");
        }
    }
    public void updateScore(String team, String matchId) {
        // First check if the connection is active
        if (stompSession == null || !stompSession.isConnected()) {
            System.out.println("Reconnecting to WebSocket server...");
            connectToWebSocket();
        }
        Map<String, String> payload = new HashMap<>();
        payload.put("team", team);
        
        String destination = "/app/updateScore/" + matchId;
        stompSession.send(destination, payload);
        System.out.println("Score updated for match " + matchId + " with team " + team);
    }
    public void endMatch(String matchId){
        //TODO: write the code for sending end match message
        // Send the end match message to the server
        String destination="/app/endMatch/"+ matchId;
        stompSession.send(destination, matchId);
        // Print confirmation message
        System.out.println("Match " + matchId + " has ended");
    }
    public void subscribeToMatch(String matchId) {
        try {
            // Connect to the WebSocket server if not already connected
            if (stompSession == null || !stompSession.isConnected()) {
                connectToWebSocket();
            }
            // Subscribe to score updates for this specific match
            stompSession.subscribe("/topic/updateScore/"+ matchId, new StompSessionHandlerAdapter() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return Message.class; 
                }

                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    try {
                        Message message = (Message) payload;
                        System.out.println("Score update: " + message.matchScore());
                    } catch (Exception e) {
                        System.out.println("Error processing message: " + e.getMessage());
                    }
                }
            });

            // Subscribe to end match notifications for this specific match
            stompSession.subscribe("/topic/endMatch/"+ matchId, new StompSessionHandlerAdapter() {
                @Override
                public Type getPayloadType(StompHeaders headers) {
                    return Message.class; 
                }
                
                @Override
                public void handleFrame(StompHeaders headers, Object payload) {
                    try {
                        Message message = (Message) payload;
                        System.out.println("Match ended: " + message.matchScore());
                        System.out.println("Exiting subscription as match has ended");
                        close(); // Close the connection when match ends
                        //exit the program
                        System.exit(0);
                       
                    } catch (Exception e) {
                        System.out.println("Error processing end match message: " + e.getMessage());
                    }
                }    
            });
            System.out.println("Subscribed to match " + matchId);
        } catch (Exception e) {
            System.out.println("Failed to subscribe to match: " + e.getMessage());
        }
    }
    public void close(){
        if (this.stompSession != null) {
            this.stompSession.disconnect();
            System.out.println("Closed websocket connection");
        }
        if (this.sockJsClient != null) {
            this.sockJsClient.stop(); 
            System.out.println("Stopped SockJsClient");
        }
    }
}
class MyStompSessionHandler extends StompSessionHandlerAdapter {
    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        System.err.println("An error occurred: " + exception.getMessage());
    }
}