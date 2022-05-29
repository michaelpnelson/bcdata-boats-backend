package ca.bc.gov.bcdata.boats;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Main {
  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }

  private Map<Long, Boat> boats = new ConcurrentHashMap<>();

  @GetMapping("/boats")
  ResponseEntity<Collection<Boat>> getBoats() {
    return buildOkResponseWithCorsHeader().body(boats.values());
  }

  @PostMapping("/boat") 
  ResponseEntity<Boat> addBoat(@RequestBody Boat boat) {
    if (boat == null) {
      throw new RuntimeException("The value of 'boat' is empty.");
    }
    // TODO more validation, tests
    boats.put(boat.getId(), boat);
    return buildOkResponseWithCorsHeader().body(boat);
  }

  @DeleteMapping("/boats")
  ResponseEntity<String> clearBoats() {
    boats.clear();
    return buildOkResponseWithCorsHeader().body("cleared!");
  }

  private ResponseEntity.BodyBuilder buildOkResponseWithCorsHeader() {
    return ResponseEntity.ok().header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
  }
}
