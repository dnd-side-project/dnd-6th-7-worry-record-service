package dnd.project.dnd6th7worryrecordservice.domain.user;


import dnd.project.dnd6th7worryrecordservice.dto.UserRequestDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

@SuppressWarnings("unchecked")  //검증되지 않은 연산자 관련 경고를 무시
public interface UserRepository extends JpaRepository<User, Long> {

    User save(User user);

    Optional<User> findByUserId(Long userId);

    Optional<User> findByKakaoId(String kakaoId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.username = ?1, u.email = ?2, u.imgUrl = ?3, u.refreshToken = ?4 WHERE u.kakaoId = ?5")
    void updateUserByKakaoId(String username, String email, String imgURL, String refreshToken, String kakaoId);

}

