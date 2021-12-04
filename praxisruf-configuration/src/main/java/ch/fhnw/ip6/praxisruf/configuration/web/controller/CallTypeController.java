package ch.fhnw.ip6.praxisruf.configuration.web.controller;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.CallTypeDto;
import ch.fhnw.ip6.praxisruf.configuration.api.CallTypeService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/calltypes")
@AllArgsConstructor
@Api(tags = "CallTypes")
public class CallTypeController {

    private final CallTypeService callTypeService;

    @GetMapping("/{id}")
    public CallTypeDto findById(@PathVariable("id") UUID callTypeId){
        return callTypeService.findById(callTypeId);
    }

    @GetMapping
    public Set<CallTypeDto> findAll(){
        return callTypeService.findAll();
    }

    @PostMapping
    @Operation(description = "Create a new client configuration")
    public CallTypeDto create(@RequestBody CallTypeDto callTypeDto) {
        return callTypeService.create(callTypeDto);
    }

    @PutMapping
    @Operation(description = "Update an existing client configuration")
    public CallTypeDto update(@RequestBody CallTypeDto callTypeDto) {
        return callTypeService.update(callTypeDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") UUID callTypeId){
        callTypeService.deleteById(callTypeId);
    }

    @DeleteMapping("/many/{filter}")
    public void deleteMany(@PathVariable List<UUID> filter) {
        callTypeService.deleteAllById(filter);
    }
    
}
