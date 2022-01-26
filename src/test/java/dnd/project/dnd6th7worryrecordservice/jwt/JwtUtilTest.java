package dnd.project.dnd6th7worryrecordservice.jwt;

import dnd.project.dnd6th7worryrecordservice.dto.UserRequestDto;
import dnd.project.dnd6th7worryrecordservice.dto.jwt.TokenDto;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import javax.crypto.SecretKey;

@WebAppConfiguration
@SpringBootTest
class JwtUtilTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Value("${jwt.secret}")
    private String secret;

    @Test
    @DisplayName("토큰 생성 테스트")
    public void TokenCreateTest(){
        UserRequestDto userRequestDto = new UserRequestDto("username", "abbc@naver.com", "1231234","awdlawdnkawndlknflakwf.com");

        TokenDto tokens = jwtUtil.createToken(userRequestDto);

        System.out.println("tokens.getJwtAccessToken = " + tokens.getJwtAccessToken());
        System.out.println("tokens.getJwtRefreshToken = " + tokens.getJwtRefreshToken());
    }

    @Test
    @DisplayName("JWT 시크릿 키 생성 테스트")
    public void SecretKeyGenerateTest(){
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        System.out.println("key = " + key);

        String SecretKey = Encoders.BASE64URL.encode(key.getEncoded());
        System.out.println("SecretKey = " + SecretKey);

        SecretKey decodedKey = Keys.hmacShaKeyFor(Decoders.BASE64URL.decode(SecretKey));
        System.out.println("decodedKey = " + decodedKey);
    }

    @Test
    @DisplayName("토큰 검증 테스트")
    public void validateTest(){

        UserRequestDto userRequestDto = new UserRequestDto("username", "abbc@naver.com", "1231234","awdlawdnkawndlknflakwf.com");
        TokenDto tokens = jwtUtil.createToken(userRequestDto);

        jwtUtil.validate(tokens.getJwtAccessToken());

    }

    @Test
    @DisplayName("decode 테스트")
    public void decodeTest(){
        UserRequestDto userRequestDto = new UserRequestDto("username", "abbc@naver.com", "1231234","awdlawdnkawndlknflakwf.com");
        TokenDto tokens = jwtUtil.createToken(userRequestDto);

        String decode = jwtUtil.decodePayload(tokens.getJwtAccessToken());

        System.out.println("decode = " + decode);
    }

}