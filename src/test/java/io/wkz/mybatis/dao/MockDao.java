package io.wkz.mybatis.dao;

import io.wkz.mybatis.dto.MockDto;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

/**
 * @author 王可尊
 * @since 1.0
 */
public interface MockDao {
	@Select("select * from mock_table where id= #{id}")
	MockDto getById(int id);
	@Insert("insert into mock_table value(#{name})")
	void addOne(MockDto dto);
}
