package com.motionmate.domain.follow;

import com.motionmate.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "follower_id")
    private User fromUser; // 팔로우를 건 사람

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "following_id")
    private User toUser; // 팔로우를 받는 사람

    public Follow(User fromUser, User toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
    }
}
