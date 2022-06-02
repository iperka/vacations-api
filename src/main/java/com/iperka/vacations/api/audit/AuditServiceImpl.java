package com.iperka.vacations.api.audit;



import com.iperka.vacations.api.audit.exceptions.AuditNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

/**
 * Service layer implementation for {@link Audit} resources.
 * 
 * @author Michael Beutler
 * @since 1.0.0
 * @since 1.0.0
 */
@Service
public class AuditServiceImpl implements AuditService {

    @Autowired
    private AuditRepository auditRepository;

    /**
     * Returns all audits as {@link Page} object to offer pagination
     * metadata.
     * 
     * @since 1.0.0
     * @param pageable Page request data.
     * @return Page object.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_audits:all:read', 'SCOPE_audits:all:write')")
    public Page<Audit> findAll(Pageable pageable) {
        return auditRepository.findAll(pageable);
    }

    /**
     * Returns a audit identified by given id as {@link Audit}.
     * 
     * @since 1.0.0
     * @param id Object id of desired audit.
     * @return Audit object.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_audits:all:read', 'SCOPE_audits:all:write')")
    public Audit findById(String id) throws AuditNotFoundException {
        return auditRepository.findById(id).orElseThrow(AuditNotFoundException::new);
    }

    /**
     * Returns a Audit identified by given ObjectId as {@link Page}.
     * 
     * @since 1.0.0
     * @param objectId Object id of desired Audit.
     * @param pageable Page request data.
     * @return Page object containing Audit logs.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_audits:all:read', 'SCOPE_audits:all:write')")
    public Page<Audit> findAllByObjectId(String objectId, Pageable pageable) {
        return auditRepository.findAllByObjectId(objectId, pageable);
    }

    /**
     * Returns a audit created based on given object as {@link Audit}.
     * 
     * @since 1.0.0
     * @param audit Audit object to save.
     * @return Audit object.
     */
    @Override
    public Audit create(Audit audit) {
        audit.setOwner("admin");
        return auditRepository.save(audit);
    }
}
