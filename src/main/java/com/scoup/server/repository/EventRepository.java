package com.scoup.server.repository;

import com.scoup.server.domain.Event;
import com.scoup.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

}
