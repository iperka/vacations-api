package com.iperka.vacations.api.vacations;

import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.iperka.vacations.api.audit.AuditOperation;
import com.iperka.vacations.api.helpers.DateCalculator;
import com.iperka.vacations.api.security.Auditable;
import com.iperka.vacations.api.vacations.exceptions.VacationNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

/**
 * The {@link com.iperka.vacations.api.VacationServiceImpl}
 * class implements defines the
 * {@link com.iperka.vacations.api.VacationServiceImpl}
 * interface and provides service layer methods.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
@Service
public class VacationServiceImpl extends Auditable implements VacationService {
    @Autowired
    private VacationRepository vacationRepository;

    /**
     * Retrieves all vacations as {@link org.springframework.data.domain.Page}
     * object. Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @since 1.0.0
     * @param pageable Pageable object.
     * @return Optional Page with Vacation objects.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_vacations:all:read', 'SCOPE_vacations:all:write')")
    public Page<Vacation> findAll(Pageable pageable) {
        return vacationRepository.findAll(pageable);
    }

    /**
     * Retrieves all vacations owned by given user as
     * {@link org.springframework.data.domain.Page} object.
     * 
     * @since 1.0.0
     * @param pageable Pageable object.
     * @param owner    Owner user id provided by Auth0.
     * @return Optional Page with Vacation objects.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_vacations:read', 'SCOPE_vacations:write', 'SCOPE_vacations:all:read', 'SCOPE_vacations:all:write')")
    public Page<Vacation> findAllByOwner(Pageable pageable, String owner) {
        return vacationRepository.findAllByOwner(pageable, owner);
    }

    /**
     * Returns vacation with given UUID.
     * Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @since 1.0.0
     * @param uuid UUID of desired object.
     * @return Vacation object.
     * @throws VacationNotFoundException if vacation could not be found.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_vacations:all:read', 'SCOPE_vacations:all:write')")
    public Vacation findByUuid(UUID uuid) throws VacationNotFoundException {
        return vacationRepository.findByUuid(uuid).orElseThrow(VacationNotFoundException::new);
    }

    /**
     * Returns vacation with given UUID and object must be owned
     * by given user.
     * 
     * @since 1.0.0
     * @param uuid  UUID of desired object.
     * @param owner Owner user id provided by Auth0.
     * @return Vacation object.
     * @throws VacationNotFoundException if vacation could not be found.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_vacations:read', 'SCOPE_vacations:write', 'SCOPE_vacations:all:read', 'SCOPE_vacations:all:write')")
    public Vacation findByUuidAndOwner(UUID uuid, String owner) throws VacationNotFoundException {
        return vacationRepository.findByUuidAndOwner(uuid, owner).orElseThrow(VacationNotFoundException::new);
    }

    /**
     * Returns next vacation for given user relative to given date.
     * 
     * @since 1.0.1
     * @param owner     Owner user id provided by Auth0.
     * @param startDate Start date for relative search.
     * @return Next vacation according to given owner and date.
     * @throws VacationNotFoundException if no vacation could not be found.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_vacations:read', 'SCOPE_vacations:write', 'SCOPE_vacations:all:read', 'SCOPE_vacations:all:write')")
    public Vacation findByOwnerAndStartDateGreaterThanOrderByStartDateAsc(String owner, Date startDate)
            throws VacationNotFoundException {
        return vacationRepository.findByOwnerAndStartDateGreaterThanOrderByStartDateAsc(owner, startDate)
                .orElseThrow(VacationNotFoundException::new);
    }

    /**
     * Creates and returns vacation.
     * 
     * @since 1.0.0
     * @param vacation new object.
     * @return Vacation created object.
     * @throws VacationNotFoundException if vacation could not be found.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_vacations:write', 'SCOPE_vacations:all:write')")
    public Vacation create(Vacation vacation) {
        vacation = vacationRepository.save(vacation);
        this.audit(AuditOperation.CREATE, null, vacation);
        return vacation;
    }

    /**
     * Updates and returns vacation with given object.
     * 
     * @since 1.0.0
     * @param vacation new object.
     * @return Vacation updated object.
     * @throws VacationNotFoundException if vacation could not be found.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_vacations:all:write')")
    public Vacation update(Vacation vacation) throws VacationNotFoundException {
        Vacation before = this.findByUuid(vacation.getUuid());
        Vacation after = vacationRepository.save(vacation);
        this.audit(AuditOperation.UPDATE, before, after);
        return after;
    }

    /**
     * Updates and returns vacation with given object if owner equals given owner.
     * 
     * @since 1.0.0
     * @param vacation new object.
     * @param owner    Owner user id provided by Auth0.
     * @return Vacation updated object.
     * @throws VacationNotFoundException if vacation could not be found.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_vacations:write', 'SCOPE_vacations:all:write')")
    public Vacation updateByOwner(Vacation vacation, String owner) throws VacationNotFoundException {
        Vacation before = this.findByUuidAndOwner(vacation.getUuid(), owner);
        Vacation after = vacationRepository.save(vacation);
        this.audit(AuditOperation.UPDATE, before, after);
        return after;
    }

    /**
     * Deletes vacation with given UUID.
     * Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @since 1.0.0
     * @param uuid UUID of desired object.
     * @throws VacationNotFoundException if vacation could not be found.
     */
    @Override
    @PreAuthorize("hasAuthority('SCOPE_vacations:all:write')")
    public void deleteByUuid(UUID uuid) throws VacationNotFoundException {
        Vacation vacation = this.findByUuid(uuid);
        vacationRepository.deleteByUuid(vacation.getUuid());
    }

    /**
     * Deletes vacation with given UUID and object must be owned
     * by given user.
     * 
     * @since 1.0.0
     * @param uuid  UUID of desired object.
     * @param owner Owner user id provided by Auth0.
     * @throws VacationNotFoundException if vacation could not be found.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_vacations:write', 'SCOPE_vacations:all:write')")
    public void deleteByUuidAndOwner(UUID uuid, String owner) throws VacationNotFoundException {
        Vacation vacation = this.findByUuidAndOwner(uuid, owner);
        vacationRepository.deleteByUuidAndOwner(vacation.getUuid(), owner);
    }

    @PreAuthorize("hasAnyAuthority('SCOPE_vacations:read', 'SCOPE_vacations:write', 'SCOPE_vacations:all:read', 'SCOPE_vacations:all:write')")
    public double[] getDaysCountByMonth(List<Vacation> vacations, Year year) {
        double[] vacationMonthViewDTO = new double[12];

        for (Vacation vacation : vacations) {
            double total = vacation.getDays();
            double daysAdded = 0;

            LocalDate startDateAsLocal = LocalDate.ofInstant(vacation.getStartDate().toInstant(),
                    ZoneId.systemDefault());
            LocalDate endDateAsLocal = LocalDate.ofInstant(vacation.getEndDate().toInstant(), ZoneId.systemDefault());

            List<LocalDate> dates = DateCalculator.getBusinessDaysBetween(startDateAsLocal, endDateAsLocal,
                    Optional.empty());

            for (LocalDate localDate : dates) {
                if (localDate.getYear() != year.getValue()) {
                    continue;
                }
                if (total > daysAdded + 1) {
                    vacationMonthViewDTO[localDate.getMonthValue() - 1] += 1;
                    daysAdded++;
                    continue;
                }
                vacationMonthViewDTO[localDate.getMonthValue() - 1] += total - daysAdded;
            }
        }

        return vacationMonthViewDTO;
    }
}
