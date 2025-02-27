package com.tcs.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Scope;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
//import org.springframework.web.servlet.LocaleResolver;
//import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

public class MessageConfig {

    // cargar los properties
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        //apunta los archivos llamados messages
        messageSource.setBasename("classpath:messages");
        // usar cuando tenemos algun encoding como tildes o caracteres
        //messageSource.setDefaultEncoding("UTF-8");
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
    // singleton = una sola instancia
    // prototype = varias instancias
//    @Bean
//    @Scope("prototype")
//    public LocaleResolver localeResolver() {
//        SessionLocaleResolver localeResolver = new SessionLocaleResolver();
//        localeResolver.setDefaultLocale(Locale.ROOT);
//        //localeResolver.setDefaultLocale(Locale.ENGLISH);
//        //localeResolver.setDefaultLocale(Locale.FRANCE);
//        return localeResolver;
//    }
}
