package com.pygzx.zxtools.util;

import org.apache.commons.lang3.tuple.Triple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

public class MailUtil {
	private static final Logger LOGGER = LoggerFactory.getLogger(MailUtil.class);

	private static Map<String, Triple<Properties, Authenticator, String>> configMap = Collections.emptyMap();

	public static void init(Properties config) {
		Set<String> nameSet = new HashSet<>();
		Map<String, Triple<Properties, Authenticator, String>> map = new HashMap<>();
		config.forEach((k, v) -> {
			String name = ((String) k).split("\\.")[0];
			nameSet.add(name);
		});
		nameSet.forEach(name -> {
			String username = config.getProperty(name + ".username");
			String password = config.getProperty(name + ".password");
			String host = config.getProperty(name + ".host");
			String auth = config.getProperty(name + ".auth");
			Properties p = System.getProperties();
			p.put("mail.smtp.host", host);
			p.put("mail.smtp.auth", auth);
			map.put(name, Triple.of(p, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			}, username));
		});
		configMap = map;
	}

	public static boolean send(String name, String to, String subject, String text) {
		if (!configMap.containsKey(name)) {
			LOGGER.warn("No config: "+ name);
			return false;
		}
		Triple<Properties, Authenticator, String> triple = configMap.get(name);
		Session session = Session.getDefaultInstance(triple.getLeft(), triple.getMiddle());
		try {
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(triple.getRight()));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			message.setSubject(subject);
			message.setText(text);
			Transport.send(message);
			LOGGER.info(String.format("Send email: to=%s (Success)", to));
			return true;
		} catch (Exception e) {
			LOGGER.error(String.format("Send email: to=%s, message=%s", to, e.getMessage()), e);
		}
		return false;
	}
}
