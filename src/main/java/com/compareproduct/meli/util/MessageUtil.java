package com.compareproduct.meli.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

@Component
public class MessageUtil {

    private static final Logger log = LoggerFactory.getLogger(MessageUtil.class);
    private static final String BUNDLE_NAME = "messages";

    public static String getMessage(String key) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, Locale.ROOT);
            return bundle.getString(key);
        } catch (MissingResourceException e) {
            log.warn("Message not found for key: {}", key, e);
            return key; // Retorna a chave se não encontrar a mensagem
        } catch (IllegalArgumentException e) {
            log.error("Invalid argument for message key: {}", key, e);
            return key;
        } catch (Exception e) {
            log.error("Unexpected error loading message for key: {}", key, e);
            return key;
        }
    }
}