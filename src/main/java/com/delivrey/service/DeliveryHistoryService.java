package com.delivrey.service;

import com.delivrey.dto.DeliveryHistoryDto;
import com.delivrey.entity.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public interface DeliveryHistoryService {
    
    @Transactional
    void createHistoryFromTour(Tour tour);

    @Transactional(readOnly = true)
    @NonNull
    Page<DeliveryHistoryDto> findAll(@NonNull Pageable pageable);

    @Transactional(readOnly = true)
    @NonNull
    Optional<DeliveryHistoryDto> findById(@NonNull Long id);
    
    @Transactional(readOnly = true)
    @NonNull
    Page<DeliveryHistoryDto> findByCustomerId(@NonNull Long customerId, @NonNull Pageable pageable);
    
    @Transactional(readOnly = true)
    @NonNull
    Page<DeliveryHistoryDto> findByTourId(@NonNull Long tourId, @NonNull Pageable pageable);
    
    @Transactional(readOnly = true)
    @NonNull
    Page<DeliveryHistoryDto> findByDeliveryDateBetween(
            @NonNull LocalDate startDate, 
            @NonNull LocalDate endDate, 
            @NonNull Pageable pageable);
    
    @Transactional(readOnly = true)
    @NonNull
    Map<String, Object> getCustomerDeliveryStats(@NonNull Long customerId);
    
    @Transactional(readOnly = true)
    @NonNull
    Map<String, Object> getTourDeliveryStats(@NonNull Long tourId);
}
