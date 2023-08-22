package ru.effectivemobile.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.effectivemobile.entity.Friendship;
import ru.effectivemobile.entity.FriendshipStatus;
import ru.effectivemobile.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    Optional<Friendship> findByMainUserAndTargetUser(User mainUser, User targetUser);

    List<Friendship> findByMainUserAndStatus(User user, FriendshipStatus status, Pageable pageable);

    List<Friendship> findAllByMainUserAndStatus(User user, FriendshipStatus status);

}


