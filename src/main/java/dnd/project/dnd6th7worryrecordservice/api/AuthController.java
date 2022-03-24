package dnd.project.dnd6th7worryrecordservice.api;

import dnd.project.dnd6th7worryrecordservice.domain.user.User;
import dnd.project.dnd6th7worryrecordservice.dto.jwt.TokenDto;
import dnd.project.dnd6th7worryrecordservice.dto.user.UserInfoDto;
import dnd.project.dnd6th7worryrecordservice.dto.user.UserResponseDto;
import dnd.project.dnd6th7worryrecordservice.jwt.JwtUtil;
import dnd.project.dnd6th7worryrecordservice.service.AppleService;
import dnd.project.dnd6th7worryrecordservice.service.KakaoService;
import dnd.project.dnd6th7worryrecordservice.service.UserService;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {

    @Autowired
    JwtUtil jwtUtil;
    private final KakaoService kakaoService;
    private final UserService userService;
    private final AppleService appleService;

    @ApiOperation(value = "KAKAO OAuth2 로그인", notes = "카카오 계정으로 로그인 후 ResponseHeader로 JWT AccessToken, RefreshToken 을 발급한다")
    @PostMapping(value = "/kakao")
    //Token값 헤더로 받도록 변경 필요
    public ResponseEntity<UserResponseDto> kakaoLogin(@RequestHeader("oauthToken") String accessToken, @RequestHeader("deviceToken") String deviceToken, HttpServletResponse res) {
        System.out.println("accessToken = " + accessToken);

        try {
            // OAuth2 Token으로 유저정보 받아오기
            UserInfoDto userInfo = kakaoService.getUserInfo(accessToken);
            userInfo.setDeviceToken(deviceToken);   //userInfo에 deviceToken 추가

            //UserInfo NullCheck
            Assert.notNull(userInfo.getSocialId());

            TokenDto tokens = jwtUtil.createToken(userInfo);
            userInfo.setRefreshToken(tokens.getJwtRefreshToken());

            //socialId 기준으로 DB select하여 User 데이터가 없으면 Insert, 있으면 Update
            userService.insertOrUpdateUser(userInfo);

            Optional<User> userBySocialData = userService.findUserBySocialData(userInfo.getSocialId(), userInfo.getSocialType());

            //UserResponseDto에 userId 추가
            UserResponseDto userResponseDto = new UserResponseDto(userBySocialData.get().getUserId(), userInfo.getUsername(), userInfo.getEmail(), userInfo.getImgURL());

            res.addHeader("at-jwt-access-token", tokens.getJwtAccessToken());
            res.addHeader("at-jwt-refresh-token", tokens.getJwtRefreshToken());

            return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity("Request Value Error", HttpStatus.NOT_FOUND);
        }
    }

//    @ApiOperation(value = "APPLE OAuth2 로그인", notes = "애플 계정으로 로그인 후 ResponseHeader로 JWT AccessToken, RefreshToken 을 발급한다")
//    @PostMapping(value = "/apple")
//    //Token값 헤더로 받도록 변경 필요
//    public ResponseEntity<UserResponseDto> appleLogin(@RequestHeader("oauthToken") String identityToken, @RequestHeader("deviceToken") String deviceToken, HttpServletResponse res) {
//        System.out.println("identityToken = " + identityToken);
//
//        // OAuth2 Token으로 유저정보 받아오기
//        UserInfoDto userInfo = appleService.getUserInfo(identityToken);
//        userInfo.setDeviceToken(deviceToken);   //userInfo에 deviceToken 추가
//
//        try {
//            //UserInfo NullCheck
//            Assert.notNull(userInfo.getSocialId());
//
//            TokenDto tokens = jwtUtil.createToken(userInfo);
//            userInfo.setRefreshToken(tokens.getJwtRefreshToken());
//
//            //socialId 기준으로 DB select하여 User 데이터가 없으면 Insert, 있으면 Update
//            userService.insertOrUpdateUser(userInfo);
//
//            Optional<User> userBySocialData = userService.findUserBySocialData(userInfo.getSocialId(), userInfo.getSocialType());
//
//            //UserResponseDto에 userId 추가
//            UserResponseDto userResponseDto = new UserResponseDto(userBySocialData.get().getUserId(), userInfo.getUsername(), userInfo.getEmail(), userInfo.getImgURL());
//
//            res.addHeader("at-jwt-access-token", tokens.getJwtAccessToken());
//            res.addHeader("at-jwt-refresh-token", tokens.getJwtRefreshToken());
//
//            return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
//        } catch (Exception e){
//            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
//        }
//    }

    @ApiOperation(value = "FCM서버 디바이스 토큰 갱신", notes = "새로 발급된 DeviceToken을 DB에 저장한다")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "userId"
                    , value = "유저PK"),
            @ApiImplicitParam(
                    name = "deviceToken"
                    , value = "FCM서버에서 전송 받는 푸쉬알림을 위한 토큰")
    })
    @PutMapping(value = "/refresh")
    public ResponseEntity<?> refreshDeviceToken(@RequestParam("userId") Long userId, @RequestParam("deviceToken") String deviceToken) {
        try {
            userService.updateDeviceToken(deviceToken, userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

//    @ApiOperation(value = "토큰 재발급", notes = "토큰을 재발급한다")
//    @PostMapping(value = "/refresh")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "access-token", required = true, dataType = "String", paramType = "header"),
//            @ApiImplicitParam(name = "REFRESH-TOKEN", value = "refresh-token", required = true, dataType = "String", paramType = "header")
//    }) public ResponseEntity<UserResponseDto> refreshToken(
//            @RequestHeader(value="X-AUTH-TOKEN") String token,
//            @RequestHeader(value="REFRESH-TOKEN") String refreshToken ) {
//        return responseService.handleSingleResult(signService.refreshToken(token, refreshToken));
//    } 
//
//}

