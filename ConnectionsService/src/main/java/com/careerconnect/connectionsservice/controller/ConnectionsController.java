package com.careerconnect.connectionsservice.controller;

import com.careerconnect.connectionsservice.entity.Person;
import com.careerconnect.connectionsservice.service.ConnectionsService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

  @PostMapping("/request/{userId}")
  public ResponseEntity<Boolean> sendConnectionRequest(@PathVariable Long userId) {
    return ResponseEntity.ok(connectionsService.sendConnectionRequest(userId));
  }

  @PostMapping("/accept/{userId}")
  public ResponseEntity<Boolean> acceptConnectionRequest(@PathVariable Long userId) {
    return ResponseEntity.ok(connectionsService.acceptConnectionRequest(userId));
  }

  @PostMapping("/reject/{userId}")
  public ResponseEntity<Boolean> rejectConnectionRequest(@PathVariable Long userId) {
    return ResponseEntity.ok(connectionsService.rejectConnectionRequest(userId));
  }
}
