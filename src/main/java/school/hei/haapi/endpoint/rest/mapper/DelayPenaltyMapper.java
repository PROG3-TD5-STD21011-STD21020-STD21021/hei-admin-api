package school.hei.haapi.endpoint.rest.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import school.hei.haapi.endpoint.rest.model.CreateDelayPenaltyChange;
import school.hei.haapi.model.DelayPenalty;
import school.hei.haapi.endpoint.rest.model.DelayPenalty.InterestTimerateEnum;
import school.hei.haapi.model.User;
import school.hei.haapi.model.validator.CreateUpdateDelayPenaltyValidator;
import school.hei.haapi.repository.UserRepository;

@Component
@AllArgsConstructor
public class DelayPenaltyMapper {
    private final UserRepository userRepository;
    private final CreateUpdateDelayPenaltyValidator delayPenaltyValidator;
    public school.hei.haapi.endpoint.rest.model.DelayPenalty toRest(DelayPenalty domain) {
        return new school.hei.haapi.endpoint.rest.model.DelayPenalty()
                .id(domain.getId())
                .interestPercent(domain.getInterestPercent())
                .interestTimerate(domain.getInterestTimerate())
                .creationDatetime(domain.getCreationDatetime())
                .graceDelay(domain.getGraceDelay())
                .applicabilityDelayAfterGrace(domain.getApplicabilityDelayAfterGrace());
    }

    public DelayPenalty toDomainDelayPenalty(CreateDelayPenaltyChange rest) {
        User specifiedUser = userRepository.getById(rest.getStudentId());
        delayPenaltyValidator.accept(rest);
        return DelayPenalty.builder()
                .interestPercent(rest.getInterestPercent())
                .interestTimerate(InterestTimerateEnum.valueOf(rest.getInterestTimerate().toString()))
                .graceDelay(rest.getGraceDelay())
                .applicabilityDelayAfterGrace(rest.getApplicabilityDelayAfterGrace())
            .student(specifiedUser)
                .build();
    }


}
