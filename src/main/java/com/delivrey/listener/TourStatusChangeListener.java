package com.delivrey.listener;

import com.delivrey.entity.TourStatus;
import com.delivrey.event.TourStatusChangeEvent;
import com.delivrey.service.DeliveryHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TourStatusChangeListener {
    
    private final DeliveryHistoryService deliveryHistoryService;
    
    @Async
    @TransactionalEventListener(
        phase = TransactionPhase.AFTER_COMMIT,
        classes = TourStatusChangeEvent.class
    )
    public void handleTourStatusChange(TourStatusChangeEvent event) {
        if (event.getNewStatus() == TourStatus.COMPLETED) {
            deliveryHistoryService.createHistoryFromTour(event.getTour());
        }
    }
}
