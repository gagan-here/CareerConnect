package com.careerconnect.connectionsservice.service;

import com.careerconnect.connectionsservice.auth.UserContextHolder;
import com.careerconnect.connectionsservice.entity.Person;
import com.careerconnect.connectionsservice.repository.PersonRepository;
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

  public Boolean sendConnectionRequest(Long userId) {
    return null;
  }
}
