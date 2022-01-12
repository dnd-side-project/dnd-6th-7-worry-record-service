package dnd.project.dnd6th7worryrecordservice.service;

import dnd.project.dnd6th7worryrecordservice.domain.user.Role;
import dnd.project.dnd6th7worryrecordservice.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @Test
    public void joinTest(){
        User user = new User("name","email","kakaoId", Role.USER.name(),"imgUrl");

    }
}