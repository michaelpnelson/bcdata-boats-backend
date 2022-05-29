package ca.bc.gov.bcdata.boats;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
    void tearDown() throws Exception {
        mockMvc.perform(delete("/boats"));
    }

	@Test
	public void getBoatsIsInitiallyEmpty() throws Exception {
		mockMvc.perform(get("/boats"))
            .andExpect(status().isOk())
			.andExpect(content().string("[]"));
	}

    @Test
    public void responseHasCorsHeader() throws Exception {
		mockMvc.perform(get("/boats"))
            .andExpect(header().string("Access-Control-Allow-Origin", "*"));
    }

    @Test
    public void putAndGetBoat() throws Exception {
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
    public void putEmptyValueCausesError() throws Exception {

    }
}
