package io.wkz.mybatis.service;

import io.wkz.mybatis.dao.MockDao;
import io.wkz.mybatis.dto.MockDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 王可尊
 * @since 1.0
 */
@Service
public class MockServiceImpl implements MockService {
	@Autowired
	private MockDao mockDao;

	@Override
	public MockDto getById(int id) {
		return mockDao.getById(id);
	}

	@Override
	public void addOne() {
		final MockDto mockDto = new MockDto();
		mockDto.setId(1);
		mockDto.setName("test");
		mockDao.addOne(mockDto);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public MockDto readInTransaction() {
		return this.getById(1);
	}

	@Override
	public MockDto readNotInTransaction() {
		return this.getById(1);
	}

	@Override
	@Transactional(readOnly = true,propagation = Propagation.REQUIRES_NEW)
	public MockDto readInReadOnlyTransaction() {
		return this.getById(1);
	}

	@Override
	@Transactional
	public MockDto readInnerTransactionReadOnly() {
		return this.readInReadOnlyTransaction();
	}

	@Override
	@Transactional(readOnly = true)
	public MockDto readOuterTransactionReadOnly() {
		return readInTransaction();
	}
}
