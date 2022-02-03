package dnd.project.dnd6th7worryrecordservice.controller;

import dnd.project.dnd6th7worryrecordservice.dto.UserRequestDto;
import dnd.project.dnd6th7worryrecordservice.dto.UserResponseDto;
import dnd.project.dnd6th7worryrecordservice.dto.jwt.TokenDto;
import dnd.project.dnd6th7worryrecordservice.jwt.JwtUtil;
import dnd.project.dnd6th7worryrecordservice.service.KakaoService;
import dnd.project.dnd6th7worryrecordservice.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RequestMapping("/auth")
@RestController
public class KakaoController {
    private final JwtUtil jwtUtil;
    private final KakaoService kakaoService;
    private final UserService userService;


    @ApiOperation(value = "토큰 발급", notes = "JWT AccessToken, RefreshToken 을 발급한다")
    @PostMapping(value = "/login")
    public ResponseEntity<UserResponseDto> giveToken(@RequestParam("token") String accessToken, HttpServletResponse res) {

        UserRequestDto userInfo = kakaoService.getUserInfo(accessToken);   //accessToken으로 유저정보 받아오기
        if (userInfo.getKakaoId() != null) {
            UserResponseDto userResponseDto = new UserResponseDto(userInfo.getUsername(), userInfo.getEmail(), userInfo.getImgURL());

            TokenDto tokens = jwtUtil.createToken(userInfo);
            userInfo.setRefreshToken(tokens.getJwtRefreshToken());
            //kakaoId 기준으로 DB select하여 User 데이터가 없으면 Insert, 있으면 Update
            userService.insertOrUpdateUser(userInfo);

            res.addHeader("at-jwt-access-token", tokens.getJwtAccessToken());
            res.addHeader("at-jwt-refresh-token", tokens.getJwtRefreshToken());


            return ResponseEntity.ok(userResponseDto);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }


    @ApiOperation(value = "토큰 발급 테스트", notes = "JWT AccessToken, RefreshToken 발급 테스트")
    @PostMapping(value = "/test")
    public ResponseEntity<UserResponseDto> tokenTest(HttpServletResponse res) {
        String username = "testUser";
        String kakaoId = "1234123";
        String email = "test@naver.com";
        String imgURL = "test.jpg";

        UserRequestDto userInfo = new UserRequestDto(username, email, kakaoId, imgURL);
        UserResponseDto userResponseDto = new UserResponseDto(username, email, imgURL);

        TokenDto tokens = jwtUtil.createToken(userInfo);
        userInfo.setRefreshToken(tokens.getJwtRefreshToken());

        userService.insertOrUpdateUser(userInfo);

        res.addHeader("at-jwt-access-token", tokens.getJwtAccessToken());
        res.addHeader("at-jwt-refresh-token", tokens.getJwtRefreshToken());

        return ResponseEntity.ok(userResponseDto);
    }

    //WebConfig addInterceptors 메서드에서 토큰 검증할 path 설정 가능
    @ApiOperation(value = "토큰 검증 테스트", notes = "JWT Token 검증 테스트")
    @GetMapping(value = "/validTest")
    public ResponseEntity<?> validTest() {

        return new ResponseEntity<>("success", HttpStatus.OK);
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

