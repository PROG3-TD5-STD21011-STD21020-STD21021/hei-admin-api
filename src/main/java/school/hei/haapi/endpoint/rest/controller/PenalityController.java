package school.hei.haapi.endpoint.rest.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import school.hei.haapi.endpoint.rest.mapper.PenalityMapper;
import school.hei.haapi.endpoint.rest.model.CreateDelayPenaltyChange;
import school.hei.haapi.model.Penalty;
import school.hei.haapi.service.PenalityService;
import school.hei.haapi.endpoint.rest.model.DelayPenalty;

@RestController
@AllArgsConstructor
public class PenalityController {
    private final PenalityService service;
    private final PenalityMapper mapper;

    @GetMapping(value = "/delay_penality")
    public DelayPenalty getPenalityDelay() {
        return mapper.toRest(service.get());
    }

    @PutMapping(value = "/delay_penality_change")
    public DelayPenalty changePenalityDelay(
            @RequestBody CreateDelayPenaltyChange delayPenality
            ) {
        Penalty toSave = mapper.toDomainPenality(delayPenality);
        return mapper.toRest(service.save(toSave));
    }
}
