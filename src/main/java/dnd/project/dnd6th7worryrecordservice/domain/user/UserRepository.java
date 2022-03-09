package dnd.project.dnd6th7worryrecordservice.domain.user;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unchecked")  //검증되지 않은 연산자 관련 경고를 무시
public interface UserRepository extends JpaRepository<User, Long> {

    User save(User user);

    List<User> findAll();

    Optional<User> findByUserId(Long userId);

    Optional<User> findByKakaoId(String kakaoId);

    //update 부분 jpa 변경감지로 변환
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.username = ?1, u.email = ?2, u.imgUrl = ?3, u.refreshToken = ?4, u.deviceToken = ?5 WHERE u.kakaoId = ?6")
    void updateUserByKakaoId(String username, String email, String imgURL, String refreshToken, String deviceToken, String kakaoId);

    @Query("SELECT u.refreshToken FROM User u WHERE u.kakaoId = ?1")
    String findRefreshTokenByKakaoId(String kakaoId);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.deviceToken = ?1 WHERE u.userId = ?2")
    void updateDeviceTokenByUserId(String deviceToken, Long userId);

    void delete(User user);

}

