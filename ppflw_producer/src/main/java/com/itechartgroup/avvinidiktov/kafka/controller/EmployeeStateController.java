package com.itechartgroup.avvinidiktov.kafka.controller;


import com.itechartgroup.avvinidiktov.core.model.ChangeStateReq;
import com.itechartgroup.avvinidiktov.kafka.producer.impl.EmployeeStateProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/v1/state/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeStateController {
    private final EmployeeStateProducer stateProducer;

    @Operation(summary = "Run new event on state machine of employee to change state")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Not Accepted")})
    @PostMapping("{employeeId}")
    public ResponseEntity<String> setState(@RequestBody ChangeStateReq req) throws ExecutionException, InterruptedException, TimeoutException {
        return stateProducer.sendMessage(req)
                ? ResponseEntity.ok(req.getEvent())
                : ResponseEntity.badRequest().body("Not Accepted");
    }
}
