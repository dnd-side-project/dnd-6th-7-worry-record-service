package dnd.project.dnd6th7worryrecordservice.service;

import dnd.project.dnd6th7worryrecordservice.domain.user.Role;
import dnd.project.dnd6th7worryrecordservice.domain.user.User;
import dnd.project.dnd6th7worryrecordservice.dto.UserRequestDto;
import dnd.project.dnd6th7worryrecordservice.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Component
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void insertOrUpdateUser(UserRequestDto userRequestDto) {
        String kakaoId = userRequestDto.getKakaoId();
        System.out.println("kakaoId = " + kakaoId);
        //처음 로그인 하는 유저면 DB에 insert
        if(!findUserByKakaoId(kakaoId).isPresent()){
            System.out.println("insert");
            User user = userRequestDto.toEntity();  //기본 Role = ROLE.USER
            userRepository.save(user);
        }else{ //이미 로그인 했던 유저라면 DB update
            System.out.println("update");
            updateUserByKakaoId(userRequestDto);
        }
    }

    public Optional<User> findUserByKakaoId(String kakaoId){
        Optional<User> user = userRepository.findByKakaoId(kakaoId);
        return user;
    }

    public void updateUserByKakaoId(UserRequestDto userInfo){
        userRepository.updateUserByKakaoId(userInfo.getUsername(), userInfo.getEmail(), userInfo.getImgURL(), userInfo.getRefreshToken(), userInfo.getKakaoId());
    }


}