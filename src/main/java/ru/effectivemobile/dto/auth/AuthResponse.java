package ru.effectivemobile.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthResponse(
        @JsonProperty("Authorization")
        String authToken
) {
}
