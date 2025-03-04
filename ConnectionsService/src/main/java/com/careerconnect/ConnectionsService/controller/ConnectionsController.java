package com.careerconnect.ConnectionsService.controller;

import com.careerconnect.ConnectionsService.entity.Person;
import com.careerconnect.ConnectionsService.service.ConnectionsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/core")
@RequiredArgsConstructor
public class ConnectionsController {

  private final ConnectionsService connectionsService;

  @GetMapping("/first-degree")
  public ResponseEntity<List<Person>> getFirstConnections() {
    return ResponseEntity.ok(connectionsService.getFirstDegreeConnection());
  }
}
