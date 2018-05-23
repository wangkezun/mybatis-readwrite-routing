package io.wkz.mybatis.config;

import org.springframework.context.annotation.*;

/**
 * @author 王可尊
 * @since 1.0
 */
@Configuration
@ImportResource("classpath:appcontext-mybatis.xml")
@ComponentScan(basePackages = {"io.wkz"})
public class RootConfig {

}
