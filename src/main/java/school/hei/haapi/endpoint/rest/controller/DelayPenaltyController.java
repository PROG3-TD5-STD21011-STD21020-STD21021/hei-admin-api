package school.hei.haapi.endpoint.rest.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import school.hei.haapi.endpoint.rest.mapper.DelayPenaltyMapper;
import school.hei.haapi.endpoint.rest.model.CreateDelayPenaltyChange;
import school.hei.haapi.model.DelayPenalty;
import school.hei.haapi.service.DelayPenaltyService;

@RestController
@AllArgsConstructor
public class DelayPenaltyController {
    private final DelayPenaltyService service;
    private final DelayPenaltyMapper mapper;

    @GetMapping(value = "/delay_penality")
    public school.hei.haapi.endpoint.rest.model.DelayPenalty getPenalityDelay() {
        return mapper.toRest(service.get());
    }

    @PutMapping(value = "/delay_penality_change")
    public school.hei.haapi.endpoint.rest.model.DelayPenalty changePenalityDelay(
            @RequestBody CreateDelayPenaltyChange delayPenality
            ) {
        DelayPenalty toSave = mapper.toDomainDelayPenality(delayPenality);
        return mapper.toRest(service.save(toSave));
    }
}
