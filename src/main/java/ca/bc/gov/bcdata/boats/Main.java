package ca.bc.gov.bcdata.boats;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Main {

  private Queue<String> boats = new LinkedBlockingQueue<>();

  public static void main(String[] args) throws Exception {
    SpringApplication.run(Main.class, args);
  }

  @GetMapping("/boats")
  ResponseEntity<Queue<String>> getBoats() {
    return getBoatsResponse();
  }

  @PostMapping("/boat") 
  ResponseEntity<Queue<String>> addBoat(@RequestBody String boat) {
    if (boat == null || boat.isEmpty()) {
      throw new RuntimeException("The value of 'boat' is empty.");
    }
    boats.add(boat);
    return getBoatsResponse();
  }

  @DeleteMapping("/boats")
  ResponseEntity<Queue<String>> clearBoats() {
    boats.clear();
    return getBoatsResponse();
  }

  private ResponseEntity<Queue<String>> getBoatsResponse() {
    return ResponseEntity.ok().header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*").body(boats);
  }
}
