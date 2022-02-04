package dnd.project.dnd6th7worryrecordservice.singleton;

import dnd.project.dnd6th7worryrecordservice.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class SingletonTest {

    @Autowired
    private UserService userService1;
    @Autowired
    private UserService userService2;

    @Test
    public void UserServiceSingletonTest(){
        System.out.println("userService1 = " + userService1);
        System.out.println("userService2 = " + userService2);

        Assertions.assertThat(userService1).isSameAs(userService2);
    }
}
