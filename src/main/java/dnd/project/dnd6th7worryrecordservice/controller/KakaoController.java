package dnd.project.dnd6th7worryrecordservice.controller;

import dnd.project.dnd6th7worryrecordservice.domain.user.User;
import dnd.project.dnd6th7worryrecordservice.dto.UserRequestDto;
import dnd.project.dnd6th7worryrecordservice.dto.UserResponseDto;
import dnd.project.dnd6th7worryrecordservice.dto.jwt.TokenDto;
import dnd.project.dnd6th7worryrecordservice.jwt.JwtTokenProvider;
import dnd.project.dnd6th7worryrecordservice.kakao.KakaoService;
import dnd.project.dnd6th7worryrecordservice.service.UserServiceImpl;
import io.jsonwebtoken.lang.Assert;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("auth")
@RestController
public class KakaoController {
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoService kakaoService;
    private final UserServiceImpl userService;


    @ApiOperation(value = "토큰 발급", notes = "JWT AccessToken, RefreshToken 을 발급한다")
    @PostMapping(value = "/login")
    public ResponseEntity<UserResponseDto> getUserInfo(@RequestParam("token") String accessToken, HttpServletResponse res) {

        HashMap<String, String> userInfo = kakaoService.getUserInfo(accessToken);   //accessToken으로 유저정보 받아오기
        if(userInfo == null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        UserRequestDto userRequestDto = new UserRequestDto(userInfo.get(0), userInfo.get(1), userInfo.get(2), userInfo.get(3));
        UserResponseDto userResponseDto = new UserResponseDto(userInfo.get(0), userInfo.get(1), userInfo.get(3));

        DuplicateCheck(userInfo.get(2), userRequestDto);

        TokenDto tokens = jwtTokenProvider.createToken(userRequestDto);

        res.addHeader("at-jwt-access-token",tokens.getJwtAccessToken());
        res.addHeader("at-jwt-refresh-token", tokens.getJwtRefreshToken());

        return ResponseEntity.ok(userResponseDto);
    }




    //kakaoId로 중복 회원 체크
    private void DuplicateCheck(String kakaoId, UserRequestDto userRequestDto) {
        try{
            Optional<User> userByKakaoId = userService.findUserByKakaoId(kakaoId);
            Assert.notNull(userByKakaoId);
            userService.join(userRequestDto);
        }
        catch (NullPointerException e){
            System.out.println("이미 등록된 회원입니다.");    //printStackTrace 사용하려 했는데 보안 상 문제가 있다고 하여 sout사용
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

