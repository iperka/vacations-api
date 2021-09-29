package com.iperka.vacations.api.organizations.dto;

import javax.validation.constraints.NotNull;

import com.iperka.vacations.api.helpers.DTO;
import com.iperka.vacations.api.organizations.Organization;

public class OrganizationDTO implements DTO<Organization> {

    @NotNull
    private String name;

    @NotNull
    private boolean enabled;

    @Override
    public Organization toObject() {
        final Organization organization = new Organization();

        organization.setName(this.name);
        organization.setEnabled(this.enabled);

        return organization;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(final boolean enabled) {
        this.enabled = enabled;
    }

}
