package school.hei.haapi.endpoint.rest.mapper;

import org.springframework.stereotype.Component;
import school.hei.haapi.endpoint.rest.model.CreateDelayPenaltyChange;
import school.hei.haapi.endpoint.rest.model.DelayPenalty;
import school.hei.haapi.model.Penalty;

@Component
public class PenalityMapper {
    public DelayPenalty toRest(Penalty domain) {
        return new DelayPenalty()
                .id(domain.getId())
                .interestPercent(domain.getInterestPercent())
                .interestTimerate(domain.getInterestTimerate())
                .creationDatetime(domain.getCreationDatetime())
                .graceDelay(domain.getGraceDelay())
                .applicabilityDelayAfterGrace(domain.getApplicabilityDelayAfterGrace());
    }

    public Penalty toDomainPenality(CreateDelayPenaltyChange rest) {
        return Penalty.builder()
                .interestPercent(rest.getInterestPercent())
                .interestTimerate(DelayPenalty.InterestTimerateEnum.valueOf(rest.getInterestTimerate().toString()))
                .graceDelay(rest.getGraceDelay())
                .applicabilityDelayAfterGrace(rest.getApplicabilityDelayAfterGrace())
                .build();
    }
}
