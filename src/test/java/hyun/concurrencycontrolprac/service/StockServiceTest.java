package hyun.concurrencycontrolprac.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
		Stock stock = new Stock(1L, 100L);
		stockRepository.save(stock);
	}

	@AfterEach
	public void delete() {
		stockRepository.deleteAll();
	}

	@Test
	@DisplayName("상품 단일 주문")
	public void decrease() {
		// given
		Stock savedStock = stockRepository.findAll().get(0);

		// when
		stockService.decrease(savedStock.getId(), 1L);

		// then
		Stock result = stockRepository.findAll().get(0);
		assertThat(result.getQuantity()).isEqualTo(99L);
	}

	@Test
	@DisplayName("동시에 100개 주문, 일반적인 동시성 이슈")
	public void decreaseAtSameTime() throws InterruptedException {

		// given
		Stock savedStock = stockRepository.findAll().get(0);
		int threadCount = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		// when
		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					stockService.decrease(savedStock.getId(), 1L);
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();

		// then
		Stock result = stockRepository.findAll().get(0);
		assertThat(result.getQuantity()).isNotEqualTo(0L);
	}

	@Test
	@DisplayName("동시에 100개 주문, 비관적 잠금(Pessimistic Lock) 적용")
	public void pessimisticDecreaseAtSameTime() throws InterruptedException {

		// given
		Stock savedStock = stockRepository.findAll().get(0);
		int threadCount = 100;
		ExecutorService executorService = Executors.newFixedThreadPool(32);
		CountDownLatch latch = new CountDownLatch(threadCount);

		// when
		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					stockService.pessimisticDecrease(savedStock.getId(), 1L);
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();

		// then
		Stock result = stockRepository.findAll().get(0);
		assertThat(result.getQuantity()).isEqualTo(0L);
	}
}