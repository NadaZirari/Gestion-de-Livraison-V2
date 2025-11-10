package com.delivrey.repository;

import com.delivrey.entity.Vehicle;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;




public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
	
	 Vehicle findByRegistrationNumber(String registrationNumber);
    // Find vehicles with maxVolume greater than or equal to the given value, ordered by maxVolume in descending order
    @Query("SELECT v FROM Vehicle v WHERE v.maxVolume >= :maxVolume ORDER BY v.maxVolume DESC")
    List<Vehicle> findByMaxVolumeGreaterThanEqualOrderByMaxVolumeDesc(@Param("maxVolume") double maxVolume);
    
    // Find vehicles with maxVolume less than or equal to the given value, ordered by maxVolume in ascending order
    @Query("SELECT v FROM Vehicle v WHERE v.maxVolume <= :maxVolume ORDER BY v.maxVolume ASC")
    List<Vehicle> findByMaxVolumeLessThanEqualOrderByMaxVolumeAsc(@Param("maxVolume") double maxVolume);

	 
}
