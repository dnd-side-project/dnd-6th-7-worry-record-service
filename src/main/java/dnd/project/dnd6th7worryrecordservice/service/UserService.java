package dnd.project.dnd6th7worryrecordservice.service;

import dnd.project.dnd6th7worryrecordservice.domain.user.User;
import dnd.project.dnd6th7worryrecordservice.domain.user.UserRepository;
import dnd.project.dnd6th7worryrecordservice.dto.user.UserRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void insertOrUpdateUser(UserRequestDto userRequestDto) {
        String kakaoId = userRequestDto.getKakaoId();
        //처음 로그인 하는 유저면 DB에 insert
        if(!findUserByKakaoId(kakaoId).isPresent()){
            User user = userRequestDto.toEntity(); //기본 Role = ROLE.USER
            userRepository.save(user);
        }else{ //이미 로그인 했던 유저라면 DB update
            updateUserByKakaoId(userRequestDto);
        }
    }

    public Optional<User> findUserByUserId(Long userId){
        Optional<User> user = userRepository.findByUserId(userId);
        return user;
    }

    public Optional<User> findUserByKakaoId(String kakaoId){
        Optional<User> user = userRepository.findByKakaoId(kakaoId);
        return user;
    }

    public void updateUserByKakaoId(UserRequestDto userInfo){
        userRepository.updateUserByKakaoId(userInfo.getUsername(), userInfo.getEmail(), userInfo.getImgURL(), userInfo.getRefreshToken(), userInfo.getKakaoId());
    }

    public String findRefreshTokenByKakaoId(String kakaoId){
        return userRepository.findRefreshTokenByKakaoId(kakaoId);
    }

    public void deleteUserByKakaoId(String kakaoId){
        Optional<User> user = userRepository.findByKakaoId(kakaoId);
        if(user.isPresent())
            userRepository.delete(user.get());
        else
            throw new NullPointerException();
    }


}