package hyun.concurrencycontrolprac.repository;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import hyun.concurrencycontrolprac.entity.Stock;

public interface StockRepository extends JpaRepository<Stock, Long> {

	@Lock(value = LockModeType.PESSIMISTIC_WRITE)
	@Query("select s from Stock s where s.id=:id")
	Stock findByIdWithPessimisticLock(@Param("id") Long id);

	@Lock(value = LockModeType.OPTIMISTIC)
	@Query("select s from Stock s where s.id=:id")
	Stock findByIdWithOptimisticLock(@Param("id") Long id);
}
