package dnd.project.dnd6th7worryrecordservice.repository;


import dnd.project.dnd6th7worryrecordservice.domain.user.User;

import java.util.Optional;

public interface UserRepository {

    public void save(User user);
    public Optional<User> findByUserId(Long userId);
}

