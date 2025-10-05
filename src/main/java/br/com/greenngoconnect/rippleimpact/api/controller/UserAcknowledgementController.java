package br.com.greenngoconnect.rippleimpact.api.controller;

import br.com.greenngoconnect.rippleimpact.api.dto.response.UserAcknowledgementResponse;
import br.com.greenngoconnect.rippleimpact.api.mapper.UserAcknowledgementMapper;
import br.com.greenngoconnect.rippleimpact.core.domain.useracknowledgement.UserAcknowledgement;
import br.com.greenngoconnect.rippleimpact.core.service.UserAcknowledgementService;
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
