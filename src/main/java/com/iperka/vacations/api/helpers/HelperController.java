package com.iperka.vacations.api.helpers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Helper controller to serve static files.
 * Even if its bad practice...
 * 
 * @author Michael Beutler
 * @version 0.0.1
 * @since 2021-12-19
 */
@RestController
@RequestMapping(value = "/")
public class HelperController {

    @Value("classpath:static/oauth2-redirect.html")
    private Resource oauthRedirect;

    @Value("classpath:static/favicon.ico")
    private Resource favicon;

    @GetMapping(value = "/favicon.ico", produces = MediaType.ALL_VALUE)
    public byte[] getOauthRedirect() throws IOException {
        return favicon.getInputStream().readAllBytes();
    }

}
