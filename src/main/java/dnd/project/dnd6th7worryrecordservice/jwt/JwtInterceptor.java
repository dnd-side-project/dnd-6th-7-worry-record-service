package dnd.project.dnd6th7worryrecordservice.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class JwtInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        System.out.println("####### Interceptor preHandle Start!!!");

        String atJwtToken = request.getHeader("at-jwt-access-token");

        if(atJwtToken != null && atJwtToken.length() > 0) {
            if(jwtUtil.validate(atJwtToken)) return true;
            else throw new IllegalArgumentException("Token Error!!!");
        }else {
            throw new IllegalArgumentException("No Token!!!");
        }
    }
}
