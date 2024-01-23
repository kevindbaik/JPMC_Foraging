package learn.foraging.domain;

import learn.foraging.data.ForagerRepository;
import learn.foraging.models.Forager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ForagerService {

    private final ForagerRepository repository;

    @Autowired
    public ForagerService(ForagerRepository repository) {
        this.repository = repository;
    }

    public List<Forager> findByState(String stateAbbr) {
        return repository.findByState(stateAbbr);
    }

    public List<Forager> findByLastName(String prefix) {
        return repository.findAll().stream()
                .filter(i -> i.getLastName().startsWith(prefix))
                .collect(Collectors.toList());
    }

    public Result<Forager> addForager(Forager forager) {
        Result<Forager> result = new Result<>();

        if (isInvalid(forager)) {
            result.addErrorMessage("Forager validation failed");
            return result;
        }

        if (isDuplicate(forager)) {
            result.addErrorMessage("Duplicate forager found");
            return result;
        }

        try {
            forager = repository.add(forager);
            result.setPayload(forager);
        } catch (Exception e) {
            result.addErrorMessage("Error adding forager: " + e.getMessage());
        }
        return result;
    }

    private boolean isInvalid(Forager forager) {
        if (forager == null) {
            return true;
        }
        if (forager.getFirstName() == null || forager.getFirstName().trim().isEmpty()) {
            return true;
        }
        if (forager.getLastName() == null || forager.getLastName().trim().isEmpty()) {
            return true;
        }
        return forager.getState() == null || forager.getState().trim().isEmpty();
    }

    private boolean isDuplicate(Forager forager) {
        List<Forager> existingForagers = repository.findAll();
        for (Forager existing : existingForagers) {
            if (existing.getFirstName().equalsIgnoreCase(forager.getFirstName()) &&
                    existing.getLastName().equalsIgnoreCase(forager.getLastName()) &&
                    existing.getState().equalsIgnoreCase(forager.getState())) {
                return true;
            }
        }
        return false;
    }
}
