package ch.fhnw.ip6.praxisruf.configuration.web.controller;

import ch.fhnw.ip6.praxisruf.commons.dto.configuration.CallGroupDto;
import ch.fhnw.ip6.praxisruf.configuration.api.CallGroupService;
import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/callgroups")
@AllArgsConstructor
@Api(tags = "CallGroups")
public class CallGroupController {

    private final CallGroupService callGroupService;

    @GetMapping("/{id}")
    public CallGroupDto findById(@PathVariable("id") UUID callGroupId){
        return callGroupService.findById(callGroupId);
    }

    @GetMapping
    public Set<CallGroupDto> findAll(){
        return callGroupService.findAll();
    }

    @PostMapping
    @Operation(description = "Create a new client configuration")
    public CallGroupDto create(@RequestBody CallGroupDto callGroupDto) {
        return callGroupService.create(callGroupDto);
    }

    @PutMapping
    @Operation(description = "Update an existing client configuration")
    public CallGroupDto update(@RequestBody CallGroupDto callGroupDto) {
        return callGroupService.update(callGroupDto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") UUID callGroupId){
        callGroupService.deleteById(callGroupId);
    }

    @DeleteMapping("/many/{filter}")
    public void deleteMany(@PathVariable List<UUID> filter) {
        callGroupService.deleteAllById(filter);
    }
    
}
