package com.iperka.vacations.api.users;



import com.iperka.vacations.api.users.exceptions.EmailAndOrPhoneAlreadyConnectedException;
import com.iperka.vacations.api.users.exceptions.UserNotFoundException;

/**
 * The {@link com.iperka.users.api.UserService}
 * interface defines the methods that will interact with the
 * {@link com.iperka.users.api.UserRepository} interface.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.9
 */
public interface UserService {
    /**
     * Returns user with String.
     * 
     * @since 1.0.9
     * @param id User id of desired object.
     * @return Optional with User object.
     * @throws UserNotFoundException if user could not be found.
     */
    public abstract User findById(String id) throws UserNotFoundException;

    /**
     * Returns user with given owner.
     * 
     * @since 1.0.9
     * @param owner User id of desired object.
     * @return Optional with User object.
     * @throws UserNotFoundException if user could not be found.
     */
    public abstract User findByOwner(String owner) throws UserNotFoundException;

    /**
     * Returns user with given email hash.
     * 
     * @since 1.0.9
     * @param emailHash Hash of email.
     * @return Optional with User object.
     * @throws UserNotFoundException if user could not be found.
     */
    public abstract User findByEmailHash(String emailHash) throws UserNotFoundException;

    /**
     * Returns user with given phone hash.
     * 
     * @since 1.0.9
     * @param phoneHash Hash of phone number.
     * @return Optional with User object.
     * @throws UserNotFoundException if user could not be found.
     */
    public abstract User findByPhoneHash(String phoneHash) throws UserNotFoundException;

    /**
     * Creates and returns user.
     * 
     * @since 1.0.9
     * @param user new object.
     * @return User created object.
     * @throws EmailAndOrPhoneAlreadyConnectedException if user already exists.
     */
    public abstract User create(User user) throws EmailAndOrPhoneAlreadyConnectedException;

    /**
     * Updates and returns user with given object if owner equals given owner.
     * 
     * @since 1.0.9
     * @param user  new object.
     * @param owner Owner user id provided by Auth0.
     * @return User updated object.
     * @throws UserNotFoundException if user could not be found.
     */
    public abstract User updateByOwner(User user, String owner) throws UserNotFoundException;

    /**
     * Deletes user with given String.
     * Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @since 1.0.9
     * @param id String of desired object.
     * @throws UserNotFoundException if user could not be found.
     */
    public abstract void deleteById(String id) throws UserNotFoundException;

    /**
     * Deletes user with given String and object must be owned
     * by given user.
     * 
     * @since 1.0.9
     * @param id  String of desired object.
     * @param owner Owner user id provided by Auth0.
     * @throws UserNotFoundException if user could not be found.
     */
    public abstract void deleteByIdAndOwner(String id, String owner) throws UserNotFoundException;

    /**
     * Deletes all users, object must be owned
     * by given user.
     * 
     * @since 1.0.9
     * @param owner Owner user id provided by Auth0.
     */
    public abstract void deleteAllByOwner(String owner);
}
