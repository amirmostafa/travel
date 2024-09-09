package com.travel.service;

import com.travel.domain.TourPackage;
import com.travel.repository.TourPackageRepository;
import com.travel.service.dto.TourPackageDTO;
import com.travel.service.mapper.TourPackageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.travel.domain.TourPackage}.
 */
@Service
@Transactional
public class TourPackageService {

    private static final Logger log = LoggerFactory.getLogger(TourPackageService.class);

    private final TourPackageRepository tourPackageRepository;

    private final TourPackageMapper tourPackageMapper;

    public TourPackageService(TourPackageRepository tourPackageRepository, TourPackageMapper tourPackageMapper) {
        this.tourPackageRepository = tourPackageRepository;
        this.tourPackageMapper = tourPackageMapper;
    }

    /**
     * Save a tourPackage.
     *
     * @param tourPackageDTO the entity to save.
     * @return the persisted entity.
     */
    public TourPackageDTO save(TourPackageDTO tourPackageDTO) {
        log.debug("Request to save TourPackage : {}", tourPackageDTO);
        TourPackage tourPackage = tourPackageMapper.toEntity(tourPackageDTO);
        tourPackage = tourPackageRepository.save(tourPackage);
        return tourPackageMapper.toDto(tourPackage);
    }

    /**
     * Update a tourPackage.
     *
     * @param tourPackageDTO the entity to save.
     * @return the persisted entity.
     */
    public TourPackageDTO update(TourPackageDTO tourPackageDTO) {
        log.debug("Request to update TourPackage : {}", tourPackageDTO);
        TourPackage tourPackage = tourPackageMapper.toEntity(tourPackageDTO);
        tourPackage = tourPackageRepository.save(tourPackage);
        return tourPackageMapper.toDto(tourPackage);
    }

    /**
     * Partially update a tourPackage.
     *
     * @param tourPackageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TourPackageDTO> partialUpdate(TourPackageDTO tourPackageDTO) {
        log.debug("Request to partially update TourPackage : {}", tourPackageDTO);

        return tourPackageRepository
            .findById(tourPackageDTO.getId())
            .map(existingTourPackage -> {
                tourPackageMapper.partialUpdate(existingTourPackage, tourPackageDTO);

                return existingTourPackage;
            })
            .map(tourPackageRepository::save)
            .map(tourPackageMapper::toDto);
    }

    /**
     * Get one tourPackage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TourPackageDTO> findOne(Long id) {
        log.debug("Request to get TourPackage : {}", id);
        return tourPackageRepository.findById(id).map(tourPackageMapper::toDto);
    }

    /**
     * Delete the tourPackage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete TourPackage : {}", id);
        tourPackageRepository.deleteById(id);
    }
}
