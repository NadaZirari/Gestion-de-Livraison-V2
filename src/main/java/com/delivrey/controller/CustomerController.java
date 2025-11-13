package com.delivrey.controller;

import com.delivrey.dto.CustomerDto;
import com.delivrey.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
@Tag(
    name = "Customer API",
    description = "API pour la gestion des clients",
    externalDocs = @io.swagger.v3.oas.annotations.ExternalDocumentation(
        description = "Documentation complète sur la gestion des clients",
        url = "https://github.com/NadaZirari/Gestion-de-Livraison-V2/wiki/API-Customers"
    )
)
@SecurityRequirement(name = "bearerAuth")
public class CustomerController {

    private final CustomerService customerService;
    private static final String CUSTOMER_NOT_FOUND = "Client non trouvé avec l'ID fourni";
    private static final String INVALID_REQUEST = "Requête invalide";

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Récupère tous les clients avec pagination",
        description = "Retourne une page de clients avec pagination et filtrage optionnel"
    )
    @Parameter(
        name = "page",
        description = "Numéro de page (0-based)",
        example = "0",
        schema = @Schema(type = "integer", defaultValue = "0")
    )
    @Parameter(
        name = "size",
        description = "Nombre d'éléments par page",
        example = "20",
        schema = @Schema(type = "integer", defaultValue = "20")
    )
    @Parameter(
        name = "sort",
        description = "Critères de tri au format: property,{asc|desc}",
        example = "name,asc",
        schema = @Schema(type = "string")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Liste des clients récupérée avec succès",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Non authentifié",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "403",
            description = "Non autorisé",
            content = @Content
        )
    })
    public ResponseEntity<Page<CustomerDto>> getAllCustomers(
            @Parameter(hidden = true) @PageableDefault(size = 20) @NonNull Pageable pageable) {
        try {
            log.info("Fetching all customers with pagination: {}", pageable);
            return ResponseEntity.ok(customerService.findAll(pageable));
        } catch (Exception e) {
            log.error("Error fetching customers: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/search")
    @Operation(
        summary = "Recherche des clients",
        description = "Recherche des clients par nom et/ou adresse"
    )
    @Parameter(
        name = "name",
        description = "Nom du client à rechercher (optionnel)",
        example = "John",
        schema = @Schema(type = "string")
    )
    @Parameter(
        name = "address",
        description = "Adresse à rechercher (optionnel)",
        example = "Paris",
        schema = @Schema(type = "string")
    )
    @Parameter(
        name = "page",
        description = "Numéro de page (0-based)",
        example = "0",
        schema = @Schema(type = "integer", defaultValue = "0")
    )
    @Parameter(
        name = "size",
        description = "Nombre d'éléments par page",
        example = "20",
        schema = @Schema(type = "integer", defaultValue = "20")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Résultats de la recherche",
            content = @Content(mediaType = "application/json")
        )
    })
    public ResponseEntity<Page<CustomerDto>> searchCustomers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @Parameter(hidden = true) @PageableDefault(size = 20) Pageable pageable) {
        try {
            log.info("Searching customers with name: {}, address: {}", name, address);
            return ResponseEntity.ok(customerService.search(name, address, pageable));
        } catch (Exception e) {
            log.error("Error searching customers: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Récupère un client par son ID",
        description = "Retourne les détails d'un client spécifique en fonction de son identifiant unique"
    )
    @Parameter(
        name = "id",
        description = "ID du client à récupérer",
        required = true,
        example = "1",
        schema = @Schema(type = "integer")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Client trouvé avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CustomerDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Client non trouvé",
            content = @Content
        )
    })
    public ResponseEntity<CustomerDto> getCustomerById(
            @PathVariable @NonNull Long id) {
        try {
            log.info("Fetching customer with id: {}", id);
            return customerService.findById(id)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error fetching customer with id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Crée un nouveau client",
        description = "Crée un nouveau client avec les informations fournies"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Détails du client à créer",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = CustomerDto.class),
            examples = @ExampleObject(
                name = "CustomerExample",
                value = "{\n  \"firstName\": \"John\",\n  \"lastName\": \"Doe\",\n  \"email\": \"john.doe@example.com\",\n  \"phone\": \"+1234567890\",\n  \"address\": \"123 Main St, City, Country\"\n}"
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "201",
            description = "Client créé avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CustomerDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Données du client invalides",
            content = @Content
        )
    })
    public ResponseEntity<CustomerDto> createCustomer(
            @Valid @RequestBody CustomerDto customerDto) {
        try {
            log.info("Creating new customer: {}", customerDto);
            CustomerDto savedCustomer = customerService.save(customerDto);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(savedCustomer);
        } catch (Exception e) {
            log.error("Error creating customer: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
        summary = "Met à jour un client existant",
        description = "Met à jour les informations d'un client existant avec l'ID fourni"
    )
    @Parameter(
        name = "id",
        description = "ID du client à mettre à jour",
        required = true,
        example = "1",
        schema = @Schema(type = "integer")
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Nouvelles informations du client",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = CustomerDto.class)
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Client mis à jour avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = CustomerDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Données du client invalides",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Client non trouvé",
            content = @Content
        )
    })
    public ResponseEntity<CustomerDto> updateCustomer(
            @PathVariable @NonNull Long id,
            @Valid @RequestBody CustomerDto customerDto) {
        try {
            log.info("Updating customer with id {}: {}", id, customerDto);
            return ResponseEntity.ok(customerService.update(id, customerDto));
        } catch (Exception e) {
            log.error("Error updating customer with id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Supprime un client",
        description = "Supprime un client existant avec l'ID fourni"
    )
    @Parameter(
        name = "id",
        description = "ID du client à supprimer",
        required = true,
        example = "1",
        schema = @Schema(type = "integer")
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "204",
            description = "Client supprimé avec succès",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Client non trouvé",
            content = @Content
        )
    })
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable @NonNull Long id) {
        try {
            log.info("Deleting customer with id: {}", id);
            customerService.deleteById(id);
            log.info("Successfully deleted customer with id: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting customer with id {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
        return ResponseEntity.ok(customerService.search(name, address, pageable));
    }
}
