package ch.fhnw.ip5.praxiscloudservice.web.configuration.controller;

import ch.fhnw.ip5.praxiscloudservice.api.configuration.ClientService;
import ch.fhnw.ip5.praxiscloudservice.api.dto.ClientDto;
import ch.fhnw.ip5.praxiscloudservice.api.dto.MinimalClientDto;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/clients")
@AllArgsConstructor
@Api(tags = "Client")
public class ClientsController {

    private final ClientService clientService;

    // ###### Admin CRUD Operations
    @GetMapping("/{id}")
    public ClientDto getClientById(@PathVariable("id") UUID clientId) {
        return clientService.findById(clientId);
    }

    @GetMapping()
    public Set<ClientDto> getAllClients() {
        return clientService.findAll();
    }

    @PostMapping
    public ClientDto createClient(@RequestBody ClientDto clientDto) {
        return clientService.create(clientDto);
    }

    @PutMapping
    public ClientDto updateClient(@RequestBody ClientDto clientDto){
        return clientService.update(clientDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id){
        clientService.deleteById(id);}

    @DeleteMapping("/many/{filter}")
    public void deleteMany(@PathVariable List<UUID> filter){
        clientService.deleteAllById(filter);
    }

    // ###### Client API

    /**
     * @return Set of key value pairs where each entry consists of (clientName, clientId)
     */
    @GetMapping("/byUser")
    @Operation(description = "Find all available clients for a given user")
    public Set<MinimalClientDto> getAvailableClients() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return clientService.findByUserId((UUID) auth.getDetails());
    }

}
