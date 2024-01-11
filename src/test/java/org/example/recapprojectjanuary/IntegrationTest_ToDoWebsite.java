package org.example.recapprojectjanuary;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.example.recapprojectjanuary.dto.Status;
import org.example.recapprojectjanuary.dto.ToDo;
import org.example.recapprojectjanuary.repo.ToDoRepo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class IntegrationTest_ToDoWebsite {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public ToDoRepo toDoRepo;


    private static MockWebServer mockGptWebServer;



    @BeforeAll
    public static void setup() throws IOException{
        mockGptWebServer = new MockWebServer();

        mockGptWebServer.start();
    }

    @DynamicPropertySource
    public static void configureUrl(DynamicPropertyRegistry registry){
        registry.add("app.chatgpt.api.url", () -> mockGptWebServer.url("/").toString());

    }
    @AfterAll
    public static void cleanup() throws IOException {
        mockGptWebServer.shutdown();

    }

    @Test
    @DirtiesContext
    public void getToDoTest() throws Exception{

        //GIVEN
        toDoRepo.save(new ToDo("1","ToDoImplemented", Status.OPEN));



        //WHEN
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/todo"))

        //THEN
                .andExpect(status().isOk())
                .andExpect(content().json("""
                 
                 [{
                 "id": "1",
                 "description": "ToDoImplemented",
                 "status" : "OPEN"
                 }]
                 
                 """))
                .andReturn();
        assertEquals(result.getResponse().getStatus(),200);

    }

    @Test
    @DirtiesContext
    public void getToDoById() throws Exception {

        //GIVEN
        toDoRepo.save(new ToDo("1","ToDoImplemented", Status.OPEN));

        //WHEN
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                 {
                 "id": "1",
                 "description": "ToDoImplemented",
                 "status" : "OPEN"
                 }
                 """))
                .andReturn();

        assertEquals(result.getResponse().getStatus(), 200);

    }

    @Test
    @DirtiesContext
    public void postToDoTest() throws Exception{
    //Given

        mockGptWebServer.enqueue(new MockResponse()
                .addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON)
                .setBody("""
                        {
                        "choices":
                        [{
                        "message":{
                        "role": "user",
                        "content": "implementToDo"
                        }
                        }]
                        }
                        """));
        //WHEN
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/todo").contentType(MediaType.APPLICATION_JSON).content("""
                {
                "content": "implementTodo",
                "status": "OPEN"
                }
                """))
                .andExpect(status().isCreated())
                .andReturn();

        assertEquals(mvcResult.getResponse().getStatus(),201);

    }

    @Test
    @DirtiesContext
    public void deleteToDoTest()throws Exception{

        //GIVEN
        toDoRepo.save(new ToDo("1","ToDoImplemented",Status.OPEN));

        //WHEN
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.delete("/api/todo/1"))

                //THEN
                .andExpect(status().isNoContent())
                .andReturn();

        assertEquals(result.getResponse().getStatus(),204);

    }

    @Test
    @DirtiesContext
    public void updateStatusTest() throws Exception{
        //GIVEN
        toDoRepo.save(new ToDo("1","ToDoImplemented",Status.OPEN));

        //WHEN
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.put("/api/todo/1").contentType(MediaType.APPLICATION_JSON).content("""
                {
                "content": "implementTodo",
                "status": "OPEN"
                }
                """))
                .andExpect(status().isOk())
                .andReturn();
        //THEN
        assertEquals(result.getResponse().getStatus(),200);

    }


}
