package com.scoup.server.repository;

import com.scoup.server.domain.Event;
import com.scoup.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    //List<Event> findByCafe_Id(long l);
}
