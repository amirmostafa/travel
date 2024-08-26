package com.travel.service;

import com.travel.domain.RoomPrice;
import com.travel.repository.RoomPriceRepository;
import com.travel.service.dto.RoomPriceDTO;
import com.travel.service.mapper.RoomPriceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.travel.domain.RoomPrice}.
 */
@Service
@Transactional
public class RoomPriceService {

    private static final Logger log = LoggerFactory.getLogger(RoomPriceService.class);

    private final RoomPriceRepository roomPriceRepository;

    private final RoomPriceMapper roomPriceMapper;

    public RoomPriceService(RoomPriceRepository roomPriceRepository, RoomPriceMapper roomPriceMapper) {
        this.roomPriceRepository = roomPriceRepository;
        this.roomPriceMapper = roomPriceMapper;
    }

    /**
     * Save a roomPrice.
     *
     * @param roomPriceDTO the entity to save.
     * @return the persisted entity.
     */
    public RoomPriceDTO save(RoomPriceDTO roomPriceDTO) {
        log.debug("Request to save RoomPrice : {}", roomPriceDTO);
        RoomPrice roomPrice = roomPriceMapper.toEntity(roomPriceDTO);
        roomPrice = roomPriceRepository.save(roomPrice);
        return roomPriceMapper.toDto(roomPrice);
    }

    /**
     * Update a roomPrice.
     *
     * @param roomPriceDTO the entity to save.
     * @return the persisted entity.
     */
    public RoomPriceDTO update(RoomPriceDTO roomPriceDTO) {
        log.debug("Request to update RoomPrice : {}", roomPriceDTO);
        RoomPrice roomPrice = roomPriceMapper.toEntity(roomPriceDTO);
        roomPrice = roomPriceRepository.save(roomPrice);
        return roomPriceMapper.toDto(roomPrice);
    }

    /**
     * Partially update a roomPrice.
     *
     * @param roomPriceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RoomPriceDTO> partialUpdate(RoomPriceDTO roomPriceDTO) {
        log.debug("Request to partially update RoomPrice : {}", roomPriceDTO);

        return roomPriceRepository
            .findById(roomPriceDTO.getId())
            .map(existingRoomPrice -> {
                roomPriceMapper.partialUpdate(existingRoomPrice, roomPriceDTO);

                return existingRoomPrice;
            })
            .map(roomPriceRepository::save)
            .map(roomPriceMapper::toDto);
    }

    /**
     * Get all the roomPrices.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<RoomPriceDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RoomPrices");
        return roomPriceRepository.findAll(pageable).map(roomPriceMapper::toDto);
    }

    /**
     * Get one roomPrice by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RoomPriceDTO> findOne(Long id) {
        log.debug("Request to get RoomPrice : {}", id);
        return roomPriceRepository.findById(id).map(roomPriceMapper::toDto);
    }

    /**
     * Delete the roomPrice by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RoomPrice : {}", id);
        roomPriceRepository.deleteById(id);
    }
}
