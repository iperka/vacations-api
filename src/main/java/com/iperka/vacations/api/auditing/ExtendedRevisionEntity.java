package com.iperka.vacations.api.auditing;

import java.util.UUID;

import javax.persistence.Entity;

import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

@Entity
@RevisionEntity(ExtendedRevisionListener.class)
public class ExtendedRevisionEntity extends DefaultRevisionEntity {
    private UUID userUuid;

    public UUID getUserUUID() {
        return userUuid;
    }

    public void setUserUUID(UUID uuid) {
        this.userUuid = uuid;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((userUuid == null) ? 0 : userUuid.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        ExtendedRevisionEntity other = (ExtendedRevisionEntity) obj;
        if (userUuid == null) {
            if (other.userUuid != null)
                return false;
        } else if (!userUuid.equals(other.userUuid))
            return false;
        return true;
    }

}
