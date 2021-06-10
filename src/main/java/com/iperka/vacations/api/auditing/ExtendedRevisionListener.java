package com.iperka.vacations.api.auditing;

import com.iperka.vacations.api.users.User;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.context.SecurityContextHolder;

public class ExtendedRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        // Get user from SecurityContextHolder
        var user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var extendedRevisionEntity = (ExtendedRevisionEntity) revisionEntity;
        extendedRevisionEntity.setUserUUID(user.getUuid());
    }

}
