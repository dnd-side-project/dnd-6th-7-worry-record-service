package dnd.project.dnd6th7worryrecordservice.service;

import dnd.project.dnd6th7worryrecordservice.aws.S3Uploader;
import dnd.project.dnd6th7worryrecordservice.domain.user.Role;
import dnd.project.dnd6th7worryrecordservice.domain.user.User;
import dnd.project.dnd6th7worryrecordservice.dto.UserRequestDto;
import dnd.project.dnd6th7worryrecordservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Component
@RequiredArgsConstructor
public class UserServiceImpl {
    private final UserRepository userRepository;


    public void join(UserRequestDto userRequestDto, String imgUrl) {
        try {
            User user = new User(userRequestDto.getUsername(), userRequestDto.getEmail(), userRequestDto.getKakaoId(), Role.USER.name(), imgUrl);
            userRepository.save(user);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Optional<User> findByUserId(Long userId) {
        Optional<User> User = userRepository.findByUserId(userId);
        return User;
    }


//    private void validateDuplicateMember(User user){
//        userRepository.findByName(member.getName())
//                .ifPresent(m -> {
//                    throw new IllegalStateException("이미 존재하는 회원입니다.");
//                });
//    }
}