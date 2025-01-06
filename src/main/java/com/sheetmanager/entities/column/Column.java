package com.sheetmanager.entities.column;

import com.sheetmanager.entities.cell.Cell;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Column {
    private Map<Integer, Cell> rows;
}
