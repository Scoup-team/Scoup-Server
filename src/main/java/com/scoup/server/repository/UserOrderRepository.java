package com.scoup.server.repository;

import com.scoup.server.domain.User;
import com.scoup.server.domain.UserOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOrderRepository extends JpaRepository<UserOrder, Long> {

}
