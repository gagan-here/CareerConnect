package com.careerconnect.ConnectionsService.service;

import com.careerconnect.ConnectionsService.auth.UserContextHolder;
import com.careerconnect.ConnectionsService.entity.Person;
import com.careerconnect.ConnectionsService.repository.PersonRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class ConnectionsService {

  private final PersonRepository personRepository;

  public List<Person> getFirstDegreeConnection() {
    Long userId = UserContextHolder.getCurrentUserId();
    log.info("Getting first degree connections for user with id: {}", userId);

    return personRepository.getFirstDegreeConnections(userId);
  }
}
