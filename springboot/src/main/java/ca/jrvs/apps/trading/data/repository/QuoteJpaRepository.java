package ca.jrvs.apps.trading.data.repository;

import ca.jrvs.apps.trading.data.entity.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteJpaRepository extends JpaRepository<Quote, String> {

}
