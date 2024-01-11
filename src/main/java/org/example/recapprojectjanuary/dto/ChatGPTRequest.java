package org.example.recapprojectjanuary;

import org.example.recapprojectjanuary.dto.ChatGPTMessage;

import java.util.Collections;
import java.util.List;

public record ChatGPTRequest(
        String model,
        List<ChatGPTMessage> messages
)  {
    public ChatGPTRequest(String message) {
        this("gpt-3.5-turbo", Collections.singletonList(new ChatGPTMessage("user", message)));
    }
}
