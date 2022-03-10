package dnd.project.dnd6th7worryrecordservice.api;

import dnd.project.dnd6th7worryrecordservice.domain.user.User;
import dnd.project.dnd6th7worryrecordservice.dto.jwt.TokenDto;
import dnd.project.dnd6th7worryrecordservice.dto.user.UserRequestDto;
import dnd.project.dnd6th7worryrecordservice.dto.user.UserResponseDto;
import dnd.project.dnd6th7worryrecordservice.jwt.JwtUtil;
import dnd.project.dnd6th7worryrecordservice.service.KakaoService;
import dnd.project.dnd6th7worryrecordservice.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class AuthController {
    private final JwtUtil jwtUtil;
    private final KakaoService kakaoService;
    private final UserService userService;

    @ApiOperation(value = "카카오 로그인", notes = "카카오 계정으로 로그인 후 ResponeHeader로 JWT AccessToken, RefreshToken 을 발급한다")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "token"
                    , value = "카카오 엑세스 토큰"),
            @ApiImplicitParam(
                    name = "deviceToken"
                    , value = "FCM서버에서 전송 받는 푸쉬알림을 위한 토큰")
    })
    @PostMapping(value = "/kakao")
    //Token값 헤더로 받도록 변경 필요
    public ResponseEntity<UserResponseDto> kakaoLogin(@RequestParam("token") String accessToken, @RequestParam("deviceToken") String deviceToken, HttpServletResponse res) {
        System.out.println("accessToken = " + accessToken);
        UserRequestDto userInfo = kakaoService.getUserInfo(accessToken);   //accessToken으로 유저정보 받아오기
        userInfo.setDeviceToken(deviceToken);   //userInfo에 deviceToken 추가
        if (userInfo.getSocialId() != null) {
            TokenDto tokens = jwtUtil.createToken(userInfo);
            userInfo.setRefreshToken(tokens.getJwtRefreshToken());

            //socialId 기준으로 DB select하여 User 데이터가 없으면 Insert, 있으면 Update
            userService.insertOrUpdateUser(userInfo);

            Optional<User> userBySocialData = userService.findUserBySocialData(userInfo.getSocialId(), userInfo.getSocialType());

            //UserResponseDto에 userId 추가
            UserResponseDto userResponseDto = new UserResponseDto(userBySocialData.get().getUserId(), userInfo.getUsername(), userInfo.getEmail(), userInfo.getImgURL());

            res.addHeader("at-jwt-access-token", tokens.getJwtAccessToken());
            res.addHeader("at-jwt-refresh-token", tokens.getJwtRefreshToken());

            return ResponseEntity.ok(userResponseDto);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "애플 로그인", notes = "애플 계정으로 로그인 후 ResponeHeader로 JWT AccessToken, RefreshToken 을 발급한다")
    @PostMapping(value = "/apple")
    public ResponseEntity<UserResponseDto> appleLogin(@RequestParam("token") String accessToken, @RequestParam("deviceToken") String deviceToken, HttpServletResponse res){

    }

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
    public ResponseEntity<?> refreshDeviceToken(@RequestParam("userId") Long userId, @RequestParam("deviceToken") String deviceToken){
        try {
            userService.updateDeviceToken(deviceToken, userId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
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

