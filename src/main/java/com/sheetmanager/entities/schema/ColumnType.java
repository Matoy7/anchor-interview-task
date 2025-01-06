package com.sheetmanager.entities.schema;

public enum ColumnType {
    INT ("int", Integer.class),
    DOUBLE("double", Double.class),
    BOOLEAN("boolean", Boolean.class),
    STRING("string", String.class);

    public final String typeName;
    public final Class type;
    ColumnType(String typeName, Class type) {
        this.type = type;
        this.typeName = typeName;
    }

    public static ColumnType getColumnType(String typeName) {
        for (ColumnType e : values()) {
            if (e.typeName.equals(typeName)) {
                return e;
            }
        }
        return null;
    }

    public static boolean isContentMatchColType(ColumnType columnType, Object value) {
        return columnType.type.isInstance(value);
    }
}
