package com.iperka.vacations.api.audit;

import java.util.UUID;

import com.iperka.vacations.api.audit.exceptions.AuditNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service defining interface for {@link Audit} resources.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
public interface AuditService {

    /**
     * Returns all Audit logs as {@link Page} object to offer pagination
     * metadata.
     * 
     * @since 1.0.0
     * @param pageable Page request data.
     * @return Page object containing Audit logs.
     */
    public abstract Page<Audit> findAll(Pageable pageable);

    /**
     * Returns a Audit identified by given id as {@link Audit}.
     * 
     * @since 1.0.0
     * @param id Object id of desired Audit.
     * @return Audit object.
     */
    public abstract Audit findById(UUID id) throws AuditNotFoundException;

    /**
     * Returns a Audit identified by given ObjectId as {@link Page}.
     * 
     * @since 1.0.0
     * @param objectUuid Object uuid of desired Audit.
     * @param pageable   Page request data.
     * @return Page object containing Audit logs.
     */
    public abstract Page<Audit> findAllByObjectId(UUID objectUuid, Pageable pageable);

    /**
     * Returns a Audit created based on given object as {@link Audit}.
     * 
     * @since 1.0.0
     * @param Audit Audit object to save.
     * @return Audit object.
     */
    public abstract Audit create(Audit Audit);
}
