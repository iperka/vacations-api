package com.iperka.vacations.api.users;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * The {@link com.iperka.vacations.api.users.UserServiceImpl} class implements
 * the {@link com.iperka.vacations.api.users.UserService} interface and is used
 * to manage the user entity and authentication / authorization. model.
 * 
 * @author Michael Beutler
 * @version 0.0.2
 * @since 2021-05-07
 */
@Service
public class UserServiceImpl implements UserService {

    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Search user in database with repository
        Optional<User> uOptional = userRepository.findByUsername(username);

        // Check if user with given username exists
        if (!uOptional.isPresent()) {
            String message = String.format("User with username '%s' can not be found.", username);

            // Log message
            logger.info(message);

            // Throw UsernameNotFoundException
            throw new UsernameNotFoundException(message);
        }

        // only debug
        logger.debug("User with username '{}' found.", username);

        // Get user
        User user = uOptional.get();

        // TODO get authorities from separate entity / repository
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Returns user with granted authorities
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                user.isEnabled(), !user.isExpired(), !user.isCredentialsExpired(), user.isLocked(), authorities);
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return this.userRepository.findAll(pageable);
    }

    @Override
    public Optional<User> findByUUID(UUID uuid) {
        return this.userRepository.findById(uuid);
    }
}
