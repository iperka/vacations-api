package com.iperka.vacations.api.friendships;

import java.util.List;


import com.iperka.vacations.api.friendships.exceptions.FriendshipNotFoundException;
import com.iperka.vacations.api.friendships.exceptions.FriendshipRelationAlreadyExistsException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * The {@link com.iperka.vacations.api.friendships.FriendshipService}
 * interface defines the methods that will interact with the
 * {@link com.iperka.vacations.api.friendships.FriendshipRepository} interface.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.5
 */
public interface FriendshipService {
    /**
     * Retrieves all friendships as {@link org.springframework.data.domain.Page}
     * object. Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @since 1.0.5
     * @param pageable Pageable object.
     * @return Optional Page with Friendship objects.
     */
    public abstract Page<Friendship> findAll(Pageable pageable);

    /**
     * Retrieves all friendships owned by given user as
     * {@link org.springframework.data.domain.Page} object.
     * 
     * @since 1.0.5
     * @param pageable Pageable object.
     * @param owner    Owner user id provided by Auth0.
     * @return Page with Friendship objects.
     */
    public abstract Page<Friendship> findAllByOwner(Pageable pageable, String owner);

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
    public abstract Page<Friendship> findAllByOwnerAndUser(Pageable pageable, String owner, String user);

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
    public abstract List<Friendship> findAllByOwnerOrUser(String owner, String user);

    /**
     * Checks if friendship relation already exists and returns a boolean.
     * 
     * @since 1.0.6
     * @param owner Owner user id provided by Auth0.
     * @param user  Related user id provided by Auth0.
     * @return True if relation already exists.
     */
    public abstract boolean existsByOwnerAndUserIgnoreCase(String owner, String user);

    /**
     * Returns friendship with given String.
     * Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @since 1.0.5
     * @param id String of desired object.
     * @return Friendship object.
     * @throws FriendshipNotFoundException if friendship could not be found.
     */
    public abstract Friendship findById(String id) throws FriendshipNotFoundException;

    /**
     * Returns friendship with given String and object must be owned
     * by given user.
     * 
     * @since 1.0.5
     * @param id  String of desired object.
     * @param owner Owner user id provided by Auth0.
     * @return Friendship object.
     * @throws FriendshipNotFoundException if friendship could not be found.
     */
    public abstract Friendship findByIdAndOwner(String id, String owner) throws FriendshipNotFoundException;

    /**
     * Creates and returns friendship.
     * 
     * @since 1.0.5
     * @param friendship new object.
     * @return Friendship created object.
     * @throws FriendshipRelationAlreadyExistsException if friendship object already
     *                                                  exists.
     */
    public abstract Friendship create(Friendship friendship) throws FriendshipRelationAlreadyExistsException;

    /**
     * Updates and returns friendship with given object.
     * 
     * @since 1.0.5
     * @param friendship new object.
     * @return Friendship updated object.
     * @throws FriendshipNotFoundException if friendship could not be found.
     */
    public abstract Friendship update(Friendship friendship) throws FriendshipNotFoundException;

    /**
     * Updates and returns friendship with given object if owner equals given owner.
     * 
     * @since 1.0.5
     * @param friendship new object.
     * @param owner      Owner user id provided by Auth0.
     * @return Friendship updated object.
     * @throws FriendshipNotFoundException if friendship could not be found.
     */
    public abstract Friendship updateByOwner(Friendship friendship, String owner) throws FriendshipNotFoundException;

    /**
     * Deletes friendship with given String.
     * Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @since 1.0.5
     * @param id String of desired object.
     * @throws FriendshipNotFoundException if friendship could not be found.
     */
    public abstract void deleteById(String id) throws FriendshipNotFoundException;

    /**
     * Deletes friendship with given String and object must be owned
     * by given user.
     * 
     * @since 1.0.5
     * @param id  String of desired object.
     * @param owner Owner user id provided by Auth0.
     * @throws FriendshipNotFoundException if friendship could not be found.
     */
    public abstract void deleteByIdAndOwner(String id, String owner) throws FriendshipNotFoundException;
}
