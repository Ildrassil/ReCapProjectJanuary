package org.example.recapprojectjanuary.dto;

import java.util.List;

public record ChatGptResponse(
        List<ChatGPTChoice> choices
) {
    public String text() {
        return choices.get(0).message().content();
    }
}
