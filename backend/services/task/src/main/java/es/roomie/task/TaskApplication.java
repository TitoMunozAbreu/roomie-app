package es.roomie.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@SpringBootApplication
public class TaskApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskApplication.class, args);
	}

	/**
	 * Bean definition for localeResolver.
	 * This method configures the locale resolver to use session-based locale management.
	 *
	 * @return A LocaleResolver configured for session management with a default locale
	 */
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(Locale.ENGLISH); // change this
		return localeResolver;
	}

	/**
	 * Bean definition for messageSource.
	 * This method sets up a message source for internationalization,
	 * loading messages from a resource bundle.
	 *
	 * @return A ResourceBundleMessageSource configured with the specified basename and encoding
	 */
	@Bean
	public ResourceBundleMessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("ValidationMessages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
}
