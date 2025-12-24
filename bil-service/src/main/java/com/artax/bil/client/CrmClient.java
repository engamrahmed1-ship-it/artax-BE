package com.artax.bil.client;

import com.artax.bil.dto.customer.create.*;
import com.artax.bil.dto.customer.get.*;
import com.artax.bil.dto.customer.update.ProjectDetailUpdateDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Optional;


@Component
public class CrmClient {


    private final WebClient client;
    private static final String CUSTOMER_PATH = "/internal/customer";
    private static final String OPPORTUNITY_PATH = "/internal/opportunity";
    private static final String PROJECT_PATH = "/internal/projects";
    private static final String INTERACTION_PATH = "/internal/interactions";
    private static final String LEAD_PATH = "/internal/lead";
    private static final String TICKET_PATH = "/internal/ticket";
    private static final String DOCUMENT_PATH = "/internal/document";

    public CrmClient(@Qualifier("crmWebClient") WebClient client) {
        this.client = client;
    }


    public Mono<CustomerDto> createCustomer(String authHeader, CustomerCreateRequest req) {
        Mono<CustomerDto> customerMono = client.post()
                .uri(CUSTOMER_PATH)
                .headers(headers -> {
                    if (authHeader != null) headers.set(HttpHeaders.AUTHORIZATION, authHeader);
                })
                .bodyValue(req)
                .retrieve()
                .bodyToMono(CustomerDto.class)
                .timeout(Duration.ofSeconds(10));

        // 2. Block and wait for the result (use a timeout for safety)
        return customerMono; // Blocks the thread for up to 10 seconds
    }

    /**
     * Call CRM /internal/customer/search
     * @param authHeader e.g. "Bearer <jwt>" (can be null)
     */
    public Mono<PaginationSearchResponse> searchCustomers(String authHeader,
                                                          Long id,
                                                          String name,
                                                          String phone,
                                                          String custType,
                                                          int page,
                                                          int size) {
        WebClient.RequestHeadersSpec<?> req = client.
                get()
                .uri(uriBuilder -> uriBuilder
                        .path(CUSTOMER_PATH+"/search")
                        // Only add parameters if they have meaningful values
                        .queryParamIfPresent("id", Optional.ofNullable(id)) // id is Long → auto toString()
                        .queryParamIfPresent("name", Optional.ofNullable(name).filter(s -> !s.isBlank()))
                        .queryParamIfPresent("phone", Optional.ofNullable(phone).filter(s -> !s.isBlank()))
                        .queryParamIfPresent("custType", Optional.ofNullable(custType).filter(s -> !s.isBlank()))
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build()
                )
                .headers(headers -> {
                    if (authHeader != null) headers.set(HttpHeaders.AUTHORIZATION, authHeader);
                });


        return req.retrieve()
                .onStatus(status -> status.is4xxClientError(), resp -> resp.createException())
                .onStatus(status -> status.is5xxServerError(), resp -> resp.createException())
                .bodyToMono(PaginationSearchResponse.class);
    }

    // ---------------- GET CUSTOMER BY ID ----------------
//    public Mono<CustomerDto> getCustomerById(String authHeader, Long customerId) {
//        return client.get()
//                .uri(CUSTOMER_PATH+"/search/{id}", customerId)
//                .headers(headers -> setAuthHeader(headers, authHeader))
//                .retrieve()
//                .bodyToMono(CustomerDto.class)
//                .timeout(Duration.ofSeconds(10));
//    }


    // Add contact
    public Mono<Void> addContact(String authHeader, Long customerId, CompanyContactRequest request) {
        return client.post()
                .uri(CUSTOMER_PATH+"/{id}/contact", customerId)
                .headers(headers -> {
                    if (authHeader != null) headers.set(HttpHeaders.AUTHORIZATION, authHeader);
                })
                .bodyValue(request)
                .retrieve()
                .bodyToMono(Void.class);
    }

    // Delete contact
    public Mono<Void> deleteContact(String authHeader, Long customerId, Long contactId) {
        return client.delete()
                .uri(CUSTOMER_PATH+"/{customerId}/contact/{contactId}", customerId, contactId)
                .headers(headers -> {
                    if (authHeader != null) headers.set(HttpHeaders.AUTHORIZATION, authHeader);
                })
                .retrieve()
                .bodyToMono(Void.class);
    }

    // Set primary contact
    public Mono<Void> setPrimaryContact(String authHeader, Long customerId, Long contactId) {
        return client.put()
                .uri(CUSTOMER_PATH+"/{customerId}/contact/primary/{contactId}", customerId, contactId)
                .headers(headers -> {
                    if (authHeader != null) headers.set(HttpHeaders.AUTHORIZATION, authHeader);
                })
                .retrieve()
                .bodyToMono(Void.class);
    }


 // START OF PROJECTS
    // ---------------- GET PROJECTS ----------------
    public Mono<PaginationSearchResponse> getProjectsByCustomer(String authHeader, Long customerId
            , int page, int size) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(PROJECT_PATH+"/customer/{customerId}")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build(customerId))
                .headers(headers -> {
                    if (authHeader != null) headers.set(HttpHeaders.AUTHORIZATION, authHeader);
                })
                .retrieve()
                .bodyToMono(PaginationSearchResponse.class)
                .timeout(Duration.ofSeconds(10))
                .retry(2); // Try 2 more times if it times out
    }


        public Mono<ProjectDto> getProjectById(String auth, Long projectId) {
            return client.get()
                    .uri(PROJECT_PATH+"/{projectId}", projectId)
                    .headers(headers -> {
                        if (auth != null) headers.set(HttpHeaders.AUTHORIZATION, auth);
                    })
                    .retrieve()
                    .bodyToMono(ProjectDto.class);
        }

        // --- PROJECT WRITE OPERATIONS ---

        public Mono<ProjectDto> createProject(String auth, Long customerId, ProjectCreateRequest dto) {
            return client.post()
                    .uri(PROJECT_PATH+"/customer/{customerId}", customerId)
                    .headers(headers -> {
                        if (auth != null) headers.set(HttpHeaders.AUTHORIZATION, auth);
                    })
                    .bodyValue(dto)
                    .retrieve()
                    .bodyToMono(ProjectDto.class);
        }

        public Mono<Void> deleteProject(String auth, Long projectId) {
            return client.delete()
                    .uri(PROJECT_PATH+"/{projectId}", projectId)
                    .headers(headers -> {
                        if (auth != null) headers.set(HttpHeaders.AUTHORIZATION, auth);
                    })
                    .retrieve()
                    .toBodilessEntity()
                    .then();
        }

        // --- PROJECT DETAIL OPERATIONS ---


    public Mono<ProjectDetailDto> addProjectDetail(String auth, Long projectId, ProjectDetailsCreateRequest dto) {
        return client.post()
                .uri(PROJECT_PATH+"/{projectId}/detail", projectId)
                .headers(headers -> {
                    if (auth != null) headers.set(HttpHeaders.AUTHORIZATION, auth);
                })
                .bodyValue(dto)
                .retrieve()
                // Handle 404 if project doesn't exist or 400 for validation errors
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .flatMap(errorBody -> Mono.error(new RuntimeException("CRM API Error: " + errorBody))))
                .bodyToMono(ProjectDetailDto.class);
    }

        public Mono<ProjectDetailDto> updateProjectDetail(String auth, Long detailId, ProjectDetailUpdateDto dto) {
            return client.put()
                    .uri(PROJECT_PATH+"/detail/{detailId}", detailId)
                    .headers(headers -> {
                        if (auth != null) headers.set(HttpHeaders.AUTHORIZATION, auth);
                    })
                    .bodyValue(dto)
                    .retrieve()
                    .bodyToMono(ProjectDetailDto.class);
        }

        public Mono<Void> deleteProjectDetail(String auth, Long detailId) {
            return client.delete()
                    .uri(PROJECT_PATH+"/detail/{detailId}", detailId)
                    .headers(headers -> {
                        if (auth != null) headers.set(HttpHeaders.AUTHORIZATION, auth);
                    })
                    .retrieve()
                    .toBodilessEntity()
                    .then();
        }


    // ------------------------------- END OF PROJECTS

    // START OF INTERACTIONS
    // ---------------- GET INTERACTIONS ----------------

    public Mono<InteractionDto> createInteraction(String auth, Long customerId,
                                                  InteractionCreateRequest dto) {
        return client.post()
                .uri(INTERACTION_PATH + "/{customerId}", customerId)
                .headers(headers -> {
                    if (auth != null) headers.set(HttpHeaders.AUTHORIZATION, auth);
                })
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(InteractionDto.class);
    }

    /**
     * Retrieves a paginated list of interactions for a specific customer.
     */
    public Mono<PaginationSearchResponse> getCustomerInteractions(String auth, Long customerId
            , int page, int size) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(INTERACTION_PATH + "/{customerId}")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build(customerId))
                .headers(headers -> {
                    if (auth != null) headers.set(HttpHeaders.AUTHORIZATION, auth);
                })
                .retrieve()
                .bodyToMono(PaginationSearchResponse.class);
    }

    /**
     * Deletes a specific interaction.
     */
    public Mono<Void> deleteInteraction(String auth, Long interactionId) {
        return client.delete()
                .uri(INTERACTION_PATH + "/{interactionId}", interactionId)
                .headers(headers -> {
                    if (auth != null) headers.set(HttpHeaders.AUTHORIZATION, auth);
                })
                .retrieve()
                .bodyToMono(Void.class);
    }


    // ------------------------------- END OF INTERACTIONS

    // START OF DOCUMENTS
    // ---------------- GET DOCUMENTS ----------------

    public Mono<DocumentDto> addDocument(String auth, Long customerId, DocumentCreateRequest dto) {
        return client.post()
                .uri(DOCUMENT_PATH + "/{customerId}", customerId)
                .headers(headers -> {
                    if (auth != null) headers.set(HttpHeaders.AUTHORIZATION, auth);
                })
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(DocumentDto.class);
    }

    /**
     * Retrieves a paginated list of documents associated with a customer.
     */
    public Mono<PaginationSearchResponse> getDocuments(String auth, Long customerId, int page, int size) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(DOCUMENT_PATH + "/{customerId}")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build(customerId))
                .headers(headers -> {
                    if (auth != null) headers.set(HttpHeaders.AUTHORIZATION, auth);
                })
                .retrieve()
                .bodyToMono(PaginationSearchResponse.class);
    }

    /**
     * Deletes a document by its unique ID.
     */
    public Mono<Void> deleteDocument(String auth, Long documentId) {
        return client.delete()
                .uri(DOCUMENT_PATH + "/{documentId}", documentId)
                .headers(headers -> {
                    if (auth != null) headers.set(HttpHeaders.AUTHORIZATION, auth);
                })
                .retrieve()
                .bodyToMono(Void.class);
    }

    // ------------------------------- END OF DOCUMENTS
    // START OF TICKET
    /**
     * Creates a new support ticket for a specific customer.
     */
    public Mono<TicketDto> createTicket(String auth, Long customerId, TicketCreateRequest dto) {
        return client.post()
                .uri(TICKET_PATH + "/{customerId}", customerId)
                .headers(headers -> {
                    if (auth != null) headers.set(HttpHeaders.AUTHORIZATION, auth);
                })
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(TicketDto.class);
    }

    /**
     * Retrieves a paginated list of tickets for a specific customer.
     */
    public Mono<PaginationSearchResponse> getTickets(String auth, Long customerId, int page, int size) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(TICKET_PATH + "/{customerId}")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build(customerId))
                .headers(headers -> {
                    if (auth != null) headers.set(HttpHeaders.AUTHORIZATION, auth);
                })
                .retrieve()
                .onStatus(HttpStatusCode::is5xxServerError, response -> {
                            System.err.println("Ticket service is down or failing");
                            return Mono.empty();
                })
                .bodyToMono(PaginationSearchResponse.class)
                .onErrorReturn(PaginationSearchResponse.empty());
    }

    /**
     * Deletes a ticket by its ID.
     */
    public Mono<Void> deleteTicket(String auth, Long ticketId) {
        return client.delete()
                .uri(TICKET_PATH + "/{ticketId}", ticketId)
                .headers(headers -> {
                    if (auth != null) headers.set(HttpHeaders.AUTHORIZATION, auth);
                })
                .retrieve()
                .bodyToMono(Void.class);
    }

    // ------------------------------- END OF TICKET

    // START OF OPPORTUNITY

    /**
     * Creates a new opportunity.
     */
    public Mono<OpportunityDto> create(String auth, OpportunityCreateRequest dto) {
        return client.post()
                .uri(OPPORTUNITY_PATH)
                .headers(headers -> {
                    if (auth != null) headers.set(HttpHeaders.AUTHORIZATION, auth);
                })
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(OpportunityDto.class);
    }

    /**
     * Retrieves a single opportunity by its ID.
     */
    public Mono<OpportunityDto> getById(String auth, Long id) {
        return client.get()
                .uri(OPPORTUNITY_PATH + "/{id}", id)
                .headers(headers -> {
                    if (auth != null) headers.set(HttpHeaders.AUTHORIZATION, auth);
                })
                .retrieve()
                .bodyToMono(OpportunityDto.class);
    }

    /**
     * Lists opportunities for a specific customer with pagination.
     * Maps to /internal/opportunity/customer/{customerId}
     */
    public Mono<PaginationSearchResponse> getOpportunitesByCustomer(String auth, Long customerId, int page, int size) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .path(OPPORTUNITY_PATH + "/customer/{customerId}")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build(customerId))
                .headers(headers -> {
                    if (auth != null) headers.set(HttpHeaders.AUTHORIZATION, auth);
                })
                .retrieve()
                .bodyToMono(PaginationSearchResponse.class);
    }

    /**
     * Updates an existing opportunity.
     */
    public Mono<OpportunityDto> update(String auth, Long opportunityId, OpportunityCreateRequest dto) {
        return client.put()
                .uri(OPPORTUNITY_PATH + "/{opportunityId}", opportunityId)
                .headers(headers -> {
                    if (auth != null) headers.set(HttpHeaders.AUTHORIZATION, auth);
                })
                .bodyValue(dto)
                .retrieve()
                .bodyToMono(OpportunityDto.class);
    }

    /**
     * Deletes an opportunity.
     */
    public Mono<Void> delete(String auth, Long opportunityId) {
        return client.delete()
                .uri(OPPORTUNITY_PATH + "/{opportunityId}", opportunityId)
                .headers(headers -> {
                    if (auth != null) headers.set(HttpHeaders.AUTHORIZATION, auth);
                })
                .retrieve()
                .bodyToMono(Void.class);
    }

    // ------------------------------- END OF OPPORTUNITY


    // ---------------- HELPER ----------------
    private void setAuthHeader(HttpHeaders headers, String authHeader) {
        if (authHeader != null && !authHeader.isBlank()) {
            headers.set(HttpHeaders.AUTHORIZATION, authHeader);
        }
    }







}








