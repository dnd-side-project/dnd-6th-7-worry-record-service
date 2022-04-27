package dnd.project.dnd6th7worryrecordservice.jwt;

import com.google.gson.Gson;
import dnd.project.dnd6th7worryrecordservice.dto.jwt.JwtPayloadDto;
import dnd.project.dnd6th7worryrecordservice.dto.jwt.TokenDto;
import dnd.project.dnd6th7worryrecordservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {
    private final UserService userService;

    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        System.out.println("####### Interceptor preHandle Start!!!");

        String atJwtAccessToken = request.getHeader("at-jwt-access-token");
        String atJwtRefreshToken = request.getHeader("at-jwt-refresh-token");

        System.out.println("atJwtAccessToken = " + atJwtAccessToken);
        System.out.println("atJwtRefreshToken = " + atJwtRefreshToken);
        System.out.println("request method : " + request.getMethod());

        //preflight 대응
        if ("OPTIONS".equals(request.getMethod())) {
            System.out.println("request method is OPTIONS!!");
            return true;
        }

        //토큰 검증 및 재발급

        //When RefreshToken isn't in HTTP header
        if (atJwtRefreshToken == null) {
            //When AccessToken is in HTTP header
            if (atJwtAccessToken != null && atJwtAccessToken.length() > 0) {
                //validate AccessToken
                if (jwtUtil.validateToken(atJwtAccessToken)) return true;
                else {
                    log.error("Jwt AccessToken validate error!");
                    throw new IllegalArgumentException("Jwt AccessToken validate Error!");
                }
            }//When AccessToken isn't HTTP header
            else {
                log.error("Jwt AccessToken is empty!");
                throw new IllegalArgumentException("No Jwt AccessToken!");
            }
        }//When RefreshToken in HTTP header
        else {
            //validate RefreshToken
            if (jwtUtil.validateToken(atJwtRefreshToken)) {
                //Decode & Parse AccessToken to JwtPayloadDto
                String accessTokenPayload = jwtUtil.decodePayload(atJwtAccessToken);
                Gson gson = new Gson();
                JwtPayloadDto jwtPayload = gson.fromJson(accessTokenPayload, JwtPayloadDto.class);

                //Compare RefreshToken in DB with RefreshToken in HTTP header
                String refreshTokenInDB = userService.findRefreshTokenBySocialData(jwtPayload.getSocialId(), jwtPayload.getSocialType());

                if (refreshTokenInDB.equals(atJwtRefreshToken)) {
                    //Create new AccessToken and addHeader
                    TokenDto jwtToken = jwtUtil.createToken(jwtPayload.toUserRequestDto());
                    String jwtRefreshToken = jwtToken.getJwtRefreshToken();
                    String jwtAccessToken = jwtToken.getJwtAccessToken();

                    userService.updateRefreshTokenBySocialData(jwtRefreshToken, jwtPayload.getSocialId(), jwtPayload.getSocialType());
                    response.addHeader("at-jwt-access-token", jwtAccessToken);
                    response.addHeader("at-jwt-refresh-token", jwtRefreshToken);
                } else {
                    log.error("Jwt RefreshToken error!");
                    throw new IllegalArgumentException("RefreshToken Error!");
                }
                return true;
            } else {
                log.error("Jwt token error!");
                throw new IllegalArgumentException("Refresh Token Error!!!");
            }
        }

    }
}
