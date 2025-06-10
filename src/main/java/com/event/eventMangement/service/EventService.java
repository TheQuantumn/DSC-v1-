package com.event.eventMangement.service;

import com.event.eventMangement.entity.Event;
import jakarta.persistence.Entity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EventService {
    Event createEvent(Event event);
    List<Event> getAllEvent();
    Event getEventById(Long id);
    Event updateEvent(Event event , Long id);
    void deleteEvent(Long id);
}
