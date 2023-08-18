package ru.effectivemobile.dto.notification;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
@ApiModel
public record NotificationsDto(
        @JsonProperty("enable")
        @ApiModelProperty(value = "enable", example = "true")
        boolean enable,
        @JsonProperty("type")
        @ApiModelProperty(value = "type", example = "POST")
        String type
) {

}
