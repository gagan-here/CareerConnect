package com.careerconnect.postsservice.clients;

import com.careerconnect.postsservice.dto.PersonDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ConnectionsService", path = "/connections")
public interface ConnectionsClient {

  @GetMapping("/core/{userId}/first-degree")
  List<PersonDto> getFirstConnections(@PathVariable Long userId);
}
