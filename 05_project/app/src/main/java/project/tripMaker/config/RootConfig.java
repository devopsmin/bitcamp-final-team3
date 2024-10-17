package project.tripMaker.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@ComponentScan(
    value = "project.tripMaker",
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ANNOTATION,
        value = Controller.class
    )
)

@PropertySource({
    "classpath:config/jdbc-local.properties"})
    // , "file:${user.home}/config/ncp.properties"})
@EnableTransactionManagement // 스프링 프렘워크야, @Transactional 메서드가 붙은 클래스를 만나면 Proxy 클래스를 자동 생성하라!
@MapperScan("project.tripMaker.dao")
public class RootConfig {

  ApplicationContext appCtx;

  public RootConfig(ApplicationContext appCtx) {
    this.appCtx = appCtx;
  }

  @Bean
  public DataSource dataSource(
      @Value("${jdbc.driver}") String jdbcDriver,
      @Value("${jdbc.url}") String jdbcUrl,
      @Value("${jdbc.username}") String jdbcUsername,
      @Value("${jdbc.password}") String jdbcPassword) {
    DriverManagerDataSource ds = new DriverManagerDataSource();
    ds.setDriverClassName(jdbcDriver);
    ds.setUrl(jdbcUrl);
    ds.setUsername(jdbcUsername);
    ds.setPassword(jdbcPassword);
    return ds;
  }

  @Bean
  public PlatformTransactionManager transactionManager(DataSource ds) {
    return new DataSourceTransactionManager(ds);
  }

  @Bean
  public SqlSessionFactory sqlSessionFactory(DataSource ds) throws Exception {
    SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
    factoryBean.setDataSource(ds);
    factoryBean.setTypeAliasesPackage("project.tripMaker.vo");
    factoryBean.setMapperLocations(appCtx.getResources("classpath:mappers/*Mapper.xml"));
    return factoryBean.getObject();
  }
}
