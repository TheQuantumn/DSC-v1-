package com.event.eventMangement.service.serviceImp;

import com.event.eventMangement.entity.Event;
import com.event.eventMangement.repository.EventRepo;
import com.event.eventMangement.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventServiceImp implements EventService {
    @Autowired
    private EventRepo eventRepo;

    @Override
    public Event createEvent(Event event) {
        eventRepo.save(event);
        return event;
    }

    @Override
    public List<Event> getAllEvent() {
        return eventRepo.findAll();
    }

    @Override
    public Event getEventById(Long id) {
        return eventRepo.findById(id).orElseThrow(()-> new RuntimeException("Event 404"));
    }

    @Override
    public Event updateEvent(Event event, Long id) {
        Event e =getEventById(id);
        e.setName(event.getName()!=null ? event.getName() : e.getName() );
        e.setDescription(event.getDescription()!=null ? event.getDescription() : e.getDescription());
        e.setStartDate(event.getStartDate()!=null ? event.getStartDate() : e.getStartDate());
        e.setEndDate(event.getEndDate()!=null? event.getEndDate(): e.getEndDate());
        e.setFee(event.getFee()!=null ? event.getFee() :  e.getFee());
        eventRepo.save(e);
        return e;
    }

    @Override
    public void deleteEvent(Long id) {
        Event r =getEventById(id);
        eventRepo.delete(r);

    }
}
