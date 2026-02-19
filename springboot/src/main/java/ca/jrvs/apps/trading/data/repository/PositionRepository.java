package ca.jrvs.apps.trading.data.repository;

import ca.jrvs.apps.trading.data.entity.Position;
import ca.jrvs.apps.trading.data.entity.PositionKey;
import java.util.List;


public interface PositionRepository extends ReadOnlyRepository<Position, PositionKey> {

  List<Position> findAllByIdAccountId(Integer accountId);

  Integer countAllByIdAccountId(Integer accountId);
}
