package com.iperka.vacations.api.friendships;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FriendshipServiceImplTest {
    @Mock
    private FriendshipRepository friendshipRepository;

    FriendshipServiceImpl friendshipService;

    @BeforeEach
    void initUseCase() {
        friendshipService = new FriendshipServiceImpl();
    }

    @Test
    void createFriendship() {
        Friendship friendship = new Friendship();
        friendship.setOwner("test");
        friendship.setUser("test");
        friendship.setStatus(FriendshipStatus.ACCEPTED);

        // providing knowledge
        when(friendshipRepository.save(any(Friendship.class))).thenReturn(friendship);

        Friendship savedFriendship = friendshipRepository.save(friendship);
        assertNotNull(savedFriendship.getUser());
    }

}