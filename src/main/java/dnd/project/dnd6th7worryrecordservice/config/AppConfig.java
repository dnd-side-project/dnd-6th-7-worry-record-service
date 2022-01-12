package dnd.project.dnd6th7worryrecordservice.config;

import com.amazonaws.services.s3.AmazonS3Client;
import dnd.project.dnd6th7worryrecordservice.aws.S3Uploader;
import dnd.project.dnd6th7worryrecordservice.repository.JpaUserRepository;
import dnd.project.dnd6th7worryrecordservice.repository.UserRepository;
import dnd.project.dnd6th7worryrecordservice.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class AppConfig {
    private EntityManager em;


    @Autowired
    public AppConfig(EntityManager em){
        this.em = em;
    }


    @Bean
    public UserRepository userRepository() { return new JpaUserRepository(em);}

    @Bean
    public UserServiceImpl userService() { return new UserServiceImpl(userRepository());}

}
