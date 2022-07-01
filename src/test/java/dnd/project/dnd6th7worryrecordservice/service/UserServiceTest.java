package dnd.project.dnd6th7worryrecordservice.service;

import dnd.project.dnd6th7worryrecordservice.domain.user.Role;
import dnd.project.dnd6th7worryrecordservice.domain.user.SocialType;
import dnd.project.dnd6th7worryrecordservice.domain.user.User;
import dnd.project.dnd6th7worryrecordservice.dto.user.UserInfoDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;


    @Test
    void insertOrUpdateUser() {
        // Test 유저 데이터 생성
        String testSocialId = "T_socialId";
        UserInfoDto userInfoDto = new UserInfoDto("T_User","T_email",testSocialId,SocialType.KAKAO,"T_imgUrl");
        userInfoDto.setRefreshToken("T_refreshToken");
        userInfoDto.setDeviceToken("T_deviceToken");
        userService.insertOrUpdateUser(userInfoDto);

        User findUser = userService.findUserBySocialData("T_socialId", SocialType.KAKAO).get();
        System.out.println("================== 검증 ==================");
        Assertions.assertThat(findUser.getSocialId()).isEqualTo(testSocialId);

        System.out.println("================== 테스트 데이터 삭제 ==================");
        userService.deleteUserBySocialData("T_socialId", SocialType.KAKAO);
    }

    @Test
    void findAllUser() {
    }

    @Test
    void findUserByUserId() {
    }

    @Test
    void findUserBySocialData() {
    }

    @Test
    void updateUserBySocialData() {
    }

    @Test
    void updateDeviceToken() {
    }

    @Test
    void findRefreshTokenBySocialData() {
    }

    @Test
    void deleteUserBySocialData() {
    }

    @Test
    void updateRefreshTokenBySocialData() {
    }

    @Test
    void findSecurityUserBySocialData() {
    }
}