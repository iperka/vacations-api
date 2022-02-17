package com.iperka.vacations.api.security;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.MappedSuperclass;

import com.iperka.vacations.api.audit.Audit;
import com.iperka.vacations.api.audit.AuditOperation;
import com.iperka.vacations.api.audit.AuditService;
import com.iperka.vacations.api.helpers.GenericFields;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;

import lombok.extern.slf4j.Slf4j;

/**
 * Superclass for auditable service implementations.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@MappedSuperclass
public abstract class Auditable {
    @Autowired
    private AuditService auditService;

    /**
     * Writes audit operation to database. If the operation is
     * {@link AuditOperation#UPDATE} the objects will get compared and differences
     * will be evaluated. This method doesn't throw, it will just log unhandled
     * exceptions to console.
     * 
     * @since 1.0.0
     * @param operation Type of operation.
     * @param before    Before update. (Can be null if create or delete)
     * @param after     After update. (Can not be null.)
     */
    protected void audit(@NonNull AuditOperation operation, GenericFields before, @NonNull GenericFields after) {
        String description = "n/a";

        // Evaluate description.
        switch (operation.toString()) {
            case "CREATE":
                description = String.format("Created %s with uuid %s.", after.getClass().getName(),
                        after.getUuid());
                break;
            case "UPDATE":
                description = String.format("Update %s with uuid %s.", after.getClass().getName(),
                        after.getUuid());
                break;
            case "DELETE":
                description = String.format("Delete %s with uuid %s.", after.getClass().getName(),
                        after.getUuid());
                break;

            default:
                break;
        }

        // Check if operation equals UPDATE and if so compare objects.
        if (operation.toString().equals(AuditOperation.UPDATE.toString())) {
            Map<String, Map<String, String>> diff = new HashMap<>();

            // Iterate over all getters and compare before with after value. This does not
            // work for nested values.
            for (Method m : before.getClass().getMethods()) {
                if (m.getName().startsWith("get") && m.getParameterTypes().length == 0) {
                    try {
                        // Exec getter methods
                        Object beforeObject = m.invoke(before);
                        Object afterObject = m.invoke(after);

                        // If one object is null, continue
                        if (beforeObject == null || afterObject == null) {
                            continue;
                        }

                        if (!beforeObject.equals(afterObject)) {
                            Map<String, String> values = new HashMap<>();
                            values.put("from", m.invoke(before).toString());
                            values.put("to", m.invoke(after).toString());

                            // Removes get from getter method and converts to lowercase
                            diff.put(m.getName().replace("get", "").toLowerCase(), values);
                        }
                    } catch (Exception e) {
                        log.error("Error", e);
                    }

                }
            }

            // Write to database
            this.auditService
                    .create(new Audit(after.getClass().getName(), after.getUuid(), operation, diff, description));
            return;
        }

        // Write to database
        this.auditService
                .create(new Audit(after.getClass().getName(), after.getUuid(), operation, null, description));
    }
}
