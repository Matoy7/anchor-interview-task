package com.sheetmanager.entities.schema;

import lombok.*;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Schema {
    private Map<String, ColumnType> columnMetadata;
}
