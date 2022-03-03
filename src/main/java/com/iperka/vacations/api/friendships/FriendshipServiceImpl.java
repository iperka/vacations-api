package com.iperka.vacations.api.friendships;

import java.util.UUID;

import com.iperka.vacations.api.audit.AuditOperation;
import com.iperka.vacations.api.friendships.exceptions.FriendshipNotFoundException;
import com.iperka.vacations.api.security.Auditable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

/**
 * The {@link com.iperka.vacations.api.friendships.FriendshipServiceImpl}
 * class implements defines the
 * {@link com.iperka.vacations.api.friendships.FriendshipServiceImpl}
 * interface and provides service layer methods.
 * 
 * @author Michael Beutler
 * @version 1.0.0
 * @since 1.0.5
 */
@Service
public class FriendshipServiceImpl extends Auditable implements FriendshipService {
    @Autowired
    private FriendshipRepository friendshipRepository;

    /**
     * Retrieves all friendships as {@link org.springframework.data.domain.Page}
     * object. Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @since 1.0.5
     * @param pageable Pageable object.
     * @return Page with Friendship objects.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_friendships:all:read', 'SCOPE_friendships:all:write')")
    public Page<Friendship> findAll(Pageable pageable) {
        return friendshipRepository.findAll(pageable);
    }

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
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_friendships:read', 'SCOPE_friendships:write', 'SCOPE_friendships:all:read', 'SCOPE_friendships:all:write')")
    public Page<Friendship> findAllByOwnerAndUser(Pageable pageable, String owner, String user) {
        return friendshipRepository.findAllByOwnerAndUser(pageable, owner, user);
    }

    /**
     * Retrieves all friendships owned by given user as
     * {@link org.springframework.data.domain.Page} object.
     * 
     * @since 1.0.5
     * @param pageable Pageable object.
     * @param owner    Owner user id provided by Auth0.
     * @return Page with Friendship objects.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_friendships:read', 'SCOPE_friendships:write', 'SCOPE_friendships:all:read', 'SCOPE_friendships:all:write')")
    public Page<Friendship> findAllByOwner(Pageable pageable, String owner) {
        return friendshipRepository.findAllByOwner(pageable, owner);
    }

    /**
     * Returns friendship with given UUID.
     * Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @since 1.0.5
     * @param uuid UUID of desired object.
     * @return Friendship object.
     * @throws FriendshipNotFoundException if friendship could not be found.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_friendships:all:read', 'SCOPE_friendships:all:write')")
    public Friendship findByUuid(UUID uuid) throws FriendshipNotFoundException {
        return friendshipRepository.findByUuid(uuid).orElseThrow(FriendshipNotFoundException::new);
    }

    /**
     * Returns friendship with given UUID and object must be owned
     * by given user.
     * 
     * @since 1.0.5
     * @param uuid  UUID of desired object.
     * @param owner Owner user id provided by Auth0.
     * @return Friendship object.
     * @throws FriendshipNotFoundException if friendship could not be found.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_friendships:read', 'SCOPE_friendships:write', 'SCOPE_friendships:all:read', 'SCOPE_friendships:all:write')")
    public Friendship findByUuidAndOwner(UUID uuid, String owner) throws FriendshipNotFoundException {
        return friendshipRepository.findByUuidAndOwner(uuid, owner).orElseThrow(FriendshipNotFoundException::new);
    }

    /**
     * Creates and returns friendship.
     * 
     * @since 1.0.5
     * @param friendship new object.
     * @return Friendship created object.
     * @throws FriendshipNotFoundException if friendship could not be found.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_friendships:write', 'SCOPE_friendships:all:write')")
    public Friendship create(Friendship friendship) {
        friendship = friendshipRepository.save(friendship);
        this.audit(AuditOperation.CREATE, null, friendship);
        return friendship;
    }

    /**
     * Updates and returns friendship with given object.
     * 
     * @since 1.0.5
     * @param friendship new object.
     * @return Friendship updated object.
     * @throws FriendshipNotFoundException if friendship could not be found.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_friendships:all:write')")
    public Friendship update(Friendship friendship) throws FriendshipNotFoundException {
        Friendship before = this.findByUuid(friendship.getUuid());
        Friendship after = friendshipRepository.save(friendship);
        this.audit(AuditOperation.UPDATE, before, after);
        return after;
    }

    /**
     * Updates and returns friendship with given object if owner equals given owner.
     * 
     * @since 1.0.5
     * @param friendship new object.
     * @param owner      Owner user id provided by Auth0.
     * @return Friendship updated object.
     * @throws FriendshipNotFoundException if friendship could not be found.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_friendships:write', 'SCOPE_friendships:all:write')")
    public Friendship updateByOwner(Friendship friendship, String owner) throws FriendshipNotFoundException {
        Friendship before = this.findByUuidAndOwner(friendship.getUuid(), owner);
        Friendship after = friendshipRepository.save(friendship);
        this.audit(AuditOperation.UPDATE, before, after);
        return after;
    }

    /**
     * Deletes friendship with given UUID.
     * Bare in mind that these method should be explicit to administrative
     * roles.
     * 
     * @since 1.0.5
     * @param uuid UUID of desired object.
     * @throws FriendshipNotFoundException if friendship could not be found.
     */
    @Override
    @PreAuthorize("hasAuthority('SCOPE_friendships:all:write')")
    public void deleteByUuid(UUID uuid) throws FriendshipNotFoundException {
        Friendship friendship = this.findByUuid(uuid);
        friendshipRepository.deleteByUuid(friendship.getUuid());
    }

    /**
     * Deletes friendship with given UUID and object must be owned
     * by given user.
     * 
     * @since 1.0.5
     * @param uuid  UUID of desired object.
     * @param owner Owner user id provided by Auth0.
     * @throws FriendshipNotFoundException if friendship could not be found.
     */
    @Override
    @PreAuthorize("hasAnyAuthority('SCOPE_friendships:write', 'SCOPE_friendships:all:write')")
    public void deleteByUuidAndOwner(UUID uuid, String owner) throws FriendshipNotFoundException {
        Friendship friendship = this.findByUuidAndOwner(uuid, owner);
        friendshipRepository.deleteByUuidAndOwner(friendship.getUuid(), owner);
    }
}
