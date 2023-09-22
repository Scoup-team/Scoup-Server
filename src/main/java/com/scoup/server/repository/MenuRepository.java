package com.scoup.server.repository;

import com.scoup.server.domain.Menu;
import com.scoup.server.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    List<Menu> findByCafe_Id(Long id);

    Optional<Menu> findByName(String name);
}
