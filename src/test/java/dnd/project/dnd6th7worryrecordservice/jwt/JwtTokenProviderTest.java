package dnd.project.dnd6th7worryrecordservice.jwt;

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
        String token = jwtTokenProvider.createToken("username", "abbc@naver.com", "awdlawdnkawndlknflakwf.com");

        System.out.println("token = " + token);
    }

}