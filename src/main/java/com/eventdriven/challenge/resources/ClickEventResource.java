package com.eventdriven.challenge.resources;

import com.eventdriven.challenge.services.ClickEventService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/clickEvent")
public class ClickEventResource {

    private static final String BASE_PATH = "/api/v1/";
    private ClickEventService service;

    public ClickEventResource(ClickEventService service) {
        this.service = service;
    }
}