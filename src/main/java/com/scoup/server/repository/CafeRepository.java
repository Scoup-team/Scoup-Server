package com.scoup.server.repository;

import com.scoup.server.domain.Cafe;
import com.scoup.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CafeRepository extends JpaRepository<Cafe, Long> {

}
