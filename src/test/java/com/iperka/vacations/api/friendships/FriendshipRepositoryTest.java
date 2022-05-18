package com.iperka.vacations.api.friendships;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Optional;

import com.iperka.vacations.api.friendships.dto.FriendshipDTO;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

@DataMongoTest
class FriendshipRepositoryTest {
	@Autowired
	private FriendshipRepository friendshipRepository;

	@Test
	void shouldFindFriendshipById() {
		FriendshipDTO friendshipDTO = new FriendshipDTO();
		Friendship friendshipBeforeSave = friendshipDTO.toObject();
		friendshipBeforeSave.setOwner("test");
		friendshipBeforeSave.setUser("test");
		friendshipBeforeSave.setStatus(FriendshipStatus.ACCEPTED);

		Friendship friendship = friendshipRepository.save(friendshipBeforeSave);

		Optional<Friendship> result = friendshipRepository.findById(friendship.getId());
		assertEquals(true, result.isPresent());
		if (result.isPresent()) {
			assertEquals(true, result.get().getId().compareTo(friendship.getId()) == 0);
		}

		assertFalse(friendshipRepository.findById("invalidId").isPresent());
	}
}