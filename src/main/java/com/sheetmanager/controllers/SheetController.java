package com.sheetmanager.controllers;

import com.sheetmanager.dtos.cell.CellDTO;
import com.sheetmanager.dtos.schema.ColumnMetadataDTO;
import com.sheetmanager.entities.cell.Cell;
import com.sheetmanager.mappers.CellMapper;
import com.sheetmanager.mappers.SchemaMapper;
import com.sheetmanager.services.SheetService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.sheetmanager.entities.schema.Schema;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("sheet")
public class SheetController {
    private final SheetService sheetService;
    private final SchemaMapper schemaMapper;
    private final CellMapper cellMapper;

    @Autowired
    public SheetController(SheetService sheetService, SchemaMapper schemaMapper, CellMapper cellMapper) {
        this.schemaMapper = schemaMapper;
        this.cellMapper = cellMapper;
        this.sheetService = sheetService;
    }

    @PostMapping(value = "/")
    public ResponseEntity<BaseResponse> createNewSheet(@RequestBody ColumnMetadataDTO columnMetadataDTO) {
        log.info("Request to create new sheet request had received");
        if (columnMetadataDTO == null) {
            return new ResponseEntity("please enter valid schema dto in request body", new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        Schema schema = this.schemaMapper.toEntity(columnMetadataDTO);
        int newSheetId = this.sheetService.createNewSheet(schema);
        return new ResponseEntity(new BaseResponse("id", newSheetId), HttpStatus.CREATED);
    }

    @PostMapping(value = "/{sheetId}/col/{colName}/row/{rowNumber}")
    public ResponseEntity<BaseResponse> setCell(@PathVariable(value = "sheetId") int sheetId, @PathVariable(value = "colName")
            String colName, @PathVariable(value = "rowNumber") int rowNumber, @RequestBody CellDTO cellDTO) {
        try {
            log.info("Request to set cell with this rowNumber: " + rowNumber + " colName: " + colName + " content: " + cellDTO.getContent() + " to this sheetId: " + sheetId + " request had received");
            Cell cell = this.cellMapper.toEntity(cellDTO);
            this.sheetService.setCell(sheetId, colName, rowNumber, cell);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/{sheetId}")
    public ResponseEntity<BaseResponse> getSheetById(@PathVariable(value = "sheetId") int sheetId) {
        try {
            log.info("Request to get all cells from this sheetId: " + sheetId + " had received");
            List<CellDTO> cellDTOList = this.sheetService.getCellsDTOListBySheetId(sheetId);
            return new ResponseEntity(new BaseResponse("sheet id:  " + sheetId, cellDTOList), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}