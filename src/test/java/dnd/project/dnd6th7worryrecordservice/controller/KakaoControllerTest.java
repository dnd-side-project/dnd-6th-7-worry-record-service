package dnd.project.dnd6th7worryrecordservice.controller;

import dnd.project.dnd6th7worryrecordservice.dto.UserRequestDto;
import dnd.project.dnd6th7worryrecordservice.dto.UserResponseDto;
import dnd.project.dnd6th7worryrecordservice.dto.jwt.TokenDto;
import dnd.project.dnd6th7worryrecordservice.jwt.JwtUtil;
import dnd.project.dnd6th7worryrecordservice.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.util.NestedServletException;

import javax.transaction.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@WebAppConfiguration
@SpringBootTest
public class KakaoControllerTest {

    private WebApplicationContext context; // MockMvc 객체 생성을 위한 context
    private MockMvc mvc;

    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private WebApplicationContext ctx;

    @BeforeEach()
    public void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @AfterEach
    void resetDB() {
        userService.deleteUserByKakaoId("0000");
    }


    @Test
    @DisplayName("토큰 발급 테스트")
    public void giveTokenTest() throws Exception {
        mvc.perform(post("/auth/test"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("토큰 검증 테스트 - No token")
    public void verifyTokenTest1() throws Exception {
        String username = "testUser";
        String kakaoId = "0000";
        String email = "test@naver.com";
        String imgURL = "test.jpg";

        UserRequestDto userInfo = new UserRequestDto(username, email, kakaoId, imgURL);
        UserResponseDto userResponseDto = new UserResponseDto(username, email, imgURL);

        TokenDto tokens = jwtUtil.createToken(userInfo);

        try {
            mvc.perform(get("/auth/validTest"))
                    .andExpect(status().isOk()) // 호출 결과값이 OK가 나오면 정상처리
                    .andDo(print());// 결과를 print
        } catch (NestedServletException e) {
            System.out.println("No token");
        }
    }

    @Test
    @DisplayName("토큰 검증 테스트 - AccessToken")
    public void verifyTokenTest2() throws Exception {
        String username = "testUser";
        String kakaoId = "0000";
        String email = "test@naver.com";
        String imgURL = "test.jpg";

        UserRequestDto userInfo = new UserRequestDto(username, email, kakaoId, imgURL);
        UserResponseDto userResponseDto = new UserResponseDto(username, email, imgURL);

        TokenDto tokens = jwtUtil.createToken(userInfo);

        mvc.perform(get("/auth/validTest")
                        .header("at-jwt-access-token", tokens.getJwtAccessToken()))
                .andExpect(status().isOk()) // 호출 결과값이 OK가 나오면 정상처리
                .andDo(print());// 결과를 print

    }

    @Test
    @DisplayName("토큰 검증 테스트 - RefreshToken")
    public void verifyTokenTest3() throws Exception {
        String username = "testUser";
        String kakaoId = "0000";
        String email = "test@naver.com";
        String imgURL = "test.jpg";

        UserRequestDto userInfo = new UserRequestDto(username, email, kakaoId, imgURL);

        TokenDto tokens = jwtUtil.createToken(userInfo);
        userInfo.setRefreshToken(tokens.getJwtRefreshToken());
        userService.insertOrUpdateUser(userInfo);

        mvc.perform(get("/auth/validTest")
                        .header("at-jwt-access-token", tokens.getJwtAccessToken())
                        .header("at-jwt-refresh-token", tokens.getJwtRefreshToken()))
                .andExpect(status().isOk()) // 호출 결과값이 OK가 나오면 정상처리
                .andDo(print());// 결과를 print
    }
}
