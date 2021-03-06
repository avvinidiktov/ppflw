package com.itechartgroup.avvinidiktov.controller;

import com.itechartgroup.avvinidiktov.core.model.EmployeeDTO;
import com.itechartgroup.avvinidiktov.producer.impl.EmployeeProducer;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeController {

    private final EmployeeProducer producer;

    @Operation(summary = "Add new employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = EmployeeDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid request",
                    content = @Content),})
    @PostMapping("employee")
    public ResponseEntity<EmployeeDTO> add(@RequestBody EmployeeDTO employeeDTO) throws InterruptedException, ExecutionException, TimeoutException {
        return producer.sendMessage(employeeDTO)
                .map(value -> new ResponseEntity<>(value, HttpStatus.CREATED))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }
}
