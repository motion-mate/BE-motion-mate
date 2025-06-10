package com.motionmate.domain.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "profileImage")
public class UserProfileImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long no;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String bucketKey;

    @Column(nullable = false)
    private String orgName;

    @OneToOne
    @JoinColumn(name = "user_profile_id", nullable = false)
    private UserProfile userProfile;

    @Builder
    public UserProfileImage(String url, String bucketKey, String orgName, UserProfile userProfile) {
        this.url = url;
        this.bucketKey = bucketKey;
        this.orgName = orgName;
        this.userProfile = userProfile;

        if (userProfile != null) {
            userProfile.setUserProfileImage(this);
        }
    }
}
