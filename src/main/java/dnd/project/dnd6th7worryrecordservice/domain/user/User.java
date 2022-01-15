package dnd.project.dnd6th7worryrecordservice.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;

import javax.persistence.*;

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


    public User(String username, String email, String kakaoId, Role role, String imgUrl) {
        Assert.hasText(username, "username must not be empty");
        Assert.hasText(kakaoId, "kakaoId must not be empty");
        Assert.hasText(role.name(), "role must not be empty");
        Assert.hasText(imgUrl, "imgUrl must not be empty");

        this.username = username;
        this.email = email;
        this.kakaoId = kakaoId;
        this.role = role;
        this.imgUrl = imgUrl;
    }



}