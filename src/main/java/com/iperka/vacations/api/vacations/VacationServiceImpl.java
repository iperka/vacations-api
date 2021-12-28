package com.iperka.vacations.api.vacations;

import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import com.iperka.vacations.api.vacations.dto.VacationDTO;
import com.iperka.vacations.api.vacations.exceptions.VacationNotFound;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * The
 * {@link com.iperka.vacations.api.vacations.VacationServiceImpl}
 * class implements the
 * {@link com.iperka.vacations.api.vacations.VacationService} interface
 * and is used to manage the vacation.
 * 
 * @author Michael Beutler
 * @version 0.0.1
 * @since 2021-12-28
 */
@Service
@Slf4j
public class VacationServiceImpl implements VacationService {

    private VacationRepository vacationRepository;

    public VacationServiceImpl(VacationRepository vacationRepository) {
        this.vacationRepository = vacationRepository;
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_vacations:all:read')")
    public Page<Vacation> findAll(Pageable pageable) {
        log.debug("findAll called");
        return this.vacationRepository.findAll(pageable);
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_vacations:all:read')")
    public Page<Vacation> findByNameContainingIgnoreCase(Pageable pageable, String name) {
        log.debug("findAllByName called");
        return this.vacationRepository.findByNameContainingIgnoreCase(pageable, name);
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_vacations:all:read')")
    public Vacation findByUuid(UUID uuid) throws VacationNotFound {
        log.debug("findByUUID called");
        return this.vacationRepository.findByUuid(uuid).orElseThrow(VacationNotFound::new);
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_vacations:write')")
    public Vacation create(Vacation vacation) {
        log.debug("create called");
        return this.vacationRepository.save(vacation);
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_vacations:all:read')")
    public Optional<Vacation> findByNameIgnoreCase(String name) {
        log.debug("findByNameIgnoreCase called");
        return this.vacationRepository.findByNameIgnoreCase(name);
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_vacations:all:write')")
    @Transactional
    public void deleteByUuid(UUID uuid) {
        this.vacationRepository.deleteByUuid(uuid);
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_vacations:read')")
    public Page<Vacation> findAllByOwner(Pageable pageable, String owner) {
        return this.vacationRepository.findAllByOwner(pageable, owner);
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_vacations:read')")
    public Page<Vacation> findByNameContainingIgnoreCaseAndOwner(Pageable pageable, String name, String owner) {
        return this.vacationRepository.findByNameContainingIgnoreCaseAndOwner(pageable, name, owner);
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_vacations:read')")
    public Vacation findByUuidAndOwner(UUID uuid, String owner) throws VacationNotFound {
        return this.vacationRepository.findByUuidAndOwner(uuid, owner)
                .orElseThrow(VacationNotFound::new);
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_vacations:read')")
    public Optional<Vacation> findByNameIgnoreCaseAndOwner(String name, String owner) {
        return this.vacationRepository.findByNameIgnoreCaseAndOwner(name, owner);
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_vacations:write')")
    @Transactional
    public void deleteByUuidAndOwner(UUID uuid, String owner) {
        this.vacationRepository.deleteByUuidAndOwner(uuid, owner);
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_vacations:all:write')")
    public Vacation updateByUuid(UUID uuid, VacationDTO vacationDTO)
            throws VacationNotFound {
        Vacation vacation = this.vacationRepository.findByUuid(uuid).orElseThrow(VacationNotFound::new);

        if (!vacationDTO.getName().equals(vacation.getName())) {
            vacation.setName(vacationDTO.getName());
        }

        // Should only be enabled by higher priviliged principal
        if (vacationDTO.isEnabled() != vacation.isEnabled()) {
            vacation.setEnabled(vacationDTO.isEnabled());
        }

        return this.vacationRepository.save(vacation);
    }

    @Override
    @PreAuthorize("hasAuthority('SCOPE_vacations:write')")
    public Vacation updateByUuidAndOwner(UUID uuid, String owner, VacationDTO vacationDTO)
            throws VacationNotFound {
        Vacation vacation = this.vacationRepository.findByUuidAndOwner(uuid, owner)
                .orElseThrow(VacationNotFound::new);

        if (!vacationDTO.getName().equals(vacation.getName())) {
            vacation.setName(vacationDTO.getName());
        }

        // Should only be enabled by higher priviliged principal
        if (vacationDTO.isEnabled() != vacation.isEnabled()) {
            log.warn("Unprivileged principal tried to setEnabled!");
        }

        return this.vacationRepository.save(vacation);
    }
}
