package learn.foraging.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import learn.foraging.models.Forager;
import learn.foraging.data.ForageRepositoryDouble;
import learn.foraging.data.ForagerRepositoryDouble;
import learn.foraging.data.ItemRepositoryDouble;
import learn.foraging.domain.ForageService;
import learn.foraging.domain.ForagerService;
import learn.foraging.domain.Result;

class ForagerServiceTest {

    private ForagerService service = new ForagerService(new ForagerRepositoryDouble());

    @Test
    void shouldAddValidForager() {
        Forager forager = new Forager();
        forager.setFirstName("John");
        forager.setLastName("Doe");
        forager.setState("TX");

        Result<Forager> result = service.addForager(forager);

        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
        assertEquals("John", result.getPayload().getFirstName());
        assertEquals("Doe", result.getPayload().getLastName());
        assertEquals("TX", result.getPayload().getState());
    }

    @Test
    void shouldNotAddForagerWithNoFirstName() {
        Forager forager = new Forager();
        forager.setFirstName(null);
        forager.setLastName("Doe");
        forager.setState("TX");

        Result<Forager> result = service.addForager(forager);

        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        assertTrue(result.getErrorMessages().contains("First name is required."));
    }

    @Test
    void shouldNotAddForagerWithNoLastName() {
        Forager forager = new Forager();
        forager.setFirstName("Jane");
        forager.setLastName(null);
        forager.setState("TX");

        Result<Forager> result = service.addForager(forager);

        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        assertTrue(result.getErrorMessages().contains("Last name is required."));
    }

    @Test
    void shouldNotAddForagerWithNoState() {
        Forager forager = new Forager();
        forager.setFirstName("Jane");
        forager.setLastName("Doe");
        forager.setState(null);

        Result<Forager> result = service.addForager(forager);

        assertFalse(result.isSuccess());
        assertNull(result.getPayload());
        assertTrue(result.getErrorMessages().contains("State is required."));
    }

    @Test
    void shouldNotAddDuplicateForager() {
        Forager forager = new Forager();
        forager.setFirstName("Samantha");
        forager.setLastName("Green");
        forager.setState("FL");

        Result<Forager> result = service.addForager(forager);
        assertTrue(result.isSuccess());

        Forager forager2 = new Forager();
        forager2.setFirstName("Samantha");
        forager2.setLastName("Green");
        forager2.setState("FL");

        Result<Forager> result2 = service.addForager(forager2);
        assertFalse(result2.isSuccess());
        assertTrue(result2.getErrorMessages().contains("Duplicate forager found"));
    }
}