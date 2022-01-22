package dnd.project.dnd6th7worryrecordservice.service;

import dnd.project.dnd6th7worryrecordservice.domain.user.User;
import dnd.project.dnd6th7worryrecordservice.dto.UserRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;


@WebAppConfiguration
@SpringBootTest
class UserServiceTest {

    @Test
    public void requestDtoToEntityTest(){

        UserRequestDto userRequestDto = new UserRequestDto("username","email","kakaoId","imgURL");
        User user = userRequestDto.toEntity();

        System.out.println("user.getUsername(),user.getEmail(),user.getKakaoId(),user.getRole(),user.getImgUrl() = " + user.getUsername() + user.getEmail() + user.getKakaoId() + user.getRole() + user.getImgUrl());
    }

}