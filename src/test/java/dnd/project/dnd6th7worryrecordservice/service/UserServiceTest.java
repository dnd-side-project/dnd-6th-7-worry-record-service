package dnd.project.dnd6th7worryrecordservice.service;

import dnd.project.dnd6th7worryrecordservice.domain.user.SocialType;
import dnd.project.dnd6th7worryrecordservice.domain.user.User;
import dnd.project.dnd6th7worryrecordservice.dto.user.UserInfoDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;


    //테스트 유저 ArrayList에 삽입
    private ArrayList<UserInfoDto> userDtoList = new ArrayList<>();
    @BeforeEach
    public void userBeforeSetting(){

        UserInfoDto user1 = new UserInfoDto("T_User1","T_email1","T_socialId1",SocialType.KAKAO,"T_imgUrl1","T_refreshToken1","T_deviceToken1");
        UserInfoDto user2 = new UserInfoDto("T_User2","T_email2","T_socialId2",SocialType.APPLE,"T_imgUrl2","T_refreshToken2","T_deviceToken2");
        UserInfoDto user3 = new UserInfoDto("T_User3","T_email3","T_socialId3",SocialType.KAKAO,"T_imgUrl3","T_refreshToken3","T_deviceToken3");

        userDtoList.add(user1);
        userDtoList.add(user2);
        userDtoList.add(user3);

        userService.insertOrUpdateUser(user1);
        userService.insertOrUpdateUser(user2);
        userService.insertOrUpdateUser(user3);
    }
    //DB에서 테스트 유저 삭제
    @AfterEach
    public void userAfterSetting(){
        if(userService.findUserBySocialData("T_socialId1",SocialType.KAKAO).isPresent());
            userService.deleteUserBySocialData("T_socialId1",SocialType.KAKAO);
        if(userService.findUserBySocialData("T_socialId2",SocialType.APPLE).isPresent());
            userService.deleteUserBySocialData("T_socialId2",SocialType.APPLE);
        if(userService.findUserBySocialData("T_socialId3",SocialType.KAKAO).isPresent());
            userService.deleteUserBySocialData("T_socialId3",SocialType.KAKAO);
    }

    @Test
    @DisplayName("유저 삽입/업데이트 TEST")
    void insertOrUpdateUser() {
        // Test 유저 데이터 생성
        for (UserInfoDto userInfoDto : userDtoList) {
            userService.insertOrUpdateUser(userInfoDto);
        }

        System.out.println("================== 검증 ==================");
        User findUser = userService.findUserBySocialData("T_socialId1", SocialType.KAKAO).get();
        Assertions.assertThat(findUser.getSocialId()).isEqualTo("T_socialId1");
    }

    @Test
    @DisplayName("유저 전체 찾기 TEST")
    void findAllUser() {
        List<User> allUser = userService.findAllUser();
        for (User user : allUser) {
            System.out.println("userName = " + user.getUsername());
        }
    }

    @Test
    @DisplayName("유저ID로 유저 찾기 TEST")
    void findUserByUserId() {
        User user = userService.findUserBySocialData("T_socialId1", SocialType.KAKAO).get();

        User findUser = userService.findUserByUserId(user.getUserId()).get();

        System.out.println("================== 검증 ==================");
        Assertions.assertThat(findUser.getSocialId()).isEqualTo("T_socialId1");
        System.out.println("userName = " + findUser.getUsername());
    }

    @Test
    @DisplayName("소셜데이터로 유저 찾기 TEST")
    void findUserBySocialData(String t_socialId1, SocialType kakao) {
        User user = userService.findUserBySocialData("T_socialId1", SocialType.KAKAO).get();

    }

    @Test
    @DisplayName("소셜데이터로 유저 업데이트 TEST")
    void updateUserBySocialData() {
        UserInfoDto newUser1Data = new UserInfoDto("T_User_update","T_email1","T_socialId1",SocialType.KAKAO,"T_imgUrl1","T_refreshToken1","T_deviceToken1");

        userService.updateUserBySocialData(newUser1Data);
        User updatedUser = userService.findUserBySocialData("T_socialId1", SocialType.KAKAO).get();

        System.out.println("================== 검증 ==================");
        Assertions.assertThat("T_User_update").isEqualTo(updatedUser.getUsername());

    }

    @Test
    @DisplayName("디바이스 토큰 업데이트 TEST")
    void updateDeviceToken() {
        User BeforeUser = userService.findUserBySocialData("T_socialId1", SocialType.KAKAO).get();
        String BeforeDeviceToken = BeforeUser.getDeviceToken();
        userService.updateDeviceToken("newDeviceToken",BeforeUser.getUserId());

        System.out.println("================== 검증 ==================");
        User AfterUser = userService.findUserBySocialData("T_socialId1", SocialType.KAKAO).get();
        Assertions.assertThat(AfterUser.getDeviceToken()).isNotEqualTo(BeforeDeviceToken);
    }

    @Test
    @DisplayName("소셜데이터로 JWT 리프레쉬토큰 찾기 TEST")
    void findRefreshTokenBySocialData() {
        String findRefreshToken = userService.findRefreshTokenBySocialData("T_socialId1", SocialType.KAKAO);

        System.out.println("================== 검증 ==================");
        Assertions.assertThat(findRefreshToken).isEqualTo("T_refreshToken1");
    }

    @Test
    @DisplayName("소셜데이터로 유저 데이터 삭제 TEST")
    void deleteUserBySocialData() {
        UserInfoDto user = new UserInfoDto("T_User4","T_email4","T_socialId4",SocialType.KAKAO,"T_imgUrl4","T_refreshToken4","T_deviceToken4");
        userService.insertOrUpdateUser(user);

        System.out.println("================== 검증 ==================");
        userService.deleteUserBySocialData(user.getSocialId(), user.getSocialType());
        Assertions.assertThat(userService.findUserBySocialData(user.getSocialId(),user.getSocialType()).isPresent()).isFalse();

    }

    @Test
    @DisplayName("소셜데이터로 JWT 리프레쉬 토큰 업데이트 TEST")
    void updateRefreshTokenBySocialData() {
        User BeforeUser = userService.findUserBySocialData("T_socialId1", SocialType.KAKAO).get();
        userService.updateRefreshTokenBySocialData("newRefreshToken","T_socialId1", SocialType.KAKAO);

        System.out.println("================== 검증 ==================");
        User AfterUser = userService.findUserBySocialData("T_socialId1", SocialType.KAKAO).get();
        Assertions.assertThat(BeforeUser.getRefreshToken()).isNotEqualTo(AfterUser.getRefreshToken());
    }

    @Test
    @DisplayName("Id@Type 형식 소셜데이터로 유저데이터 찾기 TEST")
    void findSecurityUserBySocialData() {
        String socialIdAndType = "T_socialId1@" + SocialType.KAKAO.toString();
        Optional<User> findUser = userService.findSecurityUserBySocialData(socialIdAndType);

        System.out.println("================== 검증 ==================");
        Assertions.assertThat(findUser.isPresent()).isTrue();
    }
}