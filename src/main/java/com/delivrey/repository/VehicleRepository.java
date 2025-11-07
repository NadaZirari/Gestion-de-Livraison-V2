package com.delivrey.repository;

import com.delivrey.entity.Vehicle;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;




public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
	
	 Vehicle findByRegistrationNumber(String registrationNumber);
	 List <Vehicle>findByMaxVolumeOrderByDesc(double maxVolume);
	 
	 @Query("select v from Vehicles v orderby v.maxVolume DESC"  )
	 List <Vehicle>findByMaxVolume( @Param(value = "maxVolume") double maxVolume);

	 
}
