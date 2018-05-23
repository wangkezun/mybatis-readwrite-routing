package io.wkz.mybatis.interceptor;

import io.wkz.mybatis.common.ReadWriteRoutingLookupKeyHolder;
import io.wkz.mybatis.common.enums.ReadWriteLookupKeyEnums;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.keygen.SelectKeyGenerator;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * MyBatis的拦截器实现，此拦截器拦截update与query方法，并判断是否应该拦截
 *
 * @author 王可尊
 * @since 1.0
 */
@Intercepts({
		@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
		@Signature(type = Executor.class,
				method = "query",
				args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})
})
public class ReadWriteRoutingInterceptor implements Interceptor {
	private static final Logger log = LoggerFactory.getLogger(ReadWriteRoutingInterceptor.class);
	private static final String REGEX = ".*insert\\u0020.*|.*delete\\u0020.*|.*update\\u0020.*";
	//缓存已经存在的请求
	private static final Map<String, ReadWriteLookupKeyEnums> cacheMap = new ConcurrentHashMap<>();

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		// 判断是否是数据库事务
		boolean synchronizationActive = TransactionSynchronizationManager.isSynchronizationActive();
		if (log.isDebugEnabled()) {
			log.debug("Is this request in transaction? {}", synchronizationActive);
		}
		//当不为事务时，解析SQL来判断此次请求应该路由到哪个数据源中，并将lookupKey写入ThreadLocal供读写分离数据源使用
		if (!synchronizationActive) {
			Object[] objects = invocation.getArgs();
			MappedStatement ms = (MappedStatement) objects[0];
			String statementId = ms.getId();
			ReadWriteLookupKeyEnums lookupKey = cacheMap.get(statementId);
			if (log.isDebugEnabled()) {
				log.debug("This request not in transaction. Try to get lookupKey from cacheMap. {}", lookupKey);
			}
			if (lookupKey == null) {
				//读方法
				if (ms.getSqlCommandType().equals(SqlCommandType.SELECT)) {
					//!selectKey 为自增id查询主键(SELECT LAST_INSERT_ID() )方法，使用主库
					if (statementId.contains(SelectKeyGenerator.SELECT_KEY_SUFFIX)) {
						lookupKey = ReadWriteLookupKeyEnums.WRITE;
					} else {
						BoundSql boundSql = ms.getSqlSource().getBoundSql(objects[1]);
						String sql = boundSql.getSql().toLowerCase(Locale.CHINA).replaceAll("[\\t\\n\\r]", " ");
						if (sql.matches(REGEX)) {
							lookupKey = ReadWriteLookupKeyEnums.WRITE;
						} else {
							lookupKey = ReadWriteLookupKeyEnums.READ;
						}
					}
				} else {
					// 写方法
					lookupKey = ReadWriteLookupKeyEnums.WRITE;
				}
				if (log.isDebugEnabled()) {
					log.debug("statementId:{} lookupKey:{}  SqlCommandType [{}]",
							  statementId,
							  lookupKey.name(),
							  ms.getSqlCommandType().name());
				}
				cacheMap.put(statementId, lookupKey);
			}
			ReadWriteRoutingLookupKeyHolder.setLookupKey(lookupKey);
		}
		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		if (target instanceof Executor) {
			return Plugin.wrap(target, this);
		} else {
			return target;
		}
	}

	@Override
	public void setProperties(Properties properties) {

	}
}
