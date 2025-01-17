package com.sheetmanager.entities.cell;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Cell {
    private int sheetId;
    private String ColName;
    private int rowNumber;
    private Object content;
}
