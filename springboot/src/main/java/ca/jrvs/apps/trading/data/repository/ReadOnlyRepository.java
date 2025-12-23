package ca.jrvs.apps.trading.data.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface ReadOnlyRepository<T, ID> extends Repository<T, ID> {

  List<T> findAll();

  Optional<T> findById(ID id);

  long count();

  boolean existsById(ID id);

  List<T> findAllById(Iterable<ID> ids);
}

