package io.wkz.mybatis.common;

import io.wkz.mybatis.common.enums.ReadWriteLookupKeyEnums;
import io.wkz.mybatis.datasource.AbstractReadWriteRoutingDataSource;
import io.wkz.mybatis.interceptor.ReadWriteRoutingInterceptor;

/**
 * {@link ReadWriteRoutingInterceptor}拦截请求后
 * 将判断决定使用的{@link ReadWriteLookupKeyEnums}值写入{@link #holder}中
 * 供{@link AbstractReadWriteRoutingDataSource#determineCurrentLookupKey()}使用。
 * 确定当前请求所使用的数据源
 * @author 王可尊
 * @since 1.0
 */
public class ReadWriteRoutingLookupKeyHolder {
	private static final ThreadLocal<ReadWriteLookupKeyEnums> holder = new ThreadLocal<>();
	private ReadWriteRoutingLookupKeyHolder(){
	}
	public static void setLookupKey(ReadWriteLookupKeyEnums dataSource){
		holder.set(dataSource);
	}

	public static ReadWriteLookupKeyEnums getLookupKey(){
		return holder.get();
	}

	public static void clearLookupKey() {
		holder.remove();
	}
}
