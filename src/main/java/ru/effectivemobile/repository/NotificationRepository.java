package ru.effectivemobile.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.effectivemobile.entity.Notification;
import ru.effectivemobile.entity.User;

import java.util.List;
@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByTargetUserAndItsRead(User targetUser, boolean itsRead);


    List<Notification> findByTargetUser(User user, Pageable pageable);
}
