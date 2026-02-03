package com.linkdeco.link_deco.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)   // 로그인 아이디로 사용할 이메일
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 10)    // 닉네임 중복 가능
    private String nickname;

    @Column(length = 500)
    private String profileImage;

    @Builder
    public Member(String email, String password, String nickname, String profileImage) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public void updateProfileImage(String imageUrl) {
        this.profileImage = imageUrl;
    }

    public String getProfileImageKey() {
        return profileImage.substring(profileImage.lastIndexOf("/") + 1);
    }
}
