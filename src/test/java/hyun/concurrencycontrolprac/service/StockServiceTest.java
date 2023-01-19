package hyun.concurrencycontrolprac.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import hyun.concurrencycontrolprac.entity.Stock;
import hyun.concurrencycontrolprac.repository.StockRepository;

@SpringBootTest
class StockServiceTest {

	@Autowired
	private StockService stockService;

	@Autowired
	private StockRepository stockRepository;

	@BeforeEach
	public void insert() {
		// 테스트 시작 전 상품 등록
		Stock stock = new Stock(1L, 100L);
		stockRepository.save(stock);
	}

	@AfterEach
	void delete() {
		stockRepository.deleteAll();
	}

	@Test
	@DisplayName("재고 감소")
	void decrease() {
		// given & when
		stockService.decrease(1L, 1L);

		// then
		Stock result = stockRepository.findById(1L).orElseThrow();
		assertThat(result.getQuantity()).isEqualTo(99L);
	}
}