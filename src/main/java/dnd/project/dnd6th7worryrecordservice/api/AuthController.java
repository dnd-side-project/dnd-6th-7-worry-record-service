package dnd.project.dnd6th7worryrecordservice.api;

import dnd.project.dnd6th7worryrecordservice.domain.user.User;
import dnd.project.dnd6th7worryrecordservice.dto.user.UserRequestDto;
import dnd.project.dnd6th7worryrecordservice.dto.user.UserResponseDto;
import dnd.project.dnd6th7worryrecordservice.dto.jwt.TokenDto;
import dnd.project.dnd6th7worryrecordservice.jwt.JwtUtil;
import dnd.project.dnd6th7worryrecordservice.service.KakaoService;
import dnd.project.dnd6th7worryrecordservice.service.UserService;
import io.swagger.annotations.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
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

    @ApiOperation(value = "토큰 발급", notes = "JWT AccessToken, RefreshToken 을 발급한다")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message = "No param")
            //Other Http Status code..
    })
    @ApiImplicitParam(
            name = "token"
            , value = "카카오 엑세스 토큰"
            , defaultValue = "None")
    @PostMapping(value = "/kakao")
    public ResponseEntity<UserResponseDto> giveToken(@RequestParam("token") String accessToken, HttpServletResponse res) {
        System.out.println("accessToken = " + accessToken);
        UserRequestDto userInfo = kakaoService.getUserInfo(accessToken);   //accessToken으로 유저정보 받아오기
        if (userInfo.getKakaoId() != null) {
            TokenDto tokens = jwtUtil.createToken(userInfo);
            userInfo.setRefreshToken(tokens.getJwtRefreshToken());

            //kakaoId 기준으로 DB select하여 User 데이터가 없으면 Insert, 있으면 Update
            userService.insertOrUpdateUser(userInfo);

            Optional<User> userByKakaoId = userService.findUserByKakaoId(userInfo.getKakaoId());

            //UserResponseDto에 userId 추가
            UserResponseDto userResponseDto = new UserResponseDto(userByKakaoId.get().getUserId(), userInfo.getUsername(), userInfo.getEmail(), userInfo.getImgURL());

            res.addHeader("at-jwt-access-token", tokens.getJwtAccessToken());
            res.addHeader("at-jwt-refresh-token", tokens.getJwtRefreshToken());

            return ResponseEntity.ok(userResponseDto);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
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

