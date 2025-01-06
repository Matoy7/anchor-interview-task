package com.sheetmanager.services;

import com.sheetmanager.dtos.cell.CellDTO;
import com.sheetmanager.entities.sheet.Sheet;
import com.sheetmanager.entities.cell.Cell;
import com.sheetmanager.entities.column.Column;
import com.sheetmanager.entities.schema.ColumnType;
import com.sheetmanager.entities.schema.Schema;
import com.sheetmanager.mappers.CellMapper;
import com.sheetmanager.utils.CellInfo;
import com.sheetmanager.utils.SheetUtils;


import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Service
@Data
public class SheetService {
    private final String LOOKUP_PREFIX_ESCAPED = "lookup\\(";
    private final String LOOKUP_SUFFIX_ESCAPED = "\\)";

    private final String LOOKUP_SEPARATOR = ",";
    private Map<Integer, Sheet> sheetsBySheetId;
    private final CellMapper cellMapper;

    @Autowired
    public SheetService(CellMapper cellMapper) {
        sheetsBySheetId = new ConcurrentHashMap();
        this.cellMapper = cellMapper;
    }

    public int createNewSheet(Schema schema) {
        int newSheetId = sheetsBySheetId.size() + 1;
        Sheet newSheet = new Sheet(schema, new ConcurrentHashMap<>());
        sheetsBySheetId.put(newSheetId, newSheet);
        return newSheetId;
    }

    public void setCell(int sheetId, String cellColName, int cellRowNumber, Cell cell) throws IllegalArgumentException {
        Sheet sheet = sheetsBySheetId.get(sheetId);
        if (sheet == null) {
            String errorMsg = "There is no sheet with the following id: " + sheetId;
            log.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        cell.setSheetId(sheetId);
        cell.setColName(cellColName);
        cell.setRowNumber(cellRowNumber);
        validateCell(sheetId, sheet.getSchema(), cell);
        Column column = sheet.getColumns().get(cellColName);
        if (column == null) {
            Map<Integer, Cell> rows = new ConcurrentHashMap<>();
            rows.put(cellRowNumber, cell);
            sheetsBySheetId.get(sheetId).getColumns().put(cellColName, new Column(rows));
            return;
        }
        Map<Integer, Cell> rows = column.getRows();
        rows.put(cellRowNumber, cell);
    }

    public List<CellDTO> getCellsBySheetId(int sheetId) {
        List<Cell> cells = new ArrayList<>();
        if (sheetsBySheetId.get(sheetId) == null) {
            String errorMsg = "The sheet with id: " + sheetId + " does exist";
            log.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        Map<String, Column> columns = sheetsBySheetId.get(sheetId).getColumns();
        for (Map.Entry<String, Column> columnEntry : columns.entrySet()) {
            Column column = columnEntry.getValue();
            Map<Integer, Cell> rows = column.getRows();
            for (Map.Entry<Integer, Cell> rowsEntry : rows.entrySet()) {
                Cell cell = rowsEntry.getValue();
                cells.add(cell);
            }
        }
        return this.cellMapper.toDTOList(cells, this);
    }

    public Object getCellActualContent(int sheetId, Object content) {
        if (!SheetUtils.isLookupExpression(content)) {
            return content;
        }
        Cell cellFromReference = getCellFromReference(sheetId, content);
        return getCellActualContent(sheetId, cellFromReference.getContent());
    }

    private void validateCell(int sheetId, Schema schema, Cell cell) {
        String cellColName = cell.getColName();
        int cellRowNumber = cell.getRowNumber();
        Object content = cell.getContent();
        validateColumnExist(schema, cellColName);
        ColumnType columnType = schema.getColumnMetadata().get(cellColName);
        validateCellValueType(content, columnType);
        if (SheetUtils.isLookupExpression(content)) {
            validateLookupExpression(sheetId, schema, cell, cellColName, cellRowNumber, content, columnType);
        }
    }

    private void validateLookupExpression(int sheetId, Schema schema, Cell cell, String cellColName, int cellRowNumber, Object content, ColumnType columnType) {
        if (isCircleRefExist(sheetId, cell)) {
            String errorMsg = "Cycles are not allowed";
            log.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);
        }
        Cell refCell = getCellFromReference(sheetId, content);
        ColumnType columnTypeFromRef = schema.getColumnMetadata().get(refCell.getColName());
        if (columnTypeFromRef == null) {
            String errorMsg = "No such column in schema as: " + refCell.getColName();
            log.error(errorMsg);
            throw new IllegalArgumentException(errorMsg);

        }
        if (columnType != columnTypeFromRef) {
            String errorMessage = SheetUtils.getErrorMessageForIncorrectRefType(sheetId, cellColName, cellRowNumber, content, columnType, refCell, columnTypeFromRef);
            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private void validateCellValueType(Object content, ColumnType columnType) {
        if (!SheetUtils.isLookupExpression(content) && !ColumnType.isContentMatchColType(columnType, content)) {
            throw new IllegalArgumentException("The cell value: " + content + " does not match expected type is: " + columnType.type);
        }
    }

    private void validateColumnExist(Schema schema, String cellColName) {
        if (schema == null || schema.getColumnMetadata() == null || schema.getColumnMetadata().get(cellColName) == null) {
            throw new IllegalArgumentException("The column name: " + cellColName + " was not found");
        }
    }

    private boolean isCircleRefExist(int sheetId, Cell cell) {
        return isCircleRefExist(sheetId, cell, new HashSet());
    }

    private boolean isCircleRefExist(int sheetId, Cell cell, Set<CellInfo> visitedCells) {
        CellInfo cellInfo = new CellInfo(cell.getColName(), cell.getRowNumber());
        if (visitedCells.contains(cellInfo)) {
            return true;
        }
        visitedCells.add(cellInfo);
        if (!SheetUtils.isLookupExpression(cell.getContent())) {
            return false;
        }
        Cell refCell = getCellFromReference(sheetId, cell.getContent());
        return isCircleRefExist(sheetId, refCell, visitedCells);
    }


    private Cell getCellFromReference(int sheetId, Object content) {
        String[] lookupParts = content.toString().split(LOOKUP_PREFIX_ESCAPED);
        if (lookupParts.length > 0) {
            String lookupCellString = lookupParts[1].split(LOOKUP_SUFFIX_ESCAPED)[0];
            String[] lookupCellParts = lookupCellString.split(LOOKUP_SEPARATOR);
            if (lookupCellParts.length == 2) {
                String colName = lookupCellParts[0];
                int rowNumber = Integer.parseInt(lookupCellParts[1]);
                Sheet sheet = sheetsBySheetId.get(sheetId);
                if (sheet == null) {
                    return null;
                }
                Column column = sheet.getColumns().get(colName);
                if (column == null) {
                    return new Cell(sheetId, colName, rowNumber, null);
                }
                Cell cell = column.getRows().get(rowNumber);
                if (cell == null) {
                    cell = new Cell(sheetId, colName, rowNumber, null);
                }
                return cell;
            }
        }
        return null;
    }
}