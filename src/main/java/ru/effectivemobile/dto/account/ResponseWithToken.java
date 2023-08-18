package ru.effectivemobile.dto.account;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResponseWithToken(
        @JsonProperty("auth-token")
        String authToken
) {
}
