package com.iperka.vacations.api.friendships;

import java.util.List;
import java.util.UUID;

import com.iperka.vacations.api.friendships.exceptions.FriendshipNotFoundException;
import com.iperka.vacations.api.friendships.exceptions.FriendshipRelationAlreadyExistsException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The {@link com.iperka.vacations.api.friendships.FriendshipService}
 * interface defines the methods that will interact with the
 * {@link com.iperka.vacations.api.friendships.FriendshipRepository} interface.
 * 
 * @deprecated
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.5
 */
@Deprecated
public interface FriendshipService {
    /**
     * Retrieves all friendships as {@link org.springframework.data.domain.Page}
     * object. Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @deprecated
     * @since 1.0.5
     * @param pageable Pageable object.
     * @return Optional Page with Friendship objects.
     */
   @Deprecated 
public abstract Page<Friendship> findAll(Pageable pageable);

    /**
     * Retrieves all friendships owned by given user as
     * {@link org.springframework.data.domain.Page} object.
     * 
     * @deprecated
     * @since 1.0.5
     * @param pageable Pageable object.
     * @param owner    Owner user id provided by Auth0.
     * @return Page with Friendship objects.
     */
   @Deprecated 
public abstract Page<Friendship> findAllByOwner(Pageable pageable, String owner);

    /**
     * Retrieves all friendships owned by given user as
     * {@link org.springframework.data.domain.Page} object.
     * 
     * @deprecated
     * @since 1.0.5
     * @param pageable Pageable object.
     * @param owner    Owner user id provided by Auth0.
     * @param user     Related user id provided by Auth0.
     * @return Page with Friendship objects.
     */
   @Deprecated 
public abstract Page<Friendship> findAllByOwnerAndUser(Pageable pageable, String owner, String user);

    /**
     * Retrieves all friendships owned by given user as
     * List object.
     * 
     * @deprecated
     * @since 1.0.6
     * @param pageable Pageable object.
     * @param owner    Owner user id provided by Auth0.
     * @param user     Related user id provided by Auth0.
     * @return List with Friendship objects.
     */
   @Deprecated 
public abstract List<Friendship> findAllByOwnerOrUser(String owner, String user);

    /**
     * Checks if friendship relation already exists and returns a boolean.
     * 
     * @deprecated
     * @since 1.0.6
     * @param owner Owner user id provided by Auth0.
     * @param user  Related user id provided by Auth0.
     * @return True if relation already exists.
     */
   @Deprecated 
public abstract boolean existsByOwnerAndUserIgnoreCase(String owner, String user);

    /**
     * Returns friendship with given UUID.
     * Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @deprecated
     * @since 1.0.5
     * @param uuid UUID of desired object.
     * @return Friendship object.
     * @throws FriendshipNotFoundException if friendship could not be found.
     */
   @Deprecated 
public abstract Friendship findByUuid(UUID uuid) throws FriendshipNotFoundException;

    /**
     * Returns friendship with given UUID and object must be owned
     * by given user.
     * 
     * @deprecated
     * @since 1.0.5
     * @param uuid  UUID of desired object.
     * @param owner Owner user id provided by Auth0.
     * @return Friendship object.
     * @throws FriendshipNotFoundException if friendship could not be found.
     */
   @Deprecated 
public abstract Friendship findByUuidAndOwner(UUID uuid, String owner) throws FriendshipNotFoundException;

    /**
     * Creates and returns friendship.
     * 
     * @deprecated
     * @since 1.0.5
     * @param friendship new object.
     * @return Friendship created object.
     * @throws FriendshipRelationAlreadyExistsException if friendship object already
     *                                                  exists.
     */
   @Deprecated 
public abstract Friendship create(Friendship friendship) throws FriendshipRelationAlreadyExistsException;

    /**
     * Updates and returns friendship with given object.
     * 
     * @deprecated
     * @since 1.0.5
     * @param friendship new object.
     * @return Friendship updated object.
     * @throws FriendshipNotFoundException if friendship could not be found.
     */
   @Deprecated 
public abstract Friendship update(Friendship friendship) throws FriendshipNotFoundException;

    /**
     * Updates and returns friendship with given object if owner equals given owner.
     * 
     * @deprecated
     * @since 1.0.5
     * @param friendship new object.
     * @param owner      Owner user id provided by Auth0.
     * @return Friendship updated object.
     * @throws FriendshipNotFoundException if friendship could not be found.
     */
   @Deprecated 
public abstract Friendship updateByOwner(Friendship friendship, String owner) throws FriendshipNotFoundException;

    /**
     * Deletes friendship with given UUID.
     * Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @deprecated
     * @since 1.0.5
     * @param uuid UUID of desired object.
     * @throws FriendshipNotFoundException if friendship could not be found.
     */
   @Deprecated 
public abstract void deleteByUuid(UUID uuid) throws FriendshipNotFoundException;

    /**
     * Deletes friendship with given UUID and object must be owned
     * by given user.
     * 
     * @deprecated
     * @since 1.0.5
     * @param uuid  UUID of desired object.
     * @param owner Owner user id provided by Auth0.
     * @throws FriendshipNotFoundException if friendship could not be found.
     */
   @Deprecated 
public abstract void deleteByUuidAndOwner(UUID uuid, String owner) throws FriendshipNotFoundException;
}
