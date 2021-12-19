package com.iperka.vacations.api.helpers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping(value = "/")
public class HelperController {

    @Value("classpath:static/oauth2-redirect.html")
    private Resource oauthRedirect;

    @GetMapping(value = "oauth2-redirect.html", produces = MediaType.TEXT_HTML_VALUE)
    public byte[] getOauthRedirect() throws IOException {
        return oauthRedirect.getInputStream().readAllBytes();
    }

}
