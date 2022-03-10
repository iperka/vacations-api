package com.iperka.vacations.api.users;

import java.util.UUID;

import javax.transaction.Transactional;

import com.iperka.vacations.api.audit.AuditOperation;
import com.iperka.vacations.api.security.Auditable;
import com.iperka.vacations.api.users.exceptions.EmailAndOrPhoneAlreadyConnectedException;
import com.iperka.vacations.api.users.exceptions.UserNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

/**
 * The {@link com.iperka.users.api.UserServiceImpl}
 * class implements defines the
 * {@link com.iperka.users.api.UserServiceImpl}
 * interface and provides service layer methods.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.9
 */
@Service
public class UserServiceImpl extends Auditable implements UserService {
    @Autowired
    private UserRepository userRepository;

    /**
     * Returns user with UUID.
     * 
     * @since 1.0.9
     * @param uuid User uuid of desired object.
     * @return Optional with User object.
     * @throws UserNotFoundException if user could not be found.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_users:read', 'SCOPE_users:write','SCOPE_users:all:read', 'SCOPE_users:all:write')")
    public User findByUuid(UUID uuid) throws UserNotFoundException {
        return userRepository.findByUuid(uuid).orElseThrow(UserNotFoundException::new);
    }

    /**
     * Returns user with given owner.
     * 
     * @since 1.0.9
     * @param owner User id of desired object.
     * @return Optional with User object.
     * @throws UserNotFoundException if user could not be found.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_users:read', 'SCOPE_users:write','SCOPE_users:all:read', 'SCOPE_users:all:write')")
    public User findByOwner(String owner) throws UserNotFoundException {
        return userRepository.findByOwner(owner).orElseThrow(UserNotFoundException::new);
    }

    /**
     * Returns user with given email hash.
     * 
     * @since 1.0.9
     * @param emailHash Hash of email.
     * @return Optional with User object.
     * @throws UserNotFoundException if user could not be found.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_users:read', 'SCOPE_users:write','SCOPE_users:all:read', 'SCOPE_users:all:write')")
    public User findByEmailHash(String emailHash) throws UserNotFoundException {
        return userRepository.findByEmailHash(emailHash).orElseThrow(UserNotFoundException::new);
    }

    /**
     * Returns user with given phone hash.
     * 
     * @since 1.0.9
     * @param phoneHash Hash of phone number.
     * @return Optional with User object.
     * @throws UserNotFoundException if user could not be found.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_users:read', 'SCOPE_users:write','SCOPE_users:all:read', 'SCOPE_users:all:write')")
    public User findByPhoneHash(String phoneHash) throws UserNotFoundException {
        return userRepository.findByPhoneHash(phoneHash).orElseThrow(UserNotFoundException::new);
    }

    /**
     * Creates and returns user.
     * 
     * @since 1.0.9
     * @param user new object.
     * @return User created object.
     * @throws UserNotFoundException if user could not be found.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_users:write', 'SCOPE_users:all:write')")
    public User create(User user) throws EmailAndOrPhoneAlreadyConnectedException {
        if (userRepository.existsByEmailHash(user.getEmailHash())
                || userRepository.existsByPhoneHash(user.getPhoneHash())
                || userRepository.existsByOwner(user.getOwner())) {
            throw new EmailAndOrPhoneAlreadyConnectedException();
        }

        user = userRepository.save(user);
        this.audit(AuditOperation.CREATE, null, user);
        return user;
    }

    /**
     * Updates and returns user with given object if owner equals given owner.
     * 
     * @since 1.0.9
     * @param user  new object.
     * @param owner Owner user id provided by Auth0.
     * @return User updated object.
     * @throws UserNotFoundException if user could not be found.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_users:write', 'SCOPE_users:all:write')")
    public User updateByOwner(User user, String owner) throws UserNotFoundException {
        User before = this.findByUuid(user.getUuid());
        User after = userRepository.save(user);
        this.audit(AuditOperation.UPDATE, before, after);
        return after;
    }

    /**
     * Deletes user with given UUID.
     * Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @since 1.0.9
     * @param uuid UUID of desired object.
     * @throws UserNotFoundException if user could not be found.
     */
    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('SCOPE_users:write', 'SCOPE_users:all:write')")
    public void deleteByUuid(UUID uuid) throws UserNotFoundException {
        userRepository.deleteByUuid(uuid);
    }

    /**
     * Deletes user with given UUID and object must be owned
     * by given user.
     * 
     * @since 1.0.9
     * @param uuid  UUID of desired object.
     * @param owner Owner user id provided by Auth0.
     * @throws UserNotFoundException if user could not be found.
     */
    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('SCOPE_users:write', 'SCOPE_users:all:write')")
    public void deleteByUuidAndOwner(UUID uuid, String owner) throws UserNotFoundException {
        userRepository.deleteByUuidAndOwner(uuid, owner);
    }

    /**
     * Deletes all users, object must be owned
     * by given user.
     * 
     * @since 1.0.9
     * @param owner Owner user id provided by Auth0.
     */
    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('SCOPE_users:write', 'SCOPE_users:all:write')")
    public void deleteAllByOwner(String owner) {
        userRepository.deleteAllByOwner(owner);
    }

}
