package com.motionmate.domain.follow;

import com.motionmate.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Follow {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name= "follower_id")
    private User follower; // 나를 팔로우하는 유저

    @ManyToOne
    @JoinColumn(name= "following_id")
    private User following; // 내가 팔로우하는 유저

    public Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }
}
