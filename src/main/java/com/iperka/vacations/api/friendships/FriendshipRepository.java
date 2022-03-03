package com.iperka.vacations.api.friendships;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * The {@link com.iperka.vacations.api.friendships.FriendshipRepository}
 * interface defines the methods that will interact with the
 * {@link com.iperka.vacations.api.friendships.Friendship} model.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.5
 */
@Repository
public interface FriendshipRepository extends PagingAndSortingRepository<Friendship, UUID> {
    /**
     * Retrieves all friendships as {@link org.springframework.data.domain.Page}
     * object. Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @since 1.0.5
     * @param pageable Pageable object.
     * @return Optional Page with Friendship objects.
     */
    public Page<Friendship> findAll(Pageable pageable);

    /**
     * Retrieves all friendships owned by given user as
     * {@link org.springframework.data.domain.Page} object.
     * 
     * @since 1.0.5
     * @param pageable Pageable object.
     * @param owner    Owner user id provided by Auth0.
     * @return Page with Friendship objects.
     */
    public Page<Friendship> findAllByOwner(Pageable pageable, String owner);

    /**
     * Retrieves all friendships owned by given user as
     * {@link org.springframework.data.domain.Page} object.
     * 
     * @since 1.0.5
     * @param pageable Pageable object.
     * @param owner    Owner user id provided by Auth0.
     * @param user     Related user id provided by Auth0.
     * @return Page with Friendship objects.
     */
    public Page<Friendship> findAllByOwnerAndUser(Pageable pageable, String owner, String user);

    /**
     * Retrieves all friendships owned by given user as
     * List object.
     * 
     * @since 1.0.6
     * @param pageable Pageable object.
     * @param owner    Owner user id provided by Auth0.
     * @param user     Related user id provided by Auth0.
     * @return List with Friendship objects.
     */
    public List<Friendship> findAllByOwnerOrUser(String owner, String user);

    /**
     * Checks if friendship relation already exists and returns a boolean.
     * 
     * @since 1.0.6
     * @param owner Owner user id provided by Auth0.
     * @param user  Related user id provided by Auth0.
     * @return True if relation already exists.
     */
    public boolean existsByOwnerAndUserIgnoreCase(String owner, String user);

    /**
     * Returns friendship with given UUID.
     * Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @since 1.0.5
     * @param uuid UUID of desired object.
     * @return Optional with Friendship object.
     */
    public Optional<Friendship> findByUuid(UUID uuid);

    /**
     * Returns friendship with given UUID and object must be owned
     * by given user.
     * 
     * @since 1.0.5
     * @param uuid  UUID of desired object.
     * @param owner Owner user id provided by Auth0.
     * @return Optional with Friendship object.
     */
    public Optional<Friendship> findByUuidAndOwner(UUID uuid, String owner);

    /**
     * Deletes friendship with given UUID.
     * Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @since 1.0.5
     * @param uuid UUID of desired object.
     */
    public void deleteByUuid(UUID uuid);

    /**
     * Deletes friendship with given UUID and object must be owned
     * by given user.
     * 
     * @since 1.0.5
     * @param uuid  UUID of desired object.
     * @param owner Owner user id provided by Auth0.
     */
    public void deleteByUuidAndOwner(UUID uuid, String owner);
}
