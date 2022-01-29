package dnd.project.dnd6th7worryrecordservice.dto;

import dnd.project.dnd6th7worryrecordservice.domain.user.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserRequestDtoTest {

    @Test
    public void requestDtoToEntityTest(){

        UserRequestDto userRequestDto = new UserRequestDto("username","email","kakaoId","imgURL");
        User user = userRequestDto.toEntity();

        System.out.println("user.getUsername() = " + user.getUsername());
        System.out.println("user.getEmail() = " + user.getEmail());
        System.out.println("user.getKakaoId() = " + user.getKakaoId());
        System.out.println("user.getImgUrl() = " + user.getImgUrl());
    }
}