package cn.itcast.dtx.seatademo.bank1.config;

import com.alibaba.druid.pool.DruidDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfiguration {


    private final ApplicationContext applicationContext;

    public DatabaseConfiguration(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }


    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.ds0")
    public DruidDataSource ds0() {
        return new DruidDataSource();
    }

    //Seata的RM通过DataSourceProxy才能在业务代码的事务提交时，通过这个切
    //入点，与TC进行通信交互、记录undo_log等
    @Primary
    @Bean
    public DataSource dataSource(DruidDataSource ds0)  {
        return new DataSourceProxy(ds0);
    }



}
