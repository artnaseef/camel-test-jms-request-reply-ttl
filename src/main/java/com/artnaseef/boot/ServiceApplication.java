package com.artnaseef.boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.EndpointMBeanExportAutoConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.JmsAutoConfiguration;
import org.springframework.boot.autoconfigure.jmx.JmxAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;


@SpringBootApplication
@ImportResource("classpath*:META-INF/spring/camel-context.xml")
@EnableAutoConfiguration(exclude = {JmxAutoConfiguration.class,
                                    EndpointMBeanExportAutoConfiguration.class,
                                    JmsAutoConfiguration.class})
public class ServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}
}
