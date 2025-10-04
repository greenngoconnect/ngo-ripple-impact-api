package br.com.oakshield.oakshield.api.controller;

import br.com.oakshield.oakshield.api.dto.response.UserAcknowledgementResponse;
import br.com.oakshield.oakshield.api.mapper.UserAcknowledgementMapper;
import br.com.oakshield.oakshield.core.domain.useracknowledgement.UserAcknowledgement;
import br.com.oakshield.oakshield.core.service.UserAcknowledgementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/user-acknowledgements")
@CrossOrigin(origins = "*")
@Tag(name = "UserAcknowledgement Controller", description = "Operations related to artist categories")
public class UserAcknowledgementController {

    private final UserAcknowledgementService service;
    private final UserAcknowledgementMapper userAcknowledgementMapper;

    public UserAcknowledgementController(UserAcknowledgementService service, UserAcknowledgementMapper userAcknowledgementMapper) {
        this.service = service;
        this.userAcknowledgementMapper = userAcknowledgementMapper;
    }

    @Operation(summary = "Acknowledge a policy for a user",
            description = "Allows a user to acknowledge a specific policy by providing their user ID and the policy ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Policy acknowledged successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid request parameters"),
                    @ApiResponse(responseCode = "404", description = "User or policy not found")
            }
    )
    @PutMapping
    public ResponseEntity<Void> acknowledge(@RequestParam UUID userId, @RequestParam UUID policyId) {
        service.acknowledgePolicy(userId, policyId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Get all user acknowledgements",
            description = "Retrieves a list of all user acknowledgements.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "List of user acknowledgements retrieved successfully"),
                    @ApiResponse(responseCode = "204", description = "No user acknowledgements found")
            }
    )
    @GetMapping
    public ResponseEntity<List<UserAcknowledgementResponse>> getAll() {
        List<UserAcknowledgement> userAcknowledgements = service.getAll();
        List<UserAcknowledgementResponse> userAcknowledgementResponses = userAcknowledgementMapper.map(userAcknowledgements);
        return ResponseEntity.ok(userAcknowledgementResponses);
    }
}
