package mapper;


import com.delivrey.dto.VehicleDTO;
import com.delivrey.entity.Vehicle;
import com.delivrey.entity.VehicleType;

public class VehicleMapper {

    // Conversion Entity → DTO
    public static VehicleDTO toDTO(Vehicle vehicle) {
        if (vehicle == null) return null;

        VehicleDTO dto = new VehicleDTO();
        dto.setId(vehicle.getId());
        dto.setRegistrationNumber(vehicle.getRegistrationNumber());
        dto.setType(vehicle.getType() != null ? vehicle.getType().name() : null);
        dto.setMaxWeight(vehicle.getMaxWeight());
        dto.setMaxVolume(vehicle.getMaxVolume());
        dto.setMaxDeliveries(vehicle.getMaxDeliveries());

        return dto;
    }

    // Conversion DTO → Entity
    public static Vehicle toEntity(VehicleDTO dto) {
        if (dto == null) return null;

        Vehicle vehicle = new Vehicle();
        vehicle.setId(dto.getId());
        vehicle.setRegistrationNumber(dto.getRegistrationNumber());
        vehicle.setMaxWeight(dto.getMaxWeight());
        vehicle.setMaxVolume(dto.getMaxVolume());
        vehicle.setMaxDeliveries(dto.getMaxDeliveries());

        if (dto.getType() != null) {
            try {
                vehicle.setType(VehicleType.valueOf(dto.getType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                vehicle.setType(VehicleType.TRUCK); // valeur par défaut si type invalide
            }
        }

        return vehicle;
    }
}
