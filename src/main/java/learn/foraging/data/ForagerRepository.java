package learn.foraging.data;

import learn.foraging.models.Forager;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.List;

@Repository
public interface ForagerRepository {
    Forager add(Forager forager) throws IOException;

    Forager findById(String id);

    List<Forager> findAll();

    List<Forager> findByState(String stateAbbr);
}
