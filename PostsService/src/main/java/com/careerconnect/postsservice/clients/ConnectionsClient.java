package com.careerconnect.postsservice.clients;

import com.careerconnect.postsservice.dto.PersonDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "ConnectionsService", path = "/connections")
public interface ConnectionsClient {

  @GetMapping("/core/first-degree")
  List<PersonDto> getFirstConnections();
}
