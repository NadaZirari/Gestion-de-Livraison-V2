package com.delivrey.service;

import com.delivrey.entity.*;
import java.util.List;

public interface TourService {

	
	 List<Delivery> getOptimizedTour(Long tourId, String algorithm); 
	    double getTotalDistance(Long tourId, String algorithm);
	   
	       

	        // Méthodes pour le CRUD Tour
	        Tour getTourById(Long id);
	        List<Tour> getAllTours();
	        Tour saveTour(Tour tour);
	        void deleteTour(Long id);
	    }

    

