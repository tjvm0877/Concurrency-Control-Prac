package hyun.concurrencycontrolprac.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hyun.concurrencycontrolprac.entity.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {
}
