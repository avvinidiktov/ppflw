package com.itechartgroup.avvinidiktov.PeopleFlow.kafka.controller;


import com.itechartgroup.avvinidiktov.PeopleFlow.kafka.producer.impl.EmployeeStateProducer;
import com.itechartgroup.avvinidiktov.PeopleFlow.model.dto.ChangeStateReq;
import com.itechartgroup.avvinidiktov.PeopleFlow.service.EmployeeStateService;
import com.itechartgroup.avvinidiktov.PeopleFlow.statemachine.state.EmployeeState;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/state/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeStateController {
    private final EmployeeStateService stateService;
    private final EmployeeStateProducer stateProducer;

    @Operation(summary = "Get current employee state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad request")})
    @GetMapping("{employeeId}")
    public ResponseEntity<EmployeeState> getState(@PathVariable("employeeId") Long employeeId) {
        return stateService.getStateForEmployee(employeeId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @Operation(summary = "Run new event on state machine of employee to change state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Not Accepted")})
    @PostMapping("{employeeId}")
    public ResponseEntity<String> setState(@RequestBody ChangeStateReq req) throws ExecutionException, InterruptedException {
        return stateProducer.sendMessage(req)
                ? ResponseEntity.ok(req.getEvent().name())
                : ResponseEntity.badRequest().body("Not Accepted");
    }
}
