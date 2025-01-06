package com.sheetmanager.dtos.schema;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ColumnMetadataDTO {
    private List<ColumnsMetadataDTO> columns;
}
