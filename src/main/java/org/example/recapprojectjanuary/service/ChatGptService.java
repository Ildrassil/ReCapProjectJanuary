package org.example.recapprojectjanuary.service;

import org.example.recapprojectjanuary.ChatGPTRequest;
import org.example.recapprojectjanuary.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Service
public class ChatGptService {


    private final RestClient restClient;

    public ChatGptService(@Value("${app.chatgpt.api.url}") String url,
                             @Value("${app.chatgpt.api.key}") String key,
                             @Value("${app.chatgpt.api.org}") String org){
        restClient = RestClient.builder()
                .baseUrl(url)
                .defaultHeader("Authorization", "Bearer " + key)
                .defaultHeader("OpenAi-Organization",org)
                .build();
    }

    public String spellCheck(String message){
        Optional<ChatGptResponse> response = Optional.ofNullable(restClient.post()
                .uri("/completions")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ChatGPTRequest("Can you rewrite this Description of a Todo to be more fitting " + message + " please Output only the Description"))
                .retrieve()
                .body(ChatGptResponse.class));
        if(response.isPresent()){
        return response.get().text();}
        else throw new RuntimeException("ToDo could not be created");

    }
}
