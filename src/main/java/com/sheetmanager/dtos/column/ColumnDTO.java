package com.sheetmanager.dtos.column;

import com.sheetmanager.dtos.cell.CellDTO;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ColumnDTO {
    private Map<Integer, CellDTO> rows;
}
