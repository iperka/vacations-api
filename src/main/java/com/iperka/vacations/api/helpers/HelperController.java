package com.iperka.vacations.api.helpers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Helper controller to serve static files.
 * Even if its bad practice to do it this way...
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
@RestController
@RequestMapping(value = "/")
public class HelperController {

    @Value("classpath:static/oauth2-redirect.html")
    private Resource oauthRedirect;

    @Value("classpath:static/tea.html")
    private Resource tea;

    @Value("classpath:static/favicon.ico")
    private Resource favicon;
    
    @GetMapping(value = "/favicon.ico", produces = MediaType.ALL_VALUE)
    public byte[] getFavicon() throws IOException {
    return favicon.getInputStream().readAllBytes();
    }

    @GetMapping(value = "/oauth2-redirect.html", produces = MediaType.TEXT_HTML_VALUE)
    public byte[] getOauthRedirect() throws IOException {
        return oauthRedirect.getInputStream().readAllBytes();
    }

    @GetMapping(value = "/tea", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseStatus(value = HttpStatus.I_AM_A_TEAPOT)
    public byte[] getTea() throws IOException {
        return tea.getInputStream().readAllBytes();
    }
}
