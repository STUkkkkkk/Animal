package stukk.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

/**
 * @author wenli
 * @create 2022-12-05 17:31
 */
@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {

    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("Knife4j 接口文档")
                        .description("# 宠物平台接口文档详情")
                        .termsOfServiceUrl("http://127.0.0.1:8080/doc.html")
                        .contact("717@qq.com")
                        .version("1.0")
                        .build())
                // 分组名称
                .groupName("717版本")
                .select()
                // 这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.basePackage("stukk.controller"))
                .paths(PathSelectors.any())
                .build();
    }
}
