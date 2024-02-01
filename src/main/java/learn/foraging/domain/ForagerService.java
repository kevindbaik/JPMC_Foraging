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
        Result<Forager> validation = validateForager(forager);

        if (!validation.isSuccess()) {
            return validation;
        }

        if (isDuplicate(forager)) {
            validation.addErrorMessage("Duplicate forager found");
            return validation;
        }

        try {
            forager = repository.add(forager);
            validation.setPayload(forager);
        } catch (Exception e) {
            validation.addErrorMessage("Error adding forager: " + e.getMessage());
        }

        return validation;
    }

    private Result<Forager> validateForager(Forager forager) {
        Result<Forager> result = new Result<>();

        if (forager == null) {
            result.addErrorMessage("Forager must not be null.");
            return result;
        }
        if (forager.getFirstName() == null || forager.getFirstName().trim().isEmpty()) {
            result.addErrorMessage("First name is required.");
        }
        if (forager.getLastName() == null || forager.getLastName().trim().isEmpty()) {
            result.addErrorMessage("Last name is required.");
        }
        if (forager.getState() == null || forager.getState().trim().isEmpty()) {
            result.addErrorMessage("State is required.");
        }

        if (result.isSuccess()) {
            result.setPayload(forager);
        }

        return result;
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
