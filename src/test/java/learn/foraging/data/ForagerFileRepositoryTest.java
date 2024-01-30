package learn.foraging.data;

import learn.foraging.models.Forager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ForagerFileRepositoryTest {

    // I made this BeforeEach to reset test data every run. I was running into an issue where without this,
    // everytime I ran the test I would get +1 more than expected in my findAll because I was adding a forager in my 2nd test.
    // If you would like me to resolve this a different way, let me know.
    @BeforeEach
    void setUp() throws IOException {
        Path testFilePath = Paths.get("./data/forager_data_test/2024-01-29.csv");
        Files.deleteIfExists(testFilePath);
        Files.write(testFilePath, List.of("id,firstName,lastName,state", "1,John,Doe,CA", "2,Jane,Doe,NV"), StandardCharsets.UTF_8);
    }
    @Test
    void shouldFindAll() {
        ForagerFileRepository repo = new ForagerFileRepository("./data/forager_data_test/2024-01-29.csv");
        List<Forager> all = repo.findAll();
        assertEquals(2, all.size());
    }

    @Test
    void shouldAddForager() throws IOException {
        ForagerFileRepository repo = new ForagerFileRepository("./data/forager_data_test/2024-01-29.csv");
        Forager forager = new Forager();
        forager.setFirstName("Test");
        forager.setLastName("Forager");
        forager.setState("CA");

        Forager added = repo.add(forager);

        assertNotNull(added.getId());
        assertEquals("Test", added.getFirstName());
        assertEquals("Forager", added.getLastName());
        assertEquals("CA", added.getState());

        List<String> lines = Files.readAllLines(Paths.get("./data/forager_data_test/2024-01-29.csv"));
        assertTrue(lines.stream().anyMatch(l -> l.contains(added.getId()) && l.contains("Test") && l.contains("Forager")));
    }
}