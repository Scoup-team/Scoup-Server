package com.scoup.server.repository;

import com.scoup.server.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findFirstByPassword(String password);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByEmail(String email);
}
