package mapper;

import com.delivrey.dto.WarehouseDTO;
import com.delivrey.entity.Warehouse;

public class WarehouseMapper {

    // Conversion Entity → DTO
    public static WarehouseDTO toDTO(Warehouse warehouse) {
        if (warehouse == null) return null;

        WarehouseDTO dto = new WarehouseDTO();
        dto.setId(warehouse.getId());
        dto.setAddress(warehouse.getAddress());
        dto.setLatitude(warehouse.getLatitude());
        dto.setLongitude(warehouse.getLongitude());
        dto.setOpeningHours(warehouse.getOpeningHours());

        return dto;
    }

    // Conversion DTO → Entity
    public static Warehouse toEntity(WarehouseDTO dto) {
        if (dto == null) return null;

        Warehouse warehouse = new Warehouse();
        warehouse.setId(dto.getId());
        warehouse.setAddress(dto.getAddress());
        warehouse.setLatitude(dto.getLatitude());
        warehouse.setLongitude(dto.getLongitude());
        warehouse.setOpeningHours(dto.getOpeningHours() != null ? dto.getOpeningHours() : "06:00-22:00");

        return warehouse;
    }
}
