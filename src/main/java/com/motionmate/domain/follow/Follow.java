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

    @ManyToOne
    @JoinColumn(name= "follower_id")
    private User fromUser; // 나를 팔로우하는 유저

    @ManyToOne
    @JoinColumn(name= "following_id")
    private User toUser; // 내가 팔로우하는 유저

    public Follow(User fromUser, User toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
    }
}
