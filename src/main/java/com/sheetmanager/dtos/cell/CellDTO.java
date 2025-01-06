package com.sheetmanager.dtos.cell;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CellDTO {
    private String ColName;
    private int rowNumber;
    private Object content;
}
