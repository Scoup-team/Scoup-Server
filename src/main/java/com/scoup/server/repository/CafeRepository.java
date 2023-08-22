package com.scoup.server.repository;

import com.scoup.server.domain.Cafe;
import com.scoup.server.domain.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CafeRepository extends JpaRepository<Cafe, Long> {

    @Query("select c from Cafe c where c.name like concat('%',:keyword,'%') order by c.name asc ")
    List<Cafe> findAllCafeContainingKeyword(@Param("keyword") String keyword);

}
