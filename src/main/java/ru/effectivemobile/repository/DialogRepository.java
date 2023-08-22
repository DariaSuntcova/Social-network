package ru.effectivemobile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.effectivemobile.entity.Dialog;
import ru.effectivemobile.entity.User;

import java.util.Optional;

public interface DialogRepository extends JpaRepository<Dialog, Long> {
    Optional<Dialog> findByMainUserAndTargetUser(User targetUser, User mainUser);
}
