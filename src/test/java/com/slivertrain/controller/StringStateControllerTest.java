package com.slivertrain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slivertrain.representation.StringStateRep;
import com.slivertrain.representation.StringStateRepTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by ryan.zhu on 9/12/2016.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class StringStateControllerTest {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MockHttpSession mockSession;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        mockSession = new MockHttpSession(context.getServletContext(), UUID.randomUUID().toString());

        StringStateRep stateRep = new StringStateRep("a", 2);

        this.mockMvc.perform(post("/chars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stateRep))
                .session(this.mockSession));

        stateRep = new StringStateRep("1", 2);

        this.mockMvc.perform(post("/chars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stateRep))
                .session(this.mockSession));

        stateRep = new StringStateRep("c", 2);

        this.mockMvc.perform(post("/chars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stateRep))
                .session(this.mockSession));
    }

    @Test
    public void getStateTest() throws Exception {
        this.mockMvc.perform(get("/state")
                .session(this.mockSession))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"userId\":\"" + this.mockSession.getId() + "\",\"state\":\"aa11cc\"}"));
    }

    @Test
    public void getSumTest() throws Exception {
        this.mockMvc.perform(get("/sum")
                .session(this.mockSession))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"userId\":\"" + this.mockSession.getId() + "\",\"sum\":11}"));
    }

    @Test
    public void getCharTest() throws Exception {
        this.mockMvc.perform(get("/chars")
                .session(this.mockSession))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"userId\":\"" + this.mockSession.getId() + "\",\"chars\":\"aacc\"}"));
    }

    @Test
    public void deleteCharTest() throws Exception {
        //test the normal delete scenario
        this.mockMvc.perform(delete("/chars/a")
                .session(this.mockSession))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"userId\":\"" + this.mockSession.getId() + "\",\"state\":\"a11cc\"}"));

//
//        this.mockMvc.perform(get("/state")
//                .session(this.mockSession))
//                .andExpect(status().isOk())
//                .andExpect(content().string("a11cc"));

        //test scenario: character in DELETE has to be a single alphanumeric character, otherwise return 400
        this.mockMvc.perform(delete("/chars/[")
                .session(this.mockSession))
                .andExpect(status().isBadRequest());

        this.mockMvc.perform(delete("/chars/abc")
                .session(this.mockSession))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void postStateTest() throws Exception {
        //test the normal add state string scenario
        StringStateRep stateRep = new StringStateRep("a", 2);

        this.mockMvc.perform(post("/chars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stateRep))
                .session(this.mockSession))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"userId\":\"" + this.mockSession.getId() + "\",\"state\":\"aa11ccaa\"}"));

        //test scenario: character in POST request has to be just one alphanumeric character and amount a number
        //from 1 to 9, otherwise return 400
        stateRep = new StringStateRep("0", 2);

        this.mockMvc.perform(post("/chars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stateRep))
                .session(this.mockSession))
                .andExpect(status().isBadRequest());

        stateRep = new StringStateRep("]", 2);

        this.mockMvc.perform(post("/chars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stateRep))
                .session(this.mockSession))
                .andExpect(status().isBadRequest());

        stateRep = new StringStateRep("abc", 2);

        this.mockMvc.perform(post("/chars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stateRep))
                .session(this.mockSession))
                .andExpect(status().isBadRequest());

        //test scenario: if the length of the string state will exceed 200 characters after the POST request, do not
        //change the state and return 400
        this.mockMvc.perform(get("/state")
                .session(this.mockSession))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"userId\":\"" + this.mockSession.getId() + "\",\"state\":\"aa11ccaa\"}"));

        stateRep = new StringStateRep("a", 200);

        this.mockMvc.perform(post("/chars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stateRep))
                .session(this.mockSession))
                .andExpect(status().isBadRequest());

        this.mockMvc.perform(get("/state")
                .session(this.mockSession))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"userId\":\"" + this.mockSession.getId() + "\",\"state\":\"aa11ccaa\"}"));

        //test scenario: return 400 if the POST request contains invalid JSON
        StringStateRepTest stateRepInvalid = new StringStateRepTest("a", 2, "test");

        this.mockMvc.perform(post("/chars")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(stateRepInvalid))
                .session(this.mockSession))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testWrongUrlCase() throws Exception {
        this.mockMvc.perform(get("/randomUrl"))
                .andExpect(status().isNotFound());

        this.mockMvc.perform(post("/randomUrl")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
