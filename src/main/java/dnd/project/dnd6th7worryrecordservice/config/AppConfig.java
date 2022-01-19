package dnd.project.dnd6th7worryrecordservice.config;

import dnd.project.dnd6th7worryrecordservice.domain.user.UserRepository;
import dnd.project.dnd6th7worryrecordservice.kakao.KakaoService;
import dnd.project.dnd6th7worryrecordservice.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.persistence.EntityManager;

@Configuration
@EnableJpaRepositories(basePackages = "dnd.project.dnd6th7worryrecordservice")
public class AppConfig {

    @Bean
    public UserServiceImpl userService() { return new UserServiceImpl();}

    @Bean
    public KakaoService kakaoService() { return new KakaoService(); }
}
