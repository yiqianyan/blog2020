package com.test.provider;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.test.provider"})
public class SpringConfig {
    //可配置bean
}
