package dnd.project.dnd6th7worryrecordservice.jwt;

import dnd.project.dnd6th7worryrecordservice.dto.UserRequestDto;
import dnd.project.dnd6th7worryrecordservice.dto.UserResponseDto;
import dnd.project.dnd6th7worryrecordservice.dto.jwt.TokenDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;


@WebAppConfiguration
@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Test
    public void TokenCreateTest(){
        UserRequestDto userRequestDto = new UserRequestDto("username", "abbc@naver.com", "1231234","awdlawdnkawndlknflakwf.com");

        TokenDto tokens = jwtTokenProvider.createToken(userRequestDto);

        System.out.println("tokens.getJwtAccessToken = " + tokens.getJwtAccessToken());
        System.out.println("tokens.getJwtRefreshToken = " + tokens.getJwtRefreshToken());
    }

}