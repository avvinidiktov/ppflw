package com.itechartgroup.avvinidiktov.PeopleFlow.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.itechartgroup.avvinidiktov.PeopleFlow.statemachine.event.EmployeeEvent;
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
    private EmployeeEvent event;
}
