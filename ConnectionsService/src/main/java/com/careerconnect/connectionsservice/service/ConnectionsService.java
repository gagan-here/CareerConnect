package com.careerconnect.connectionsservice.service;

import com.careerconnect.connectionsservice.auth.UserContextHolder;
import com.careerconnect.connectionsservice.entity.Person;
import com.careerconnect.connectionsservice.repository.PersonRepository;
import java.util.List;
import java.util.Objects;
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

  public Boolean sendConnectionRequest(Long receiverId) {
    Long senderId = UserContextHolder.getCurrentUserId();
    log.info("Trying to send connection request, sender: {}, receiver: {}", senderId, receiverId);

    if (Objects.equals(senderId, receiverId)) {
      throw new RuntimeException("Both sender and receiver are same!");
    }

    boolean alreadySentRequest = personRepository.connectionRequestExists(senderId, receiverId);
    if (alreadySentRequest) {
      throw new RuntimeException("Connection request already exists, cannot send again");
    }

    boolean alreadyConnected = personRepository.alreadyConnected(senderId, receiverId);
    if (alreadyConnected) {
      throw new RuntimeException("Already connected users, cannot add connection request");
    }

    log.info("Successfully sent connection request");
    personRepository.addConnectionRequest(senderId, receiverId);
    return true;
  }

  public Boolean acceptConnectionRequest(Long senderId) {
    Long receiverId = UserContextHolder.getCurrentUserId();

    boolean connectionRequestExists =
        personRepository.connectionRequestExists(senderId, receiverId);
    if (!connectionRequestExists) {
      throw new RuntimeException("No connection request is sent!");
    }

    personRepository.acceptConnectionRequest(senderId, receiverId);
    return true;
  }

  public Boolean rejectConnectionRequest(Long senderId) {
    Long receiverId = UserContextHolder.getCurrentUserId();

    boolean connectionRequestExists =
        personRepository.connectionRequestExists(senderId, receiverId);
    if (!connectionRequestExists) {
      throw new RuntimeException("No connection exists, cannot delete aconnection!");
    }

    personRepository.rejectConnectionRequest(senderId, receiverId);
    return true;
  }
}
