package hyun.concurrencycontrolprac.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hyun.concurrencycontrolprac.entity.Stock;

/*
* 이번 프로젝트에서는 편의를 위해 하나의 데이터소스를 이용하지만,
* 실무에서는 Connection Pool이 부족해 질 수 있기 때문에 별도의 데이터소스를 이용하여 접근하는 것을 권장한다.
*/
public interface LockRepository extends JpaRepository<Stock, Long> {
	@Query(value = "select get_lock(:key, 3000)", nativeQuery = true)
	void getLock(String key);

	@Query(value = "select release_lock(:key)", nativeQuery = true)
	void releaseLock(String key);
}
