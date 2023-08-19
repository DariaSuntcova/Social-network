package ru.effectivemobile.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;

public record AuthResponse(
        @JsonProperty("auth-token")
        String authToken
) {
}
