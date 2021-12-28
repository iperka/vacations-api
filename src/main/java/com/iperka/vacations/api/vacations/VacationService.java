package com.iperka.vacations.api.vacations;

import java.util.Optional;
import java.util.UUID;

import com.iperka.vacations.api.vacations.dto.VacationDTO;
import com.iperka.vacations.api.vacations.exceptions.VacationAlreadyExists;
import com.iperka.vacations.api.vacations.exceptions.VacationNotFound;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The {@link com.iperka.vacations.api.vacations.VacationService}
 * interface defines the methods that will interact with the
 * {@link com.iperka.vacations.api.organisations.OrganisationRepository} model.
 * 
 * @author Michael Beutler
 * @version 0.0.8
 * @since 2021-09-29
 */
public interface VacationService {
    /**
     * Retrieves all vacations as {@link org.springframework.data.domain.Page}
     * object. Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * Required scopes: vacations:all:read || vacations:all:write
     * 
     * @return Page object.
     */
    public Page<Vacation> findAll(Pageable pageable);

    /**
     * Retrieves all vacations as {@link org.springframework.data.domain.Page}
     * object.
     * 
     * Required scopes: vacations:read || vacations:write
     * 
     * @return Page object.
     */
    public Page<Vacation> findAllByOwner(Pageable pageable, String owner);

    /**
     * Retrieves all vacations matching the given name as
     * {@link org.springframework.data.domain.Page} object. Bare in mind that these
     * method should be explicit to administrative roles.
     * 
     * Required scopes: vacations:all:read || vacations:all:write
     * 
     * @return Page object.
     */
    public Page<Vacation> findByNameContainingIgnoreCase(Pageable pageable, String name);

    /**
     * Retrieves all vacations matching the given name as
     * {@link org.springframework.data.domain.Page} object.
     * 
     * Required scopes: vacations:read || vacations:write
     * 
     * @return Page object.
     */
    public Page<Vacation> findByNameContainingIgnoreCaseAndOwner(Pageable pageable, String name, String owner);

    /**
     * Returns the vacation object matching the given uuid as
     * {@link java.util.Optional}. Bare in mind that these
     * method should be explicit to administrative roles.
     * 
     * Required scopes: vacations:all:read || vacations:all:write
     * 
     * @param uuid Objects uuid.
     * @return Optional
     */
    public Vacation findByUuid(UUID uuid) throws VacationNotFound;

    /**
     * Returns the vacation object matching the given uuid as
     * {@link java.util.Optional}.
     * 
     * Required scopes: vacations:read || vacations:write
     * 
     * @param uuid Objects uuid.
     * @return Optional
     */
    public Vacation findByUuidAndOwner(UUID uuid, String owner) throws VacationNotFound;

    /**
     * Returns the vacation object matching the given name as
     * {@link java.util.Optional}. Bare in mind that these
     * method should be explicit to administrative roles.
     * 
     * Required scopes: vacations:all:read || vacations:all:write
     * 
     * @param name Objects name.
     * @return Optional
     */
    public Optional<Vacation> findByNameIgnoreCase(String name);

    /**
     * Returns the vacation object matching the given name as
     * {@link java.util.Optional}.
     * Required scopes: vacations:read || vacations:write
     * 
     * @param name Objects name.
     * @return Optional
     */
    public Optional<Vacation> findByNameIgnoreCaseAndOwner(String name, String owner);

    /**
     * Saves a given object to database. This will create a new one if it doesn't
     * exists.
     * 
     * Required scopes: vacations:write
     * 
     * @param vacation Vacation object.
     * @return created vacation
     */
    public Vacation create(Vacation vacation) throws VacationAlreadyExists;

    /**
     * Deletes the vacation object matching the given uuid. Bare in mind that
     * these method should be explicit to administrative roles.
     * 
     * Required scopes: vacations:all:write
     * 
     * @param uuid Objects uuid.
     */
    public void deleteByUuid(UUID uuid) throws VacationNotFound;

    /**
     * Deletes the vacation object matching the given uuid.
     * 
     * Required scopes: vacations:write
     * 
     * @param uuid Objects uuid.
     */
    public void deleteByUuidAndOwner(UUID uuid, String owner) throws VacationNotFound;

    /**
     * Deletes the vacation object matching the given uuid. Bare in mind that
     * these method should be explicit to administrative roles.
     * 
     * Required scopes: vacations:all:write
     * 
     * @param uuid Objects uuid.
     */
    public Vacation updateByUuid(UUID uuid, VacationDTO vacationDTO) throws VacationNotFound, VacationAlreadyExists;

    /**
     * Deletes the vacation object matching the given uuid.
     * 
     * Required scopes: vacations:write
     * 
     * @param uuid Objects uuid.
     */
    public Vacation updateByUuidAndOwner(UUID uuid, String owner, VacationDTO vacationDTO) throws VacationNotFound, VacationAlreadyExists;
}
