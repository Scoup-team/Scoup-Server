package com.scoup.server.repository;

import com.scoup.server.domain.Menu;
import com.scoup.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> {

}
