package com.example.demo;

import org.apache.catalina.servlets.WebdavServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import java.io.File;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> webServerFactoryCustomizer() {
        return factory -> factory.setDocumentRoot(new File("/tmp"));
    }

    @Bean
    // https://github.com/goja/webdav-server/
    public ServletRegistrationBean<WebdavServlet> servletRegistrationBean() {
        ServletRegistrationBean<WebdavServlet> bean = new ServletRegistrationBean<>(new WebdavServlet(), "/webdav/*");
        bean.setLoadOnStartup(1);
        bean.addInitParameter("listings", "true");
        bean.addInitParameter("readonly", "false");
        return bean;
    }

    @Configuration
    public static class RequestLoggingFilterConfig {

        @Bean
        public CommonsRequestLoggingFilter logFilter() {
            CommonsRequestLoggingFilter filter = new CommonsRequestLoggingFilter();
            filter.setIncludeQueryString(true);
            filter.setIncludePayload(true);
            filter.setMaxPayloadLength(10000);
            filter.setIncludeHeaders(true);
            filter.setAfterMessagePrefix("REQUEST DATA : ");
            return filter;
        }
    }
}
