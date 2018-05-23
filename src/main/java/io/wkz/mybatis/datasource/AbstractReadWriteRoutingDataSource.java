package io.wkz.mybatis.datasource;

import io.wkz.mybatis.common.ReadWriteRoutingLookupKeyHolder;
import io.wkz.mybatis.common.enums.ReadWriteLookupKeyEnums;
import org.apache.ibatis.plugin.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 读写分离机制的数据源类<br/>
 * 重写了{@link #determineCurrentLookupKey()}方法切换数据源.
 * 每次请求时，由{@link Interceptor}判断{@link ReadWriteLookupKeyEnums}
 *
 * @author 王可尊
 * @since 1.0
 */
public abstract class AbstractReadWriteRoutingDataSource extends AbstractRoutingDataSource {
	private static final Logger log = LoggerFactory.getLogger(AbstractReadWriteRoutingDataSource.class);

	@Override
	protected Object determineCurrentLookupKey() {

		final ReadWriteLookupKeyEnums lookupKey = ReadWriteRoutingLookupKeyHolder.getLookupKey();
		if (lookupKey != null) {
			if (log.isDebugEnabled()) {
				log.debug("lookupKey exist!{}",lookupKey);
			}
			//当为查询读库时，调用获取读库的LookupKey接口。
			if(lookupKey == ReadWriteLookupKeyEnums.READ) {
				return determineCurrentReadLookupKey();
			} else {
				return ReadWriteLookupKeyEnums.WRITE.name();
			}
		}
		//默认情况下返回主库的lookupKey
		return ReadWriteLookupKeyEnums.WRITE.name();
	}

	/**
	 * 此方法用于获取ReadDataSource的LookupKey。
	 * 因为读库可能有多个，因此当数据库为一写多读的场景时，为了防止所有读都压在一个读库上，子类需要实现此方法来返回一个ReadLookupKey
	 *
	 * @return lookupKey
	 */
	protected abstract Object determineCurrentReadLookupKey();
}
