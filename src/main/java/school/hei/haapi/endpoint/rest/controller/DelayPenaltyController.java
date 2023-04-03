package school.hei.haapi.endpoint.rest.controller;

import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class DelayPenaltyController {
    private final DelayPenaltyService service;
    private final DelayPenaltyMapper mapper;

    @GetMapping(value = "/delay_penalty")
    public List<school.hei.haapi.endpoint.rest.model.DelayPenalty> getPenaltyDelay() {
        return service.get().stream().map(mapper::toRest).collect(Collectors.toList());
    }

    @PutMapping(value = "/delay_penalty_change")
    public school.hei.haapi.endpoint.rest.model.DelayPenalty changePenaltyDelay(
            @RequestBody CreateDelayPenaltyChange delayPenalty
            ) {
        DelayPenalty toSave = mapper.toDomainDelayPenalty(delayPenalty);
        if(delayPenalty.getStudentId() != null) {
            //validator
            return mapper.toRest(service.save(toSave, toSave.getStudent().getId()));
        }
        return mapper.toRest(service.save(toSave));
    }
}
