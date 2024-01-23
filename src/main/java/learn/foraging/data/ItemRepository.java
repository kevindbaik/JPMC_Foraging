package learn.foraging.data;

import learn.foraging.models.Item;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository {
    List<Item> findAll();

    Item findById(int id);

    Item add(Item item) throws DataException;
}
