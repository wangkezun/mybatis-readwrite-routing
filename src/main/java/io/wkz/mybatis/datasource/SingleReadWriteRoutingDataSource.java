package io.wkz.mybatis.datasource;

import io.wkz.mybatis.common.enums.ReadWriteLookupKeyEnums;

import java.util.HashMap;
import java.util.Map;

/**
 * 单主单从模式下的默认的实现方式，一般情况下直接使用该类实现数据源即可
 *
 * @author 王可尊
 * @since 1.0
 */
public final class SingleReadWriteRoutingDataSource extends AbstractReadWriteRoutingDataSource {

	/**
	 * 读库的DataSource
	 */
	private Object read;
	/**
	 * 写库的DataSource
	 */
	private Object write;

	public void setRead(Object read) {
		this.read = read;
	}

	public void setWrite(Object write) {
		this.write = write;
	}

	/**
	 * 把数据源配置到spring的管理器中，因为spring的数据源是用Map<Object,Object>存储的，所以这里以枚举的名称作为key，数据源作为value
	 */
	@Override
	public void afterPropertiesSet() {
		if (this.write == null) {
			throw new IllegalArgumentException("Property 'write' is required");
		}
		setDefaultTargetDataSource(write);
		Map<Object, Object> targetDataSources = new HashMap<>();
		targetDataSources.put(ReadWriteLookupKeyEnums.WRITE.name(), write);
		if (read != null) {
			targetDataSources.put(ReadWriteLookupKeyEnums.READ.name(), read);
		}
		setTargetDataSources(targetDataSources);
		super.afterPropertiesSet();
	}

	@Override
	protected Object determineCurrentReadLookupKey() {
		return ReadWriteLookupKeyEnums.READ.name();
	}
}
