package io.wkz.mybatis.testCases;

import io.wkz.mybatis.config.RootConfig;
import io.wkz.mybatis.dto.MockDto;
import io.wkz.mybatis.service.MockService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author 王可尊
 * @since 1.0
 */
@ExtendWith(SpringExtension.class)
@SpringJUnitConfig(RootConfig.class)
class BaseTest {
	@Autowired
	private MockService mockService;

	@Test
	void test() {
		mockService.addOne();
	}

	@Test
	void readInTransactionTest() {
		final MockDto mockDto = mockService.readInTransaction();
		assertNotNull(mockDto);
		assertEquals(mockDto.getName(), "write");
	}

	@Test
	void readNotInTransactionTest() {
		final MockDto mockDto = mockService.readNotInTransaction();
		assertNotNull(mockDto);
		assertEquals(mockDto.getName(), "read");
	}

	@Test
	void readInReadOnlyTransactionTest() {
		final MockDto mockDto = mockService.readInReadOnlyTransaction();
		assertNotNull(mockDto);
		assertEquals(mockDto.getName(), "read");
	}
	@Test
	void readInnerTransactionReadOnlyTest(){
		final MockDto mockDto = mockService.readInReadOnlyTransaction();
		assertNotNull(mockDto);
		assertEquals(mockDto.getName(), "read");
	}
	@Test
	void readOuterTransactionReadOnlyTest(){
		final MockDto mockDto = mockService.readInReadOnlyTransaction();
		assertNotNull(mockDto);
		assertEquals(mockDto.getName(), "read");
	}
}
