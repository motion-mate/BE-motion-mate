package com.motionmate.domain.notification;

import com.motionmate.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.sql.ConnectionBuilder;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private User user;

    private String type;
    private String content;
    private boolean isRead;
    private LocalDateTime createdAt;

    public Notification(User user, String type, String content) {
        this.user = user;
        this.type = type;
        this.content = content;
        this.isRead = false;
        this.createdAt = LocalDateTime.now();
    }

    public void markAsRead() {
        this.isRead = true;
    }

    public void markAsReadAll() {
        this.isRead = true;
    }
}
