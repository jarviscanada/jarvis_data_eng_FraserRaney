package ca.jrvs.apps.trading.data.repository;

import ca.jrvs.apps.trading.data.entity.Trader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraderJpaRepository extends JpaRepository<Trader, Integer> {

}
