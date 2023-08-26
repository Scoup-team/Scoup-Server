package com.scoup.server.repository;

import com.scoup.server.domain.User;
import com.scoup.server.domain.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserOrderRepository extends JpaRepository<UserOrder, Long> {

    @Query("select u from UserOrder u where u.user.id=:userId")
    List<UserOrder> findByUserId(@Param("userId") Long id);

}
