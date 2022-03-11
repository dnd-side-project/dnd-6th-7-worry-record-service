package dnd.project.dnd6th7worryrecordservice.service;

import dnd.project.dnd6th7worryrecordservice.domain.user.SocialType;
import dnd.project.dnd6th7worryrecordservice.domain.user.User;
import dnd.project.dnd6th7worryrecordservice.domain.user.UserRepository;
import dnd.project.dnd6th7worryrecordservice.dto.user.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void insertOrUpdateUser(UserInfoDto userInfoDto) {
        String socialId = userInfoDto.getSocialId();
        SocialType socialType = userInfoDto.getSocialType();
        //처음 로그인 하는 유저면 DB에 insert
        if(!findUserBySocialData(socialId, socialType).isPresent()){
            User user = userInfoDto.toEntity(); //기본 Role = ROLE.USER
            userRepository.save(user);
        }else{ //이미 로그인 했던 유저라면 DB update
            updateUserBySocialData(userInfoDto);
        }
    }

    public List<User> findAllUser(){
        return userRepository.findAll();
    }

    public Optional<User> findUserByUserId(Long userId){
        Optional<User> user = userRepository.findByUserId(userId);
        return user;
    }

    public Optional<User> findUserBySocialData(String socialId, SocialType socialType){
        Optional<User> user = userRepository.findBySocialIdAndSocialType(socialId, socialType);
        return user;
    }

    public void updateUserBySocialData(UserInfoDto userInfo){
        userRepository.updateUserBySocialIdAndSocialType(userInfo.getUsername(), userInfo.getEmail(), userInfo.getImgURL(), userInfo.getRefreshToken(), userInfo.getDeviceToken() ,userInfo.getSocialId(), userInfo.getSocialType());
    }

    public void updateDeviceToken(String deviceToken, Long userId){
        userRepository.updateDeviceTokenByUserId(deviceToken, userId);
    }

    public String findRefreshTokenBySocialData(String socialId, SocialType socialType){
        return userRepository.findRefreshTokenBySocialIdAndSocialType(socialId, socialType);
    }

    public void deleteUserBySocialData(String socialId, SocialType socialType){
        Optional<User> user = userRepository.findBySocialIdAndSocialType(socialId, socialType);
        if(user.isPresent())
            userRepository.delete(user.get());
        else
            throw new NullPointerException();
    }


}