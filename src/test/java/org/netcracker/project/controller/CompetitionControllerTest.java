package org.netcracker.project.controller;


import org.junit.jupiter.api.Test;
import org.netcracker.project.model.Competition;
import org.netcracker.project.repository.CompetitionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
@Sql(value = {"/create-user-before.sql", "/create-competition-before.sql", "/create-team-before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/create-team-after.sql", "/create-competition-after.sql", "/create-user-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@WithUserDetails("steam")
public class CompetitionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompetitionController competitionController;

    @Autowired
    private CompetitionRepository competitionRepository;

    @Test
    public void contextLoad() {
        assertNotNull(competitionController);
    }

    @Test
    public void competitionListCheck() throws Exception {
        mockMvc.perform(get("/competition"))
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='competition-list']/div/div/a").nodeCount(5));
    }

    @Test
    public void specificCompetitionInListTest() throws Exception {
        mockMvc.perform(get("/competition"))
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='competition-list']/div[1]/div/a/img").exists())
                .andExpect(xpath("//div[@id='competition-list']/div[1]/div/div/h5").string(containsString("Hackathon 5")))
                .andExpect(xpath("//div[@id='competition-list']/div[1]/div/div/p[1]").string(containsString("that is description")))
                .andExpect(xpath("//div[@id='competition-list']/div[1]/div/div/a").string(containsString("Go to competition")))
                .andExpect(xpath("//div[@id='competition-list']/div[1]/div/div/p[3]/small[1]").string(containsString("2020-06-06 13:00")))
                .andExpect(xpath("//div[@id='competition-list']/div[1]/div/div/p[3]/small[2]").string(containsString("2020-06-07 12:45")));
    }

    @Test
    public void specificCompetitionTest() throws Exception {
        mockMvc.perform(get("/competition/4"))
                .andExpect(authenticated())
                .andExpect(xpath("//img[@class='card-img-top']").exists())
                .andExpect(xpath("//h1[@id='compNameHeader']").string(containsString("Big Data Analysis MegaHackathon Moscow 2021")))
                .andExpect(xpath("//h6[@id='organizedByHeader']").string(containsString("Organized by Zhora")))
                .andExpect(xpath("//p[@id='descriptionHeader']").string(containsString("Participate and be a part of history, let megacorporations spot you and reserve a spot for you")))
                .andExpect(xpath("//p[@id='startDateHeader']").string(containsString("2021-01-12 17:55")))
                .andExpect(xpath("//p[@id='endDateHeader']").string(containsString("2021-09-12 18:15")));
    }

    @Test
    public void quitCompetitionTest() throws Exception {
        mockMvc.perform(get("/competition/4"))
                .andExpect(xpath("//button[@id='quit-button']").exists());

        mockMvc.perform(post("/competition/4/quit").with(csrf()))
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/competition/4"));
    }

    @Test
    public void joinCompetitionTest() throws Exception {
        mockMvc.perform(get("/competition/3"))
                .andExpect(authenticated())
                .andExpect(xpath("//img[@class='card-img-top']").exists())
                .andExpect(xpath("//h1[@id='compNameHeader']").string(containsString("Hackathon 3")))
                .andExpect(xpath("//h6[@id='organizedByHeader']").string(containsString("Organized by Zhora")))
                .andExpect(xpath("//p[@id='descriptionHeader']").string(containsString("Hackathon 3")))
                .andExpect(xpath("//p[@id='startDateHeader']").string(containsString("2020-01-12 12:45")))
                .andExpect(xpath("//p[@id='endDateHeader']").string(containsString("2020-08-12 12:45")))
                .andExpect(xpath("//button[@id='join-button']").exists());

        mockMvc.perform(post("/competition/3/join").with(csrf()))
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/competition/3"));
    }

    @Test
    public void getArchiveTest() throws Exception{
        mockMvc.perform(get("/competition/mycomp"))
                .andExpect(authenticated())
                .andExpect(xpath("//div[@id='archive']").nodeCount(1));
    }
    @Test
    public void competitionModerationTest() throws Exception {
        mockMvc.perform(get("/competition/1"))
                .andExpect(xpath("//h3[@id='participantsHeader']").string(containsString("Participants")));
    }

    @Test
    public void addCompetitionTest() throws Exception {
        MockHttpServletRequestBuilder multipart = multipart("/competition")
                .file("title", "123".getBytes())
                .param("startDate",  "2023-01-12T05:43")
                .param("endDate", "2023-06-12T17:59")
                .param("compName", "Hackathon 4")
                .param("description", "Description Hack")
                .with(csrf());

        assertEquals(5, competitionRepository.findAll().size());

        mockMvc.perform(multipart)
                .andExpect(authenticated())
                .andExpect(redirectedUrl("/competition"));

        assertEquals(6, competitionRepository.findAll().size());

        Competition competition = competitionRepository.findById(10L).orElse(new Competition());

        assertEquals("Hackathon 4", competition.getCompName());
        assertEquals("Description Hack", competition.getDescription());
        assertEquals(LocalDateTime.parse("2023-01-12T05:43"), competition.getStartDate());
        assertEquals(LocalDateTime.parse("2023-06-12T17:59"), competition.getEndDate());
    }

    @Test
    public void addCompetitionFailDateTest() throws Exception {
        MockHttpServletRequestBuilder multipart = multipart("/competition")
                .file("title", "123".getBytes())
                .param("startDate", "2023-01-12T05:61")
                .param("endDate", "2023-06-13T17:59")
                .param("compName", "Hackathon 4")
                .param("description", "Description Hack")
                .with(csrf());

        mockMvc.perform(multipart)
                .andExpect(authenticated());
    }

    @Test
    public void addCompetitionFailCompNameTest() throws Exception {
        MockHttpServletRequestBuilder multipart = multipart("/competition")
                .file("title", "123".getBytes())
                .param("startDate", "2023-01-12T05:43")
                .param("endDate", "2023-06-12T17:59")
                .param("description", "Description Hack")
                .with(csrf());

        mockMvc.perform(multipart)
                .andExpect(authenticated());
    }

    @Test
    public void addCompetitionFailDescriptionTest() throws Exception {
        MockHttpServletRequestBuilder multipart = multipart("/competition")
                .file("title", "123".getBytes())
                .param("startDate", "2023-01-12T05:43")
                .param("endDate", "2023-06-12T17:59")
                .param("compName", "Hackathon 4")
                .with(csrf());

        mockMvc.perform(multipart)
                .andExpect(authenticated());
    }

    @Test
    public void addCompetitionFailStartDateTest() throws Exception{
        MockHttpServletRequestBuilder multipart = multipart("/competition")
                .file("title","123".getBytes())
                .param("endDate","2023-06-12T17:59")
                .param("compName","Hackathon 4")
                .param("description","Description Hack")
                .with(csrf());
        mockMvc.perform(multipart).andExpect(authenticated());
    }

    @Test
    public void addCompetitionFailEndDateTest() throws Exception{
        MockHttpServletRequestBuilder multipart = multipart("/competition")
                .file("title","123".getBytes())
                .param("startDate","2023-01-12T05:43")
                .param("compName","Hackathon 4")
                .param("description","Description Hack")
                .with(csrf());
        mockMvc.perform(multipart).andExpect(authenticated());
    }
}
