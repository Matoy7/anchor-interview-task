package com.sheetmanager.mappers;

import com.sheetmanager.dtos.schema.ColumnMetadataDTO;
import com.sheetmanager.dtos.schema.ColumnsMetadataDTO;
import com.sheetmanager.entities.schema.ColumnType;
import com.sheetmanager.entities.schema.Schema;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Data
public class SchemaMapper {

    @Autowired
    public SchemaMapper() {
    }

    public Schema toEntity(ColumnMetadataDTO columnMetadataDTO) {
        Map<String, ColumnType> columnMetadata = new ConcurrentHashMap<>();
        List<ColumnsMetadataDTO> columns = columnMetadataDTO.getColumns();
        for (ColumnsMetadataDTO column : columns) {
            String name = column.getName();
            ColumnType columnType = ColumnType.getColumnType(column.getType());
            columnMetadata.put(name, columnType);
        }
        return new Schema(columnMetadata);
    }
}