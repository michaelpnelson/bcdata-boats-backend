package ca.bc.gov.bcdata.boats;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class MainTests {

    @Autowired
	private MockMvc mockMvc;

    @AfterEach
    void clearBoatsAfterEachTest() throws Exception {
        mockMvc.perform(delete("/boats"));
    }

	@Test
	public void getBoatsIsInitiallyEmpty() throws Exception {
		mockMvc.perform(get("/boats"))
            .andExpect(status().isOk())
			.andExpect(content().string("[]"));
	}

    @Test
    public void getBoat() throws Exception {
        Boat boat = new Boat(1l, "Just Fishin'", "Departed Harbour");
        ObjectMapper mapper = new ObjectMapper();
        String boatJson = mapper.writeValueAsString(boat);
        mockMvc.perform(post("/boat")
            .contentType(MediaType.APPLICATION_JSON)
            .content(boatJson))
            .andExpect(status().isOk());
        mockMvc.perform(get(String.format("/boat/%d", boat.getId())))
            .andExpect(status().isOk())
            .andExpect(content().string(boatJson));
    }

    @Test
    public void getBoatWithNonexistentIdGivesError() throws Exception {
        mockMvc.perform(get(String.format("/boat/%d", 666l)))
            .andExpect(status().isBadRequest())
            .andExpect(content().string("Cannot get boat with id 666 because it does not exist."));
    }

    @Test
    public void responseHasCorsHeader() throws Exception {
		mockMvc.perform(get("/boats"))
            .andExpect(header().string("Access-Control-Allow-Origin", "*"));
    }

    @Test
    public void addBoatAndGetBoats() throws Exception {
        Boat boat = new Boat(1l, "Just Fishin'", "Departed Harbour");
        ObjectMapper mapper = new ObjectMapper();
        String boatJson = mapper.writeValueAsString(boat);
        mockMvc.perform(post("/boat")
            .contentType(MediaType.APPLICATION_JSON)
            .content(boatJson))
            .andExpect(status().isOk())
            .andExpect(content().string(boatJson));
        String boatArray = String.format("[%s]", boatJson);
        mockMvc.perform(get("/boats"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(content().string(boatArray));
    }

    @Test
    public void addEmptyBoatGivesErrorResponse() throws Exception {
        mockMvc.perform(post("/boat")
            .contentType(MediaType.APPLICATION_JSON)
            .content(""))
            .andExpect(status().is(400));
    }

    @Test
    public void addBoatWithInvalidIdGivesErrorResponse() throws Exception {
        Boat boat = new Boat(-1l, "Tie Me To The Mast", "In Distress");
        mockMvc.perform(post("/boat")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(boat)))
            .andExpect(status().is(400))
            .andExpect(content().string("The value of 'id' is missing or is less than 1."));
    }

    @Test
    public void addBoatWithoutNameGivesErrorResponse() throws Exception {
        Boat boat = new Boat(2l, null, "Floundering");
        mockMvc.perform(post("/boat")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(boat)))
            .andExpect(status().is(400))
            .andExpect(content().string("The value of 'name' is missing."));
    }

    @Test
    public void addBoatWithoutStatusGivesErrorResponse() throws Exception {
        Boat boat = new Boat(3l, "Full Tank", "");
        mockMvc.perform(post("/boat")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(boat)))
            .andExpect(status().is(400))
            .andExpect(content().string("The value of 'status' is missing."));
    }

    @Test
    public void addBoatHavingExistingIdGivesErrorResponse() throws Exception {
        Boat boat1 = new Boat(1l, "Happy Sails", "Approaching Harbour");
        ObjectMapper mapper = new ObjectMapper();
        String boat1Json = mapper.writeValueAsString(boat1);
        mockMvc.perform(post("/boat")
            .contentType(MediaType.APPLICATION_JSON)
            .content(boat1Json))
            .andExpect(status().isOk())
            .andExpect(content().string(boat1Json));
        Boat boat2 = new Boat(1l, "Splish Splash", "Departing Harbour");
        String boat2Json = mapper.writeValueAsString(boat2);
        mockMvc.perform(post("/boat")
            .contentType(MediaType.APPLICATION_JSON)
            .content(boat2Json))
            .andExpect(status().is(400))
            .andExpect(content().string("Cannot add boat with id 1 since the id already exists."));
    }

    @Test
    public void replaceBoat() throws Exception {
        Boat boatOriginal = new Boat(1l, "Princess of the Sea", "Departing Berth");
        ObjectMapper mapper = new ObjectMapper();
        String boatOriginalJson = mapper.writeValueAsString(boatOriginal);
        mockMvc.perform(post("/boat")
            .contentType(MediaType.APPLICATION_JSON)
            .content(boatOriginalJson))
            .andExpect(status().isOk())
            .andExpect(content().string(boatOriginalJson));
        Boat boatUpdated = new Boat(1l, "Princess of the Sea", "Heading Out to Sea");
        String boatUpdatedJson = mapper.writeValueAsString(boatUpdated);
        mockMvc.perform(put("/boat")
            .contentType(MediaType.APPLICATION_JSON)
            .content(boatUpdatedJson))
            .andExpect(status().isOk())
            .andExpect(content().string(boatUpdatedJson));
    }

    @Test
    public void replaceBoatWithIdNotExistingCausesError() throws Exception {
        Boat boat = new Boat(1l, "Anchors Aweigh!", "Just Christened");
        mockMvc.perform(put("/boat")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(boat)))
            .andExpect(status().is(400))
            .andExpect(content().string("Cannot update boat with id 1 because it does not exist."));
    }

    @Test
    public void deleteBoat() throws Exception {
        Boat boat = new Boat(1l, "High Seas", "Under Way");
        String boatJson = new ObjectMapper().writeValueAsString(boat);
        mockMvc.perform(post("/boat")
            .contentType(MediaType.APPLICATION_JSON)
            .content(boatJson))
            .andExpect(status().isOk())
            .andExpect(content().string(boatJson));
        mockMvc.perform(delete("/boat/1"))
            .andExpect(status().isOk());
        mockMvc.perform(get("/boats"))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(content().string("[]"));
    }

    @Test
    public void deleteBoatWithIdNotExistingCausesError() throws Exception {
        mockMvc.perform(delete("/boat/999"))
            .andExpect(status().is(400))
            .andExpect(content().string("Cannot delete boat with id 999 because it does not exist."));
    }
}
