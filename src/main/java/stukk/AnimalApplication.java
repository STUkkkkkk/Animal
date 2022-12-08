package stukk;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author wenli
 * @create 2022-08-29 20:09
 */
@Slf4j
@SpringBootApplication
@ServletComponentScan
// 开启事务注解功能
@EnableTransactionManagement
// 开启Spring Cache缓存注解功能
@EnableCaching
public class AnimalApplication {
    public static void main(String[] args) {
        SpringApplication.run(AnimalApplication.class, args);
        log.info("项目启动成功！");
    }
}
