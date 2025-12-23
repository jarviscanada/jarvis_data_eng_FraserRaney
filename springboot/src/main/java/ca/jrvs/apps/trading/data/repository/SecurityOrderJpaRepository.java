package ca.jrvs.apps.trading.data.repository;

import ca.jrvs.apps.trading.data.entity.SecurityOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SecurityOrderJpaRepository extends JpaRepository<SecurityOrder, Integer> {

}
