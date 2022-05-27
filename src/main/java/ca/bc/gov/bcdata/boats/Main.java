package ca.bc.gov.bcdata.boats;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
  Queue<String> getBoats() {
    return boats;
  }

  @PostMapping("/boat") 
  Queue<String> addBoat(@RequestBody String boat) {
    if (boat == null || boat.isEmpty()) {
      throw new RuntimeException("The value of 'boat' is empty.");
    }
    boats.add(boat);
    return boats;
  }

  @DeleteMapping("/boats")
  void clearBoats() {
    boats.clear();
  }
}
