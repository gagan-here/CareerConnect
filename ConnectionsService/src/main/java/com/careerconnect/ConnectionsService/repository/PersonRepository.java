package com.careerconnect.ConnectionsService.repository;

import com.careerconnect.ConnectionsService.entity.Person;
import java.util.List;
import java.util.Optional;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends Neo4jRepository<Person, Long> {

  Optional<Person> getByName(String name);

  @Query(
      "MATCH (personA:Person) - [:CONNECTED_TO] - (personB:Person) "
          + "WHERE personA.userId = $userId "
          + "Return personB")
  List<Person> getFirstDegreeConnections(Long userId);
}
