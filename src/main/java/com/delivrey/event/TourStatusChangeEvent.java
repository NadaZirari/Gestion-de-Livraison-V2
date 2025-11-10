package com.delivrey.event;

import com.delivrey.entity.Tour;
import com.delivrey.entity.TourStatus;
import org.springframework.context.ApplicationEvent;

public class TourStatusChangeEvent extends ApplicationEvent {
    
    private final Tour tour;
    private final TourStatus oldStatus;
    private final TourStatus newStatus;
    
    public TourStatusChangeEvent(Object source, Tour tour, TourStatus oldStatus, TourStatus newStatus) {
        super(source);
        this.tour = tour;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }
    
    public Tour getTour() {
        return tour;
    }
    
    public TourStatus getOldStatus() {
        return oldStatus;
    }
    
    public TourStatus getNewStatus() {
        return newStatus;
    }
}
