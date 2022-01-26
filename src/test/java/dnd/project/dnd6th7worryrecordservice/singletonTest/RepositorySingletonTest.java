package dnd.project.dnd6th7worryrecordservice.singletonTest;

import dnd.project.dnd6th7worryrecordservice.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RepositorySingletonTest {

    @Autowired
    private UserRepository userRepository;

    @DisplayName("레퍼지토리 싱글톤 테스트")
    @RepeatedTest(3)
    public void repositoryCheck(){
        System.out.println("userRepository = " + userRepository);
    }
}
