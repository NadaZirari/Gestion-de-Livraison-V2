package com.delivrey.controller;

import com.delivrey.entity.Customer;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customers")
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

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

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
    public ResponseEntity<Page<Customer>> getAllCustomers(
            @Parameter(hidden = true) @PageableDefault(size = 20) @NonNull Pageable pageable) {
        return ResponseEntity.ok(customerService.findAll(pageable));
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
            description = "Client trouvé",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Customer.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = CUSTOMER_NOT_FOUND,
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = INVALID_REQUEST,
            content = @Content
        )
    })
    public ResponseEntity<Customer> getCustomerById(
            @PathVariable @NonNull Long id) {
        return customerService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
        summary = "Crée un nouveau client",
        description = "Crée un nouveau client avec les informations fournies"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
        description = "Détails du client à créer",
        required = true,
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = Customer.class),
            examples = @ExampleObject(
                name = "customerExample",
                value = "{\"name\": \"John Doe\", \"email\": \"john@example.com\", \"address\": \"123 Main St\"}"
            )
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Client créé avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Customer.class)
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Données du client invalides",
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Un client avec cet email existe déjà",
            content = @Content
        )
    })
public ResponseEntity<Customer> createCustomer(
            @Valid @org.springframework.web.bind.annotation.RequestBody @NonNull Customer customer) {
        return ResponseEntity.ok(customerService.save(customer));
    }

    @PutMapping(
        value = "/{id}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(
        summary = "Met à jour un client existant",
        description = "Met à jour les informations d'un client existant en fonction de son ID"
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
            schema = @Schema(implementation = Customer.class)
        )
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Client mis à jour avec succès",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Customer.class)
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = CUSTOMER_NOT_FOUND,
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = INVALID_REQUEST,
            content = @Content
        )
    })
    public ResponseEntity<Customer> updateCustomer(
            @PathVariable @NonNull Long id,
            @Valid @org.springframework.web.bind.annotation.RequestBody @NonNull Customer customer) {
        return ResponseEntity.ok(customerService.update(id, customer));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Supprime un client",
        description = "Supprime un client en fonction de son ID. Cette action est irréversible."
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
            description = CUSTOMER_NOT_FOUND,
            content = @Content
        ),
        @ApiResponse(
            responseCode = "400",
            description = INVALID_REQUEST,
            content = @Content
        ),
        @ApiResponse(
            responseCode = "409",
            description = "Impossible de supprimer le client car il est associé à des livraisons",
            content = @Content
        )
    })
    public ResponseEntity<Void> deleteCustomer(@PathVariable @NonNull Long id) {
        customerService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Recherche des clients avec filtres")
    public ResponseEntity<Page<Customer>> searchCustomers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String address,
            @PageableDefault(size = 20) @NonNull Pageable pageable) {
        return ResponseEntity.ok(customerService.search(name, address, pageable));
    }
}
