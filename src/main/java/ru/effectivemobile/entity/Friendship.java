package ru.effectivemobile.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "friendship")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private FriendshipStatus status;

    @OneToOne
    @JoinColumn(name = "main_user_id")
    private User mainUser;

    @OneToOne
    @JoinColumn(name = "target_user_id")
    private User targetUser;
}
