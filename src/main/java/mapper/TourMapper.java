package mapper;

import com.delivrey.dto.TourDTO;
import com.delivrey.dto.DeliveryDTO;
import com.delivrey.entity.Tour;
import com.delivrey.entity.TourStatus;
import org.hibernate.Hibernate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TourMapper {

    public static TourDTO toDto(Tour tour) {
        if (tour == null) return null;
        
        TourDTO dto = new TourDTO();
        dto.setId(tour.getId());
        dto.setDate(tour.getTourDate());
        dto.setVehicleId(tour.getVehicle() != null ? tour.getVehicle().getId() : null);
        dto.setWarehouseId(tour.getWarehouse() != null ? tour.getWarehouse().getId() : null);
        dto.setStatus(tour.getTourStatus() != null ? tour.getTourStatus().name() : null);
        dto.setAlgorithmUsed(tour.getAlgorithmUsed());
        
        // Safely handle lazy-loaded collections
        try {
            if (Hibernate.isInitialized(tour.getDeliveries()) && tour.getDeliveries() != null) {
                dto.setDeliveryIds(tour.getDeliveries().stream()
                    .map(delivery -> delivery.getId())
                    .collect(Collectors.toList()));
            }
        } catch (Exception e) {
            // Ignore if collection is not initialized
        }
        
        return dto;
    }

    public static Tour toEntity(TourDTO dto) {
        if (dto == null) return null;
        
        Tour tour = new Tour();
        tour.setId(dto.getId());
        tour.setTourDate(dto.getDate());
        
        if (dto.getStatus() != null) {
            try {
                tour.setTourStatus(TourStatus.valueOf(dto.getStatus()));
            } catch (IllegalArgumentException e) {
                // Handle invalid status value
                tour.setTourStatus(TourStatus.PLANNED);
            }
        } else {
            tour.setTourStatus(TourStatus.PLANNED);
        }
        
        tour.setAlgorithmUsed(dto.getAlgorithmUsed());
        return tour;
    }

    public static void updateEntity(Tour tour, TourDTO dto) {
        if (tour == null || dto == null) return;
        
        tour.setTourDate(dto.getDate());
        
        if (dto.getStatus() != null) {
            try {
                tour.setTourStatus(TourStatus.valueOf(dto.getStatus()));
            } catch (IllegalArgumentException e) {
                // Handle invalid status value
            }
        }
        
        if (dto.getAlgorithmUsed() != null) {
            tour.setAlgorithmUsed(dto.getAlgorithmUsed());
        }
    }
}
