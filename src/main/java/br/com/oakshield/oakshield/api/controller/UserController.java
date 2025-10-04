package br.com.oakshield.oakshield.api.controller;

import br.com.oakshield.oakshield.api.dto.request.UserRequest;
import br.com.oakshield.oakshield.api.dto.response.UserResponse;
import br.com.oakshield.oakshield.api.mapper.UserMapper;
import br.com.oakshield.oakshield.core.domain.user.User;
import br.com.oakshield.oakshield.core.service.UserService;
import br.com.oakshield.oakshield.utils.RestUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/v1/users")
@CrossOrigin(origins = "*")
@Tag(name = "User Controller", description = "Operations related to artist categories")
public class UserController {

    private final UserService service;
    private final UserMapper userMapper;

    public UserController(UserService service, UserMapper userMapper) {
        this.service = service;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Create a new user",
            description = "Creates a new user in the system. " +
                    "The user must have a valid email and password. " +
                    "Returns the created user with its ID.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "User created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest userRequest) {
        var user = userMapper.from(userRequest);
        var userCreate = service.create(user);
        if (userCreate == null) {
            throw new IllegalArgumentException("Erro ao criar usuário.");
        }
        var userResponse = userMapper.to(userCreate);

        URI location = RestUtils.getUri(userResponse.getId());
        return ResponseEntity.created(location).body(userResponse);
    }

    @Operation(summary = "Update an existing user",
            description = "Updates an existing user in the system. " +
                    "The user must have a valid ID and can update their email and password. " +
                    "Returns the updated user.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "User updated successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable("id") UUID id, @Valid @RequestBody UserRequest userRequest) {
        var user = userMapper.from(userRequest);
        User userUpdate = service.update(id, user);
        if (userUpdate == null) {
            throw new IllegalArgumentException("Tipo de ativo não encontrado.");
        }
        UserResponse userResponse = userMapper.to(userUpdate);
        return ResponseEntity.ok(userResponse);
    }

    @Operation(summary = "Get all users",
            description = "Retrieves a list of all users in the system. " +
                    "Returns an empty list if no users are found.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "List of users retrieved successfully"),
                    @ApiResponse(responseCode = "204", description = "No users found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll() {
        List<User> users = service.findAll();
        List<UserResponse> usersResponse = userMapper.map(users);
        return ResponseEntity.ok(usersResponse);
    }

    @Operation(summary = "Get user by ID",
            description = "Retrieves a user by their ID. " +
                    "Returns an error if the user is not found.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable UUID id) {
        User user = service.findById(id);
        UserResponse userResponse = userMapper.to(user);
        return ResponseEntity.ok(userResponse);
    }

    @Operation(summary = "Delete user by ID",
            description = "Deletes a user by their ID. " +
                    "Returns an error if the user is not found.")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "User deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "User not found"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!service.existsById(id)) {
            throw new IllegalArgumentException("Tipo de ativo não encontrado.");
        }
        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
