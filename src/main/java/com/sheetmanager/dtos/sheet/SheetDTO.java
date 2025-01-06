package com.sheetmanager.dtos.sheet;

import com.sheetmanager.entities.column.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SheetDTO {
    private Map<String, Column> columns;
}
