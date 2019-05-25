package com.sxs.web.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.jndi.JndiTemplate;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * @ClassName: MybatisConfiguration
 * @Description: java类作用描述
 * @Author: 尚先生
 * @CreateDate: 2019/5/24 16:56
 * @Version: 1.0
 */
@Configuration
@MapperScan("com.sxs.web.mapper")
public class MybatisConfiguration {
    @Bean
    public DataSource dataSource() throws ClassNotFoundException, NamingException {
        // 方式一
//        Context initContext = new InitialContext();
//        Context envContext  = (Context)initContext.lookup("java:/comp/env");
//        DataSource dataSource = (DataSource)envContext.lookup("jdbc/sxsDB");

        // 方式二
        JndiObjectFactoryBean factoryBean = new JndiObjectFactoryBean();
        JndiTemplate jndiTemplate = factoryBean.getJndiTemplate();
        DataSource dataSource = jndiTemplate.lookup("java:/comp/env/jdbc/sxsDB", DataSource.class);
        return dataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        sqlSessionFactoryBean.setTypeAliasesPackage("com.sxs.web.model");
        //   "classpath*:sqlmap/*-mapper.xml"
        sqlSessionFactoryBean.setMapperLocations(new Resource[]{});
        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
