package com.example.bookstore.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.io.IOException;
import java.time.Clock;

@Configuration
public class AppConfig {
  @Bean
  public Clock clock() {
    return Clock.systemUTC();
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
            .registerModule(new JavaTimeModule());
//            .setDateFormat(new SimpleDateFormat())
  }


  @Bean
  public LocalValidatorFactoryBean validator(MessageSource messageSource) {
    LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
    bean.setValidationMessageSource(messageSource);
    return bean;
  }

  @Bean
  public MessageSource messageSource() throws IOException {
    ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
    messageSource.setDefaultEncoding("UTF-8");
    YamlPropertiesFactoryBean bean = new YamlPropertiesFactoryBean();
    bean.setResources(new ClassPathResource("messages.yml"));
    messageSource.setCommonMessages(bean.getObject());
    return messageSource;
  }
}
