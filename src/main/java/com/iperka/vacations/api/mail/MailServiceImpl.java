package com.iperka.vacations.api.mail;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.validation.constraints.Email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * The {@link com.iperka.vacations.api.mail.MailServiceImpl} implements
 * {@link com.iperka.vacations.api.mail.MailService}.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 2021-12-31
 */
@Service
@Slf4j
public class MailServiceImpl implements MailService {
    @Value("${mail.from.address}")
    private String fromAddress;

    @Value("${mail.from.name}")
    private String fromName;

    @Autowired
    private JavaMailSender javaMailSender;

    public static int noOfQuickServiceThreads = 20;

    /**
     * this statement create a thread pool of twenty threads
     * here we are assigning send mail task using ScheduledExecutorService.submit();
     */
    private final ScheduledExecutorService quickService = Executors.newScheduledThreadPool(noOfQuickServiceThreads);

    /**
     * Sends a test message to given address. In a separate thread.
     */
    @Override
    public void sendTestMail(@Email final String to) {
        log.info("Sending test email to <" + to + ">...");
        final SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(String.format("%s <%s>", fromName, fromAddress));
        message.setTo(to);
        message.setSubject("Vacations Test Mail");
        message.setText("This message has been sent by Vacation API.");

        quickService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    javaMailSender.send(message);
                    log.info("Mail sent.");
                } catch (final Exception e) {
                    log.error("Exception occur while send a mail: ", e);
                }
            }
        });
    }
}
