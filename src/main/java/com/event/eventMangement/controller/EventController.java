package com.event.eventMangement.controller;


import com.event.eventMangement.entity.Event;
import com.event.eventMangement.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/event")
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping
    public ResponseEntity<List<Event>> showEventsAll(){
        return new ResponseEntity<>(eventService.getAllEvent(), HttpStatus.OK);
    }
     @PostMapping
    public ResponseEntity<Event> CreateEvent(@RequestBody Event event){
        return new ResponseEntity<>(eventService.createEvent(event),HttpStatus.CREATED);
     }

     @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id){
        return new ResponseEntity<>(eventService.getEventById(id),HttpStatus.OK);
     }

     @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id,  @RequestBody Event e){
        return new ResponseEntity<>(eventService.updateEvent(e,id),HttpStatus.OK);
     }

     @DeleteMapping("/{id}")
     public ResponseEntity<Event> deleteEvent(@PathVariable Long id){
        eventService.deleteEvent(id);
        return new ResponseEntity<>(HttpStatus.OK);
     }
}
