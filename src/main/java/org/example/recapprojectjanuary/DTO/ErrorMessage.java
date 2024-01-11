package org.example.recapprojectjanuary.DTO;

import java.time.Instant;

public record ErrorMessage(String errorMessage,
                           Instant errorTime
) {
    public ErrorMessage(String errorMessage) {
        this(errorMessage, Instant.now());
    }
}