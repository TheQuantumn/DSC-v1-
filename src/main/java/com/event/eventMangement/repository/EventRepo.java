package com.event.eventMangement.repository;

import com.event.eventMangement.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepo extends JpaRepository<Event, Long> {
}
