package com.iperka.vacations.api.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;

import com.iperka.vacations.api.organizations.Organization;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HelpersTest {
    void shouldReturnFalseIfOwnerIsNotEqual() {
        Organization organization = new Organization();
        UUID uuid = UUID.randomUUID();
        assertEquals(false, Helpers.isOwner(organization, uuid.toString()));
    }

    void shouldReturnTrueIfOwnerIsEqual() {
        Organization organization = new Organization();
        assertEquals(false, Helpers.isOwner(organization, organization.getOwner().toString()));
    }
}
