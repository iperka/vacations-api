package com.iperka.vacations.api.audit;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for custom query requirements. Extends the
 * {@link PagingAndSortingRepository} interface for pagination support.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
@Repository
public interface AuditRepository extends PagingAndSortingRepository<Audit, String> {
    public Page<Audit> findAllByObjectId(String objectId, Pageable pageable);
}
