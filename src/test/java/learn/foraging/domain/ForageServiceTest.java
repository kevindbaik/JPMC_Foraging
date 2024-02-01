package learn.foraging.domain;

import learn.foraging.data.DataException;
import learn.foraging.data.ForageRepositoryDouble;
import learn.foraging.data.ForagerRepositoryDouble;
import learn.foraging.data.ItemRepositoryDouble;
import learn.foraging.models.Category;
import learn.foraging.models.Forage;
import learn.foraging.models.Forager;
import learn.foraging.models.Item;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ForageServiceTest {
    private ForageService service;
    private ForageRepositoryDouble forageRepository;
    private ItemRepositoryDouble itemRepository;

    @BeforeEach
    void setUp() {
        forageRepository = new ForageRepositoryDouble();
        ForagerRepositoryDouble foragerRepository = new ForagerRepositoryDouble();
        ItemRepositoryDouble itemRepository = new ItemRepositoryDouble();
        service = new ForageService(forageRepository, foragerRepository, itemRepository);
    }

    @Test
    void shouldAdd() throws DataException {
        Forage forage = new Forage();
        forage.setDate(LocalDate.now());
        forage.setForager(ForagerRepositoryDouble.FORAGER);
        forage.setItem(ItemRepositoryDouble.ITEM);
        forage.setKilograms(0.5);

        Result<Forage> result = service.add(forage);
        assertTrue(result.isSuccess());
        assertNotNull(result.getPayload());
        assertEquals(36, result.getPayload().getId().length());
    }

    @Test
    void shouldNotAddWhenForagerNotFound() throws DataException {

        Forager forager = new Forager();
        forager.setId("30816379-188d-4552-913f-9a48405e8c08");
        forager.setFirstName("Ermengarde");
        forager.setLastName("Sansom");
        forager.setState("NM");

        Forage forage = new Forage();
        forage.setDate(LocalDate.now());
        forage.setForager(forager);
        forage.setItem(ItemRepositoryDouble.ITEM);
        forage.setKilograms(0.5);

        Result<Forage> result = service.add(forage);
        assertFalse(result.isSuccess());
    }

    @Test
    void shouldNotAddWhenItemNotFound() throws DataException {

        Item item = new Item(11, "Dandelion", Category.EDIBLE, new BigDecimal("0.05"));

        Forage forage = new Forage();
        forage.setDate(LocalDate.now());
        forage.setForager(ForagerRepositoryDouble.FORAGER);
        forage.setItem(item);
        forage.setKilograms(0.5);

        Result<Forage> result = service.add(forage);
        assertFalse(result.isSuccess());
    }

    @Test
    void getKgPerItemReport_ShouldReturnCorrectTotals() {
        LocalDate testDate = forageRepository.date;
        Item item = itemRepository.ITEM;

        Map<Item, Double> report = service.getKgPerItemReport(testDate);
        assertNotNull(report);
        assertFalse(report.isEmpty());

        double expectedKgForItem1 = 1.25;
        assertEquals(expectedKgForItem1, report.get(item));
    }

    @Test
    void getCategoryValueReport_ShouldReturnCorrectTotals() {
        LocalDate testDate = forageRepository.getDate();

        Map<Category, BigDecimal> report = service.getCategoryValueReport(testDate);

        assertNotNull(report);
        assertFalse(report.isEmpty());

        BigDecimal expectedValueForEdible = new BigDecimal("12.4875");
        assertEquals(expectedValueForEdible, report.get(Category.EDIBLE));
    }
}