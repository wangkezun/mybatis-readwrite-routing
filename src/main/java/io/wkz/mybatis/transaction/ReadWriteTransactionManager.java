package io.wkz.mybatis.transaction;

import io.wkz.mybatis.common.ReadWriteRoutingLookupKeyHolder;
import io.wkz.mybatis.common.enums.ReadWriteLookupKeyEnums;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

/**
 * @author 王可尊
 * @since 1.0
 */
public class ReadWriteTransactionManager extends DataSourceTransactionManager {
	private static final Logger log = LoggerFactory.getLogger(ReadWriteTransactionManager.class);
	private static final long serialVersionUID = 3421751740623395548L;

	@Override
	protected void doBegin(Object transaction, TransactionDefinition definition) {
		boolean readOnly = definition.isReadOnly();
		if (readOnly) {
			log.debug("数据库事务管理器 读");
			ReadWriteRoutingLookupKeyHolder.setLookupKey(ReadWriteLookupKeyEnums.READ);
		} else {
			log.debug("数据库事务管理器 写");
			ReadWriteRoutingLookupKeyHolder.setLookupKey(ReadWriteLookupKeyEnums.WRITE);
		}
		super.doBegin(transaction, definition);
	}

	@Override
	protected void doCleanupAfterCompletion(Object transaction) {
		super.doCleanupAfterCompletion(transaction);
		ReadWriteRoutingLookupKeyHolder.clearLookupKey();
	}
}
