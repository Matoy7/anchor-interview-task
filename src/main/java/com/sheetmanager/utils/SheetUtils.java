package com.sheetmanager.utils;

import com.sheetmanager.entities.cell.Cell;
import com.sheetmanager.entities.schema.ColumnType;

public class SheetUtils {
    private static final String LOOKUP_PREFIX = "lookup(";
    private static final String LOOKUP_SUFFIX = ")";
    public static boolean isLookupExpression(Object content) {
        return content != null
                && content instanceof String
                && (content.toString().startsWith(LOOKUP_PREFIX) && content.toString().endsWith(LOOKUP_SUFFIX));
    }

    public static String getErrorMessageForIncorrectRefType(int sheetId, String cellColName, int cellRowNumber, Object content, ColumnType columnType, Cell refCell, ColumnType columnTypeFromRef) {
        return "setCell(" + sheetId + ", " + cellColName + ", " + cellRowNumber + ", " +
                "“" + content + "”) failed because the type of column “" + cellColName + "”\n" +
                "(" + columnType.typeName + ") doesn’t fit the type of " + content + " which is the same type as\n" +
                "column “" + refCell.getColName() + "” (" + columnTypeFromRef.typeName + " instead of " + columnType.typeName + ")";
    }

}
