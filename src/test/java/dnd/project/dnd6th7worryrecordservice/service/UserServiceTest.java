package dnd.project.dnd6th7worryrecordservice.service;

import dnd.project.dnd6th7worryrecordservice.domain.user.User;
import dnd.project.dnd6th7worryrecordservice.dto.UserRequestDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @AfterEach
    void resetDB(){
        userService.deleteUserByKakaoId("0000");
    }

    @Test
    @DisplayName("insertOrUpdateUser 테스트")
    void insertOrUpdateUserTest() {
        String username = "testUser";
        String kakaoId ="0000";
        String email = "test@naver.com";
        String imgURL = "test.jpg";
        String refreshToken = "insertTest";

        System.out.println(":: INSERT ::");
        UserRequestDto userInfo = new UserRequestDto(username, email, kakaoId, imgURL,refreshToken);
        userService.insertOrUpdateUser(userInfo);
        System.out.println("userInfo.toString() = " + userInfo.toString());

        System.out.println(":: UPDATE ::");
        refreshToken = "updateTest";
        userInfo.setRefreshToken(refreshToken);
        userService.insertOrUpdateUser(userInfo);

        System.out.println("userInfo.toString() = " + userInfo.toString());

    }

    @Test
    @DisplayName("카카오 아이디로 유저 찾기 테스트")
    void findUserByKakaoId() {
        String username = "testUser";
        String kakaoId ="0000";
        String email = "test@naver.com";
        String imgURL = "test.jpg";
        String refreshToken = "insertTest";

        UserRequestDto userInfo = new UserRequestDto(username, email, kakaoId, imgURL,refreshToken);
        userService.insertOrUpdateUser(userInfo);

        Optional<User> userByKakaoId = userService.findUserByKakaoId(userInfo.getKakaoId());

        System.out.println("kakaoId = " + userInfo.getKakaoId());
        System.out.println("findKakaoId = " + userByKakaoId.get().getKakaoId());

        Assertions.assertSame(userByKakaoId.get().getKakaoId(), userInfo.getKakaoId());
    }

    @Test
    @DisplayName("카카오 아이디로 RefreshToken 찾기 테스트")
    void findRefreshTokenByKakaoId() {
        String username = "testUser";
        String kakaoId ="0000";
        String email = "test@naver.com";
        String imgURL = "test.jpg";
        String refreshToken = "refreshToken";

        UserRequestDto userInfo = new UserRequestDto(username, email, kakaoId, imgURL,refreshToken);
        userService.insertOrUpdateUser(userInfo);

        String DBrefreshToken = userService.findRefreshTokenByKakaoId(userInfo.getKakaoId());

        System.out.println("userInfo.getRefreshToken() = " + userInfo.getRefreshToken());
        System.out.println("Database.refreshToken = " + DBrefreshToken);

        Assertions.assertEquals(DBrefreshToken, refreshToken);
    }
}