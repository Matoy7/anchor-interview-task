package com.sheetmanager.entities.sheet;
import com.sheetmanager.entities.column.Column;
import com.sheetmanager.entities.schema.Schema;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Sheet {
    private Schema schema;
    private Map<String, Column> columns;
}
