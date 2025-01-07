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
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCreateSheetRoute() throws Exception {
        createSheetByApi();
    }

    @Test
    void testGetSheetRoute() throws Exception {
        // preparation
        String sheetId = createSheetByApi();
        // api call for tested route
        String sheetBody = getSheetByApi(sheetId);
        Assert.assertEquals("{\"key\":\"sheet id:  " + sheetId + "\",\"value\":[]}", sheetBody);
    }

    @Test
    void testSetCellRoutePlainCellContent() throws Exception {
        // preparation
        String sheetId = createSheetByApi();
        setCellByApi(sheetId, "B", 12, SheetControllerITUtils.getCellRequestBodyWithLookup(33));
        // api call for tested route
        String sheetBody = getSheetByApi(sheetId);
        Assert.assertEquals("{\"key\":\"sheet id:  " + sheetId + "\",\"value\":[{\"rowNumber\":12,\"content\":33,\"colName\":\"B\"}]}", sheetBody);
    }

    @Test
    void testSetCellRouteLookupCellContent() throws Exception {
        // preparation
        String sheetId = createSheetByApi();
        setCellByApi(sheetId, "B", 12, SheetControllerITUtils.getCellRequestBodyWithLookup("B", 11));
        setCellByApi(sheetId, "B", 11, SheetControllerITUtils.getCellRequestBodyWithLookup(44));
        // api call for tested route
        String sheetBody = getSheetByApi(sheetId);
        Assert.assertEquals("{\"key\":\"sheet id:  "+sheetId+"\",\"value\":[{\"rowNumber\":11,\"content\":44,\"colName\":\"B\"},{\"rowNumber\":12,\"content\":44,\"colName\":\"B\"}]}", sheetBody);
    }

    private String createSheetByApi() throws Exception {
        MvcResult createSheetResult = this.mockMvc.perform(MockMvcRequestBuilders.
                post("/sheet/").
                contentType(MediaType.APPLICATION_JSON).
                content(SheetControllerITUtils.getSheetBody())).
                andExpect(MockMvcResultMatchers.status().isCreated()).andReturn();
        String createSheetBodyString = createSheetResult.getResponse().getContentAsString();
        JSONObject createSheetBody = new JSONObject(createSheetBodyString);
        String sheetId = createSheetBody.get("value").toString();
        // verification
        Assert.assertEquals("{\"key\":\"id\",\"value\":" + sheetId + "}", createSheetBodyString);
        return sheetId;
    }

    private void setCellByApi(String sheetId, Object colName, int rowNumber, String content) throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.
                post("/sheet/" + sheetId + "/col/" + colName + "/row/" + rowNumber).
                contentType(MediaType.APPLICATION_JSON).content(content)).
                andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
    }

    private String getSheetByApi(String sheetId) throws Exception {
        MvcResult getSheetResult = this.mockMvc.perform(MockMvcRequestBuilders.
                get("/sheet/" + sheetId)).
                andExpect(MockMvcResultMatchers.status().isOk()).andReturn();
        // verification
        return getSheetResult.getResponse().getContentAsString();
    }
}