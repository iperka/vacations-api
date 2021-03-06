package com.iperka.vacations.api.vacations;

import java.time.LocalDate;
import java.time.Year;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;


import javax.transaction.Transactional;

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
 * The {@link com.iperka.vacations.api.UserServiceImpl}
 * class implements defines the
 * {@link com.iperka.vacations.api.UserServiceImpl}
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
     * Returns vacation with given String.
     * Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @since 1.0.0
     * @param id String of desired object.
     * @return Vacation object.
     * @throws VacationNotFoundException if vacation could not be found.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_vacations:all:read', 'SCOPE_vacations:all:write')")
    public Vacation findById(String id) throws VacationNotFoundException {
        return vacationRepository.findById(id).orElseThrow(VacationNotFoundException::new);
    }

    /**
     * Returns vacation with given String and object must be owned
     * by given user.
     * 
     * @since 1.0.0
     * @param id  String of desired object.
     * @param owner Owner user id provided by Auth0.
     * @return Vacation object.
     * @throws VacationNotFoundException if vacation could not be found.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_vacations:read', 'SCOPE_vacations:write', 'SCOPE_vacations:all:read', 'SCOPE_vacations:all:write')")
    public Vacation findByIdAndOwner(String id, String owner) throws VacationNotFoundException {
        return vacationRepository.findByIdAndOwner(id, owner).orElseThrow(VacationNotFoundException::new);
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
    public Vacation findByOwnerAndStartDateGreaterThanEqualOrderByStartDateAsc(String owner, Date startDate)
            throws VacationNotFoundException {
        return vacationRepository.findByOwnerAndStartDateGreaterThanEqualOrderByStartDateAsc(owner, startDate)
                .orElseThrow(VacationNotFoundException::new);
    }

    /**
     * Creates and returns vacation.
     * 
     * @since 1.0.0
     * @param vacation new object.
     * @return Vacation created object.
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
        Vacation before = this.findById(vacation.getId());
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
        Vacation before = this.findByIdAndOwner(vacation.getId(), owner);
        Vacation after = vacationRepository.save(vacation);
        this.audit(AuditOperation.UPDATE, before, after);
        return after;
    }

    /**
     * Deletes vacation with given String.
     * Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @since 1.0.0
     * @param id String of desired object.
     * @throws VacationNotFoundException if vacation could not be found.
     */
    @Override
    @Transactional
    @PreAuthorize("hasAuthority('SCOPE_vacations:all:write')")
    public void deleteById(String id) throws VacationNotFoundException {
        Vacation vacation = this.findById(id);
        vacationRepository.deleteById(vacation.getId());
    }

    /**
     * Deletes vacation with given String and object must be owned
     * by given user.
     * 
     * @since 1.0.0
     * @param id  String of desired object.
     * @param owner Owner user id provided by Auth0.
     * @throws VacationNotFoundException if vacation could not be found.
     */
    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('SCOPE_vacations:write', 'SCOPE_vacations:all:write')")
    public void deleteByIdAndOwner(String id, String owner) throws VacationNotFoundException {
        Vacation vacation = this.findByIdAndOwner(id, owner);
        vacationRepository.deleteByIdAndOwner(vacation.getId(), owner);
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
