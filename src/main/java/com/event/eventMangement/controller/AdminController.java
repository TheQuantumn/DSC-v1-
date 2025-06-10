package com.event.eventMangement.controller;

import com.event.eventMangement.entity.Event;
import com.event.eventMangement.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private EventService eventService;


    @GetMapping
    public List<Event> getAllEvents(){
        return eventService.getAllEvent();
    }
}
