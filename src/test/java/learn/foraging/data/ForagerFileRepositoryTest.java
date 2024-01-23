package learn.foraging.data;

import learn.foraging.models.Forager;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ForagerFileRepositoryTest {

    @Test
    void shouldFindAll() {
        ForagerFileRepository repo = new ForagerFileRepository("./data/foragers.csv");
        List<Forager> all = repo.findAll();
        assertEquals(1000, all.size());
    }

    @Test
    void shouldAddForager() throws IOException {
        ForagerFileRepository repo = new ForagerFileRepository("./data/foragers.csv");
        Forager forager = new Forager();
        forager.setFirstName("Test");
        forager.setLastName("Forager");
        forager.setState("CA");

        Forager added = repo.add(forager);

        assertNotNull(added.getId());
        assertEquals("Test", added.getFirstName());
        assertEquals("Forager", added.getLastName());
        assertEquals("CA", added.getState());

        List<String> lines = Files.readAllLines(Paths.get("./data/foragers.csv"));
        assertTrue(lines.stream().anyMatch(l -> l.contains(added.getId()) && l.contains("Test") && l.contains("Forager")));
    }
}