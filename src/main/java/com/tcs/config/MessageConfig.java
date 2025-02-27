package com.tcs.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class MessageConfig {

    // cargar los properties
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        //apunta los archivos llamados messages
        messageSource.setBasename("classpath:messages");
        // usar cuando tenemos algun encoding como tildes o caracteres
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    // Para resolver las variables en los messages.properties
    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

    //Establecer un default locale
//     @Bean
//    public LocaleContextResolver localeContextResolver() {
//        AcceptHeaderLocaleContextResolver resolver = new AcceptHeaderLocaleContextResolver();
//        resolver.setDefaultLocale(Locale.US); // Idioma por defecto (inglés)
//        return resolver;
//    }
}
