package com.itechartgroup.avvinidiktov.core.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeStateReq {
    @JsonProperty("employeeId")
    private Long employeeId;
    @JsonProperty("event")
    private String event;
}
