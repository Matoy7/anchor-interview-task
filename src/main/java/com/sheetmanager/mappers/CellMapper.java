package com.sheetmanager.mappers;

import com.sheetmanager.dtos.cell.CellDTO;
import com.sheetmanager.entities.cell.Cell;
import com.sheetmanager.services.SheetService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Data
public class CellMapper extends BaseMapperService<Cell, CellDTO> {


    public List<CellDTO> toDTOList(List<Cell> entities, SheetService sheetService) {
        List<CellDTO> list = new ArrayList<>();
        for (Cell entity : entities) {
            list.add(toDTO(entity, sheetService));
        }
        return list;
    }

    public CellDTO toDTO(Cell cell, SheetService sheetService) {
        CellDTO cellDTO = super.toDTO(cell);
        Object cellActualContent = sheetService.getCellActualContent(cell.getSheetId(), cell.getContent());
        cellDTO.setContent(cellActualContent);
        return cellDTO;
    }

    @Override
    public Class getBaseEntityType() {
        return Cell.class;
    }

    @Override
    public Class getDTOEntityType() {
        return CellDTO.class;
    }
}