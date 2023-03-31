package school.hei.haapi.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import school.hei.haapi.model.Penalty;
import school.hei.haapi.repository.PenalityRepository;

@Service
@AllArgsConstructor
public class PenalityService {
    private final PenalityRepository repository;

    public Penalty get() {
        return repository.findAll().get(0);
    }
    public Penalty save(Penalty toSave) {
        return repository.save(toSave);
    }
}
