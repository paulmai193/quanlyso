/*
 * 
 */
package logia.quanlyso.service;

import io.github.jhipster.config.JHipsterProperties;
import logia.quanlyso.domain.User;
import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.util.Locale;

/**
 * Service for sending emails.
 * <p>
 * We use the @Async annotation to send emails asynchronously.
 * </p>
 *
 * @author Dai Mai
 */
@Service
public class MailService {

	/** The log. */
	private final Logger log = LoggerFactory.getLogger(MailService.class);

	/** The Constant USER. */
	private static final String USER = "user";

	/** The Constant BASE_URL. */
	private static final String BASE_URL = "baseUrl";

	/** The j hipster properties. */
	private final JHipsterProperties jHipsterProperties;

	/** The java mail sender. */
	private final JavaMailSender javaMailSender;

	/** The message source. */
	private final MessageSource messageSource;

	/** The template engine. */
	private final SpringTemplateEngine templateEngine;

	/**
	 * Instantiates a new mail service.
	 *
	 * @param jHipsterProperties
	 *            the j hipster properties
	 * @param javaMailSender
	 *            the java mail sender
	 * @param messageSource
	 *            the message source
	 * @param templateEngine
	 *            the template engine
	 */
	public MailService(JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender,
			MessageSource messageSource, SpringTemplateEngine templateEngine) {

		this.jHipsterProperties = jHipsterProperties;
		this.javaMailSender = javaMailSender;
		this.messageSource = messageSource;
		this.templateEngine = templateEngine;
	}

	/**
	 * Send email.
	 *
	 * @param to
	 *            the to
	 * @param subject
	 *            the subject
	 * @param content
	 *            the content
	 * @param isMultipart
	 *            the is multipart
	 * @param isHtml
	 *            the is html
	 */
	@Async
	public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
		this.log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}", isMultipart,
				isHtml, to, subject, content);

		// Prepare message using a Spring helper
		MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
		try {
			MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, CharEncoding.UTF_8);
			message.setTo(to);
			message.setFrom(this.jHipsterProperties.getMail().getFrom());
			message.setSubject(subject);
			message.setText(content, isHtml);
			this.javaMailSender.send(mimeMessage);
			this.log.debug("Sent email to User '{}'", to);
		} catch (Exception e) {
			this.log.warn("Email could not be sent to user '{}'", to, e);
		}
	}

	/**
	 * Send activation email.
	 *
	 * @param user
	 *            the user
	 */
	@Async
	public void sendActivationEmail(User user) {
		this.log.debug("Sending activation email to '{}'", user.getEmail());
		Locale locale = Locale.forLanguageTag(user.getLangKey());
		Context context = new Context(locale);
		context.setVariable(MailService.USER, user);
		context.setVariable(MailService.BASE_URL, this.jHipsterProperties.getMail().getBaseUrl());
		String content = this.templateEngine.process("activationEmail", context);
		String subject = this.messageSource.getMessage("email.activation.title", null, locale);
		this.sendEmail(user.getEmail(), subject, content, false, true);
	}

	/**
	 * Send creation email.
	 *
	 * @param user
	 *            the user
	 */
	@Async
	public void sendCreationEmail(User user) {
		this.log.debug("Sending creation email to '{}'", user.getEmail());
		Locale locale = Locale.forLanguageTag(user.getLangKey());
		Context context = new Context(locale);
		context.setVariable(MailService.USER, user);
		context.setVariable(MailService.BASE_URL, this.jHipsterProperties.getMail().getBaseUrl());
		String content = this.templateEngine.process("creationEmail", context);
		String subject = this.messageSource.getMessage("email.activation.title", null, locale);
		this.sendEmail(user.getEmail(), subject, content, false, true);
	}

	/**
	 * Send password reset mail.
	 *
	 * @param user
	 *            the user
	 */
	@Async
	public void sendPasswordResetMail(User user) {
		this.log.debug("Sending password reset email to '{}'", user.getEmail());
		Locale locale = Locale.forLanguageTag(user.getLangKey());
		Context context = new Context(locale);
		context.setVariable(MailService.USER, user);
		context.setVariable(MailService.BASE_URL, this.jHipsterProperties.getMail().getBaseUrl());
		String content = this.templateEngine.process("passwordResetEmail", context);
		String subject = this.messageSource.getMessage("email.reset.title", null, locale);
		this.sendEmail(user.getEmail(), subject, content, false, true);
	}
}
