package ca.bc.gov.bcdata.boats;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

  @GetMapping("/boat/{id}")
  ResponseEntity<Boat> getBoat(@PathVariable Long id) {
    if (!boats.containsKey(id)) {
      throw new ApiValidationException(String.format("Cannot get boat with id %d because it does not exist.", id));
    }
    Boat boat = boats.get(id);
    return buildOkResponseWithCorsHeader().body(boat);
  }

  @PostMapping("/boat") 
  ResponseEntity<Boat> addBoat(@RequestBody Boat boat) {
    if (boats.containsKey(boat.getId())) {
      throw new ApiValidationException(String.format("Cannot add boat with id %d since the id already exists.", boat.getId()));
    }
    validateBoat(boat);
    return addOrReplaceBoat(boat);
  }

  @PutMapping("/boat")
  ResponseEntity<Boat> replaceBoat(@RequestBody Boat boat) {
    if (!boats.containsKey(boat.getId())) {
      throw new ApiValidationException(String.format("Cannot update boat with id %d because it does not exist.", boat.getId()));
    }
    validateBoat(boat);
    return addOrReplaceBoat(boat);
  }

  private void validateBoat(Boat boat) {
    if (boat == null) {
      throw new ApiValidationException("The value of 'boat' is empty.");
    }
    if (boat.getId() == null || boat.getId() < 1) {
      throw new ApiValidationException("The value of 'id' is missing or is less than 1.");
    }
    if (Strings.isEmpty(boat.getName())) {
      throw new ApiValidationException("The value of 'name' is missing.");
    }
    if (Strings.isEmpty(boat.getStatus())) {
      throw new ApiValidationException("The value of 'status' is missing.");
    }
  }

  private ResponseEntity<Boat> addOrReplaceBoat(Boat boat) {
    boats.put(boat.getId(), boat);
    return buildOkResponseWithCorsHeader().body(boat);
  }

  @DeleteMapping("/boats")
  ResponseEntity<String> clearBoats() {
    boats.clear();
    return buildOkResponseWithCorsHeader().body("cleared!");
  }

  @DeleteMapping("/boat/{id}")
  ResponseEntity<Boat> deleteBoat(@PathVariable Long id) {
    if (!boats.containsKey(id)) {
      throw new ApiValidationException(String.format("Cannot delete boat with id %d because it does not exist.", id));
    }
    Boat boat = boats.remove(id);
    return buildOkResponseWithCorsHeader().body(boat);
  }

  private ResponseEntity.BodyBuilder buildOkResponseWithCorsHeader() {
    ResponseEntity.BodyBuilder builder = ResponseEntity.ok();
    withCorsHeader(builder);
    return builder;
  }

  private void withCorsHeader(ResponseEntity.BodyBuilder builder) {
    builder.header(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
  }
}
