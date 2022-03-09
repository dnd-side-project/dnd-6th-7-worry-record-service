package dnd.project.dnd6th7worryrecordservice.domain.user;

import dnd.project.dnd6th7worryrecordservice.domain.worry.Worry;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@NoArgsConstructor
@Table(name = "USER")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = true)
    private String email;

    @Column(name = "role",nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "kakaoId",nullable = false)
    private String kakaoId;

    @Column(name = "imgUrl",nullable = false)
    private String imgUrl;

    @Column(name = "refreshToken", nullable = false)
    private String refreshToken;

    @Column(name = "deviceToken", nullable = false)
    private String deviceToken;

    @OneToMany(mappedBy = "user")
    List<Worry> worryList = new ArrayList<>();

    public User(String username, String email, String kakaoId, Role role, String imgUrl, String refreshToken, String deviceToken) {
        this.username = username;
        this.email = email;
        this.kakaoId = kakaoId;
        this.role = role;
        this.imgUrl = imgUrl;
        this.refreshToken = refreshToken;
        this.deviceToken = deviceToken;
    }

}