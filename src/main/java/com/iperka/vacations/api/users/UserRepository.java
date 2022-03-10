package com.iperka.vacations.api.users;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * The {@link com.iperka.users.api.UserRepository}
 * interface defines the methods that will interact with the
 * {@link com.iperka.users.api.User} model.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.9
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, UUID> {
    /**
     * Returns true if user with given owner already exists.
     * 
     * @since 1.0.9
     * @param owner User id.
     * @return true if exists.
     */
    public boolean existsByOwner(String owner);

    /**
     * Returns true if user with given email already exists.
     * 
     * @since 1.0.9
     * @param emailHash Hash of email.
     * @return true if exists.
     */
    public boolean existsByEmailHash(String emailHash);

    /**
     * Returns true if user with given phone already exists.
     * 
     * @since 1.0.9
     * @param phoneHash Hash of phone.
     * @return true if exists.
     */
    public boolean existsByPhoneHash(String phoneHash);

    /**
     * Returns user with UUID.
     * 
     * @since 1.0.9
     * @param uuid User uuid of desired object.
     * @return Optional with User object.
     */
    public Optional<User> findByUuid(UUID uuid);

    /**
     * Returns user with given owner.
     * 
     * @since 1.0.9
     * @param owner User id of desired object.
     * @return Optional with User object.
     */
    public Optional<User> findByOwner(String owner);

    /**
     * Returns user with given email hash.
     * 
     * @since 1.0.9
     * @param emailHash Hash of email.
     * @return Optional with User object.
     */
    public Optional<User> findByEmailHash(String emailHash);

    /**
     * Returns user with given phone hash.
     * 
     * @since 1.0.9
     * @param phoneHash Hash of phone number.
     * @return Optional with User object.
     */
    public Optional<User> findByPhoneHash(String phoneHash);

    /**
     * Deletes user with given UUID.
     * Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @since 1.0.9
     * @param uuid UUID of desired object.
     */
    public void deleteByUuid(UUID uuid);

    /**
     * Deletes user with given UUID and object must be owned
     * by given user.
     * 
     * @since 1.0.9
     * @param uuid  UUID of desired object.
     * @param owner Owner user id provided by Auth0.
     */
    public void deleteByUuidAndOwner(UUID uuid, String owner);

    /**
     * Deletes all users, object must be owned
     * by given user.
     * 
     * @since 1.0.9
     * @param owner Owner user id provided by Auth0.
     */
    public void deleteAllByOwner(String owner);
}
