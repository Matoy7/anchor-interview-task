package com.sheetmanager.controller;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class SheetControllerIntegrationTest<x> {
    String createSheetBody = "{\n" +
            "    \"columns\": [\n" +
            "        {\n" +
            "            \"name\": \"A\",\n" +
            "            \"type\": \"boolean\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"B\",\n" +
            "            \"type\": \"int\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"C\",\n" +
            "            \"type\": \"double\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"D\",\n" +
            "            \"type\": \"string\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";

    String setCellBody = "{\n" +
            "    \"content\": 33\n" +
            "}";
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateSheetRoute() throws Exception {

        MvcResult resultForCreateSheet = this.mockMvc.perform(MockMvcRequestBuilders.
                post("/sheet/").
                contentType(MediaType.APPLICATION_JSON).
                content(createSheetBody)).
                andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();

        String createSheetBodyString = resultForCreateSheet.getResponse().getContentAsString();
        JSONObject createSheetBody = new JSONObject(createSheetBodyString);
        String sheetId = createSheetBody.get("value").toString();
        Assert.assertEquals("{\"key\":\"id\",\"value\":"+sheetId+"}", createSheetBodyString);
    }

    @Test
    void testGetSheetRoute() throws Exception {
        MvcResult resultForCreateSheet =  this.mockMvc.perform(MockMvcRequestBuilders.
                post("/sheet/").
                contentType(MediaType.APPLICATION_JSON).content(createSheetBody)).
                andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
        String createSheetBodyString = resultForCreateSheet.getResponse().getContentAsString();
        JSONObject createSheetBody = new JSONObject(createSheetBodyString);
        String sheetId = createSheetBody.get("value").toString();
        MvcResult getSheetResult = this.mockMvc.perform(MockMvcRequestBuilders.
                get("/sheet/"+sheetId)).
                andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String getSheetBodyString = getSheetResult.getResponse().getContentAsString();
        Assert.assertEquals("{\"key\":\"sheet id:  "+sheetId+"\",\"value\":[]}", getSheetBodyString);
    }

    @Test
    void testSetCellRoute() throws Exception {
        MvcResult resultForCreateSheet =  this.mockMvc.perform(MockMvcRequestBuilders.
                post("/sheet/").
                contentType(MediaType.APPLICATION_JSON).content(createSheetBody)).
                andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
        String createSheetBodyString = resultForCreateSheet.getResponse().getContentAsString();
        JSONObject createSheetBody = new JSONObject(createSheetBodyString);
        String sheetId = createSheetBody.get("value").toString();

        this.mockMvc.perform(MockMvcRequestBuilders.
                post("/sheet/"+sheetId+"/col/B/row/12").
                contentType(MediaType.APPLICATION_JSON).content(setCellBody)).
                andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.
                get("/sheet/"+sheetId)).
                andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        Assert.assertEquals("{\"key\":\"sheet id:  "+sheetId+"\",\"value\":[{\"rowNumber\":12,\"content\":33,\"colName\":\"B\"}]}", content);
    }
}