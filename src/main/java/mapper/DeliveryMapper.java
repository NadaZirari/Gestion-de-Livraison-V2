package mapper;

import com.delivrey.dto.DeliveryDTO;
import com.delivrey.entity.Delivery;

public class DeliveryMapper {

    // Conversion Entity → DTO
    public static DeliveryDTO toDto(Delivery delivery) {
        if (delivery == null) return null;

        DeliveryDTO dto = new DeliveryDTO();
        dto.setId(delivery.getId());
        dto.setAddress(delivery.getAddress());
        dto.setLatitude(delivery.getLatitude());
        dto.setLongitude(delivery.getLongitude());
        dto.setWeight(delivery.getWeight());
        dto.setVolume(delivery.getVolume());
        dto.setTimeWindow(delivery.getTimeWindow());
        dto.setStatus(delivery.getStatus() != null ? delivery.getStatus().name() : null);

        return dto;
    }

    // Conversion DTO → Entity
    public static Delivery toEntity(DeliveryDTO dto) {
        if (dto == null) return null;

        Delivery delivery = new Delivery();
        delivery.setId(dto.getId());
        delivery.setAddress(dto.getAddress());
        delivery.setLatitude(dto.getLatitude());
        delivery.setLongitude(dto.getLongitude());
        delivery.setWeight(dto.getWeight());
        delivery.setVolume(dto.getVolume());
        delivery.setTimeWindow(dto.getTimeWindow());
        // Attention : le status peut être géré côté service
        return delivery;
    }
}
