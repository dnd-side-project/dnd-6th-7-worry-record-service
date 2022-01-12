package dnd.project.dnd6th7worryrecordservice.repository;

import dnd.project.dnd6th7worryrecordservice.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.Optional;

@Transactional
@Component
public class JpaUserRepository implements UserRepository{
    private final EntityManager em;

    @Autowired
    public JpaUserRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public void save(User user) {
        em.persist(user);
    }

    @Override
    public Optional<User> findByUserId(Long userId) {
        User user = em.find(User.class, userId);
        return Optional.ofNullable(user);
    }
}
