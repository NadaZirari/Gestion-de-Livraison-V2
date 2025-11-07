package com.delivrey.controller;

import com.delivrey.dto.DeliveryDTO;
import com.delivrey.dto.TourDTO;
import com.delivrey.entity.Delivery;
import com.delivrey.entity.Tour;
import com.delivrey.service.TourService;
import mapper.DeliveryMapper;
import mapper.TourMapper;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/tours")
@Transactional(readOnly = true)
public class TourController {

    private final TourService tourService;

    public TourController(TourService tourService) {
        this.tourService = tourService;
    }

    // --- Optimisation d’un tour (existant) ---
    @GetMapping("/{tourId}/optimize")
    @ResponseBody
    public List<DeliveryDTO> optimize(@PathVariable("tourId") Long tourId,
                                      @RequestParam(name = "algo", defaultValue = "NN") String algo) {
        List<Delivery> optimized = tourService.getOptimizedTour(tourId, algo);
        return optimized.stream().map(DeliveryMapper::toDto).collect(Collectors.toList());
    }

    // --- Distance totale d’un tour ---
    @GetMapping("/{tourId}/distance")
    @ResponseBody
    public double distance(@PathVariable("tourId") Long tourId,
                           @RequestParam(name = "algo", defaultValue = "NN") String algo) {
        return tourService.getTotalDistance(tourId, algo);
    }

    // --- Endpoint pour Swagger : retourner le Tour complet ---
    @GetMapping("/{tourId}")
    @ResponseBody
    public TourDTO getTourById(@PathVariable Long tourId) {
        Tour tour = tourService.getTourById(tourId); // méthode dans TourService
        return TourMapper.toDto(tour);               // conversion Tour -> TourDTO
    }

    // --- Endpoint pour tous les tours ---
    @GetMapping
    @ResponseBody
    public List<TourDTO> getAllTours() {
        List<Tour> tours = tourService.getAllTours();
        return tours.stream().map(TourMapper::toDto).collect(Collectors.toList());
    }

    // --- Créer un tour ---
    @PostMapping("/create")
    @ResponseBody
    @Transactional
    public TourDTO createTour(@RequestBody TourDTO dto) {
        Tour tour = TourMapper.toEntity(dto);
        Tour saved = tourService.saveTour(tour);
        return TourMapper.toDto(saved);
    }

    // --- Mettre à jour un tour ---
    @PutMapping("/{tourId}")
    @ResponseBody
    @Transactional
    public TourDTO updateTour(@PathVariable Long tourId, @RequestBody TourDTO dto) {
        Tour existing = tourService.getTourById(tourId);
        TourMapper.updateEntity(existing, dto);
        Tour saved = tourService.saveTour(existing);
        return TourMapper.toDto(saved);
    }

    // --- Supprimer un tour ---
    @DeleteMapping("/{tourId}")
    @ResponseBody
    @Transactional
    public void deleteTour(@PathVariable Long tourId) {
        tourService.deleteTour(tourId);
    }
}
