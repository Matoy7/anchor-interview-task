package com.sheetmanager.dtos.schema;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ColumnsMetadataDTO {
    private String name;
    private String type;
}
