package com.careerconnect.notificationservice.clients;

import com.careerconnect.notificationservice.dto.PersonDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "ConnectionsService", path = "/connections")
public interface ConnectionsClient {

  @GetMapping("/core/first-degree")
  List<PersonDto> getFirstConnections(@RequestHeader("X-User-Id") Long userId);
}
