package com.iperka.vacations.api.mail;

import javax.validation.constraints.Email;

/**
 * The {@link com.iperka.vacations.api.mail.MailService}
 * interface defines the basic mail service operations.
 * 
 * @author Michael Beutler
 * @version 0.0.1
 * @since 2021-12-31
 */
public interface MailService {
    /**
     * Sends a test message to given address.
     */
    void sendTestMail(@Email String to);
}
