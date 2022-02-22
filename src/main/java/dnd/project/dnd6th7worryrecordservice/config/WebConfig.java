package dnd.project.dnd6th7worryrecordservice.config;

import dnd.project.dnd6th7worryrecordservice.jwt.JwtInterceptor;
import dnd.project.dnd6th7worryrecordservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@RequiredArgsConstructor
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private final UserService userService;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        System.out.println("####### Register Interceptor: JwtInterceptor!!!");
        registry.addInterceptor(jwtInterceptor()).addPathPatterns("/worries/**");   //토큰을 검증할 path
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST")
                .exposedHeaders("at-jwt-access-token", "at-jwt-refresh-token");
    }

    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor(userService);
    }
}
