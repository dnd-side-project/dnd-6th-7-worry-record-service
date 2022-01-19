package dnd.project.dnd6th7worryrecordservice.controller;

import dnd.project.dnd6th7worryrecordservice.dto.UserRequestDto;
import dnd.project.dnd6th7worryrecordservice.dto.UserResponseDto;
import dnd.project.dnd6th7worryrecordservice.jwt.JwtTokenProvider;
import dnd.project.dnd6th7worryrecordservice.kakao.KakaoService;
import dnd.project.dnd6th7worryrecordservice.service.UserServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;

@RequiredArgsConstructor
@RequestMapping("user")
@RestController
public class KakaoController {
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoService kakaoService;
    private final UserServiceImpl userService;

    @ApiOperation(value = "토큰 발급", notes = "JWT AccessToken, RefreshToken 을 발급한다")
    @PostMapping(value = "/registry")
    public ResponseEntity<UserResponseDto> getUserInfo(@RequestParam("token") String accessToken, HttpSession session) {

        System.out.println("accessToken = " + accessToken);

        HashMap<String, String> userInfo = kakaoService.getUserInfo(accessToken);   //accessToken으로 유저정보 받아오기

        UserRequestDto userRequestDto = new UserRequestDto(userInfo.get(0), userInfo.get(1), userInfo.get(2), userInfo.get(3));
        userService.join(userRequestDto);

        String token = jwtTokenProvider.createToken(userRequestDto.getUsername(), userRequestDto.getEmail(), userRequestDto.getImgURL());
        String refreshToken = jwtTokenProvider.createRefreshToken(userRequestDto.getUsername(), userRequestDto.getEmail());

        UserResponseDto userResponseDto = new UserResponseDto(token, refreshToken);

        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
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

