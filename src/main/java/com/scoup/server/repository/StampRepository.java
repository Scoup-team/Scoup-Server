package com.scoup.server.repository;

import com.scoup.server.domain.Menu;
import com.scoup.server.domain.Stamp;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StampRepository extends JpaRepository<Stamp, Long> {

}
