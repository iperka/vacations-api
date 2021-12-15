package com.iperka.vacations.api.organizations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import com.iperka.vacations.api.helpers.Response;
import com.iperka.vacations.api.organizations.dto.OrganizationDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = { "/organizations" }, produces = MediaType.APPLICATION_JSON_VALUE)
public class OrganizationController {
    private final Logger logger = LoggerFactory.getLogger(OrganizationController.class);

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping
    public ResponseEntity<Response<List<Organization>>> getAllOrganizations(
    // @formatter:off
        Pageable pageable,
        @RequestParam(required = false) String name
    // @formatter:on
    ) {
        if (StringUtils.hasText(name)) {
            return Response
                    .fromPage(HttpStatus.OK, this.organizationService.findByNameContainingIgnoreCase(pageable, name),
                            String.format("name=*%s*", name))
                    .build();
        }

        return Response.fromPage(HttpStatus.OK, this.organizationService.findAll(pageable)).build();
    }

    @GetMapping(value = "/{uuid}")
    public ResponseEntity<Response<Organization>> getOrganizationById(@PathVariable("uuid") UUID uuid) {
        Optional<Organization> optional = organizationService.findByUUID(uuid);

        if (!optional.isPresent()) {
            return Response.<Organization>notFound("No organization found with given id.").build();
        }

        Response<Organization> response = new Response<>(HttpStatus.OK);
        response.setData(optional.get());

        return response.build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Response<Organization>> createOrganisation(Authentication authentication,
            @Valid @RequestBody(required = true) OrganizationDTO organizationDTO) {
        Organization organization = organizationDTO.toObject();
        organization.setOwner(((Jwt) authentication.getPrincipal()).getSubject());

        Response<Organization> response = new Response<Organization>(HttpStatus.CREATED);
        response.setData(this.organizationService.create(organization));

        return response.build();
    }

}
