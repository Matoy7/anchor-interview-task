package com.sheetmanager.services;

import com.sheetmanager.dtos.cell.CellDTO;
import com.sheetmanager.entities.cell.Cell;
import com.sheetmanager.entities.schema.ColumnType;
import com.sheetmanager.entities.schema.Schema;
import com.sheetmanager.mappers.CellMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(MockitoJUnitRunner.class)
public class SheetServiceTest {
    public static final String STRING_CELL_1 = "A";
    public static final String STRING_CELL_2 = "B";
    public static final String BOOLEAN_CELL = "C";
    public static final String STRING_CELL_3 = "D";


    private SheetService sheetService;

    @Before
    public void before(){
        sheetService = new SheetService(new CellMapper());
    }

    @Test
    public void createNewSheet_emptySchema_sheetWasCreatedWithIdAsOne() throws IOException {
        int sheetNumber = sheetService.createNewSheet(new Schema());
        Assert.assertNotNull(sheetNumber);
        Assert.assertEquals(1, sheetNumber);
    }

    @Test
    public void createNewSheet_fullSchema_sheetWasCreatedWithIdAsOne() {
        Map<String, ColumnType> columnTypeMap = getSchema();
        int sheetNumber = sheetService.createNewSheet(new Schema(columnTypeMap));
        Assert.assertNotNull(sheetNumber);
        Assert.assertEquals(1, sheetNumber);
    }

    @Test
    public void createTwoNewSheet_fullSchema_sheetWasCreatedWithIdAsOne() {
        Map<String, ColumnType> columnTypeMap = getSchema();
        int firstSheetNumber = sheetService.createNewSheet(new Schema(columnTypeMap));
        int secondSheetNumber = sheetService.createNewSheet(new Schema(columnTypeMap));
        Assert.assertNotNull(firstSheetNumber);
        Assert.assertEquals(1, firstSheetNumber);
        Assert.assertNotNull(secondSheetNumber);
        Assert.assertEquals(2, secondSheetNumber);
    }

    @Test
    public void setCellBySheetId_legalAssignment_noIssues() {
        Map<String, ColumnType> columnTypeMap = getSchema();
        int sheetId = sheetService.createNewSheet(new Schema(columnTypeMap));
        sheetService.setCell(sheetId, STRING_CELL_1, 1, new Cell(0, null, 0, "someContent"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void setCellBySheetId_IllegalAssignment_errorIsThrown() {
        try {
            Map<String, ColumnType> columnTypeMap = getSchema();
            int sheetId = sheetService.createNewSheet(new Schema(columnTypeMap));
            sheetService.setCell(sheetId, STRING_CELL_1, 1, new Cell(0, null, 0, false));
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("The cell value: false does not match expected type is: class java.lang.String", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void setCellBySheetId_referenceToNonExistingCol_errorIsThrown() {
        try {
            Map<String, ColumnType> columnTypeMap = getSchema();
            int sheetId = sheetService.createNewSheet(new Schema(columnTypeMap));
            sheetService.setCell(sheetId, STRING_CELL_1, 1, new Cell(0, null, 0, "lookup(BlaBla,10)"));
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("No such column in schema as: BlaBla", e.getMessage());
            throw e;
        }
    }

    @Test
    public void setCellBySheetId_referenceToExistingCol_noIssues() {
        Map<String, ColumnType> columnTypeMap = getSchema();
        int sheetId = sheetService.createNewSheet(new Schema(columnTypeMap));
        sheetService.setCell(sheetId, STRING_CELL_1, 1, new Cell(0, null, 0, "lookup(A,10)"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void setCellBySheetId_referenceToWrongTypeCol_errorIsThrown() {
        try {
            Map<String, ColumnType> columnTypeMap = getSchema();
            int sheetId = sheetService.createNewSheet(new Schema(columnTypeMap));
            sheetService.setCell(sheetId, STRING_CELL_1, 1, new Cell(0, null, 0, "lookup(C,10)"));
        } catch (IllegalArgumentException e) {
            final String errorMessage = "setCell(1, A, 1, “lookup(C,10)”) failed because the type of column “A”\n" +
                    "(string) doesn’t fit the type of lookup(C,10) which is the same type as\n" +
                    "column “C” (boolean instead of string)";
            Assert.assertEquals(errorMessage, e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void setCellBySheetId_referenceToCycle_errorIsThrown() {
        try {
            Map<String, ColumnType> columnTypeMap = getSchema();
            int sheetId = sheetService.createNewSheet(new Schema(columnTypeMap));
            sheetService.setCell(sheetId, STRING_CELL_1, 1, new Cell(0, null, 0, "lookup(B,1)"));
            sheetService.setCell(sheetId, STRING_CELL_2, 1, new Cell(0, null, 0, "lookup(D,1)"));
            sheetService.setCell(sheetId, STRING_CELL_3, 1, new Cell(0, null, 0, "lookup(A,1)"));
        } catch (IllegalArgumentException e) {
            final String errorMessage = "Cycles are not allowed";
            Assert.assertEquals(errorMessage, e.getMessage());
            throw e;
        }
    }


    @Test
    public void getCellsBySheetId_setThreeCell_threeCellsAreReturned() {
        Map<String, ColumnType> columnTypeMap = getSchema();
        int sheetId = sheetService.createNewSheet(new Schema(columnTypeMap));
        sheetService.setCell(sheetId, STRING_CELL_1, 1, new Cell(0, null, 0, "first"));
        sheetService.setCell(sheetId, STRING_CELL_2, 1, new Cell(0, null, 0, "second"));
        sheetService.setCell(sheetId, STRING_CELL_3, 1, new Cell(0, null, 0, "third"));
        List<CellDTO> cells = sheetService.getCellsBySheetId(sheetId);
        Assert.assertEquals(3, cells.size());

        CellDTO firstCell = cells.get(0);
        assertCell(firstCell, STRING_CELL_1,1, "first");

        CellDTO secondCell = cells.get(1);
        assertCell(secondCell, STRING_CELL_2,1, "second");

        CellDTO thirdCell = cells.get(2);
        assertCell(thirdCell, STRING_CELL_3, 1,"third");
    }

    @Test
    public void getCellsBySheetId_setLookUpRef_cellsValueMatchesRefCell() {
        Map<String, ColumnType> columnTypeMap = getSchema();
        int sheetId = sheetService.createNewSheet(new Schema(columnTypeMap));
        sheetService.setCell(sheetId, STRING_CELL_1, 1, new Cell(0, null, 0, "lookup(B,1)"));
        sheetService.setCell(sheetId, STRING_CELL_2, 1, new Cell(0, null, 0, "lookup(D,1)"));
        sheetService.setCell(sheetId, STRING_CELL_3, 1, new Cell(0, null, 0, "third"));
        List<CellDTO> cells = sheetService.getCellsBySheetId(sheetId);
        Assert.assertEquals(3, cells.size());

        CellDTO firstCell = cells.get(0);
        assertCell( firstCell, STRING_CELL_1, 1, "third");

        CellDTO secondCell = cells.get(1);
        assertCell( secondCell, STRING_CELL_2, 1, "third");

        CellDTO thirdCell = cells.get(2);
        assertCell( thirdCell, STRING_CELL_3, 1,"third");
    }

    @Test
    public void getCellsBySheetId_setLookUpRefAndChangeTheRefAfterAssign_cellsValueMatchesNewRefCell() {
        Map<String, ColumnType> columnTypeMap = getSchema();
        int sheetId = sheetService.createNewSheet(new Schema(columnTypeMap));
        sheetService.setCell(sheetId, STRING_CELL_3, 1, new Cell(0, null, 0, "third"));
        sheetService.setCell(sheetId, STRING_CELL_1, 1, new Cell(0, null, 0, "lookup(B,1)"));
        sheetService.setCell(sheetId, STRING_CELL_2, 1, new Cell(0, null, 0, "lookup(D,1)"));
        sheetService.setCell(sheetId, STRING_CELL_3, 1, new Cell(0, null, 0, "fourth"));
        List<CellDTO> cells = sheetService.getCellsBySheetId(sheetId);
        Assert.assertEquals(3, cells.size());

        CellDTO firstCell = cells.get(0);
        assertCell( firstCell, STRING_CELL_1, 1, "fourth");

        CellDTO secondCell = cells.get(1);
        assertCell( secondCell, STRING_CELL_2, 1, "fourth");

        CellDTO thirdCell = cells.get(2);
        assertCell( thirdCell, STRING_CELL_3, 1,"fourth");
    }


    private void assertCell(CellDTO cellDTO, String colName, int rowNumber, String content) {
        Assert.assertEquals(rowNumber, cellDTO.getRowNumber());
        Assert.assertEquals(colName, cellDTO.getColName());
        Assert.assertEquals(content, cellDTO.getContent());
    }

    private Map<String, ColumnType> getSchema() {
        Map<String, ColumnType> columnTypeMap = new HashMap<>();
        columnTypeMap.put(STRING_CELL_1, ColumnType.STRING);
        columnTypeMap.put(STRING_CELL_2, ColumnType.STRING);
        columnTypeMap.put(BOOLEAN_CELL, ColumnType.BOOLEAN);
        columnTypeMap.put(STRING_CELL_3, ColumnType.STRING);
        return columnTypeMap;
    }

}
