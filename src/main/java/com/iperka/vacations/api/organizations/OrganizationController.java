package com.iperka.vacations.api.organizations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.print.attribute.standard.Media;

import com.iperka.vacations.api.helpers.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = { "/organizations/" }, produces = MediaType.APPLICATION_JSON_VALUE)
public class OrganizationController {

    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @GetMapping
    public ResponseEntity<Response<List<Organization>>> getAllOrganizations(Pageable pageable) {
        return Response.fromPage(HttpStatus.OK, this.organizationService.findAll(pageable)).build();
    }

    @GetMapping(value = "{uuid}")
    public ResponseEntity<Response<Organization>> getOrganizationById(@PathVariable("uuid") UUID uuid) {
        Optional<Organization> optional = organizationService.findByUUID(uuid);

        if (!optional.isPresent()) {
            return Response.<Organization>notFound("No organization found with given id.").build();
        }

        Response<Organization> response = new Response<>(HttpStatus.OK);
        response.setData(optional.get());

        return response.build();
    }

}
