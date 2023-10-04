package com.scoup.server.repository;

import com.scoup.server.domain.User;
import com.scoup.server.domain.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserOrderRepository extends JpaRepository<UserOrder, Long> {

    List<UserOrder> findByUser_Id(Long id);
    List<UserOrder> findByStamp_Id(Long id);

}
