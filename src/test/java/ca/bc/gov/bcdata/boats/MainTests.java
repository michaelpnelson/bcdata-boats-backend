package ca.bc.gov.bcdata.boats;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
    public void putAndGetBoat() throws Exception {
        mockMvc.perform(post("/boat").content("Just Fishin'"))
            .andExpect(status().isOk())
            .andExpect(content().string("[\"Just Fishin'\"]"));
        mockMvc.perform(get("/boats"))
            .andExpect(status().isOk())
			.andExpect(content().string("[\"Just Fishin'\"]"));
    }

    @Test
    public void putEmptyValueCausesError() throws Exception {

    }
}
