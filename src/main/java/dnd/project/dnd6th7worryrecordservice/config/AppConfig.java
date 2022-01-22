package dnd.project.dnd6th7worryrecordservice.config;

import dnd.project.dnd6th7worryrecordservice.service.KakaoService;
import dnd.project.dnd6th7worryrecordservice.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "dnd.project.dnd6th7worryrecordservice")
public class AppConfig {

    @Bean
    public UserService userService() { return new UserService();}

    @Bean
    public KakaoService kakaoService() { return new KakaoService(); }
}
