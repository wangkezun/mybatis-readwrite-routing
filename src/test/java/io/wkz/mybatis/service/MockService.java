package io.wkz.mybatis.service;

import io.wkz.mybatis.dto.MockDto;

/**
 * @author 王可尊
 * @since 1.0
 */
public interface MockService {
	MockDto getById(int id);
	void addOne();
	MockDto readInTransaction();
	MockDto readNotInTransaction();
	MockDto readInReadOnlyTransaction();
	MockDto readInnerTransactionReadOnly();
	MockDto readOuterTransactionReadOnly();
}
