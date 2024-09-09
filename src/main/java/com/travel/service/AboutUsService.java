package com.travel.service;

import com.travel.domain.AboutUs;
import com.travel.repository.AboutUsRepository;
import com.travel.service.dto.AboutUsDTO;
import com.travel.service.mapper.AboutUsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.travel.domain.AboutUs}.
 */
@Service
@Transactional
public class AboutUsService {

    private static final Logger log = LoggerFactory.getLogger(AboutUsService.class);

    private final AboutUsRepository aboutUsRepository;

    private final AboutUsMapper aboutUsMapper;

    public AboutUsService(AboutUsRepository aboutUsRepository, AboutUsMapper aboutUsMapper) {
        this.aboutUsRepository = aboutUsRepository;
        this.aboutUsMapper = aboutUsMapper;
    }

    /**
     * Save a aboutUs.
     *
     * @param aboutUsDTO the entity to save.
     * @return the persisted entity.
     */
    public AboutUsDTO save(AboutUsDTO aboutUsDTO) {
        log.debug("Request to save AboutUs : {}", aboutUsDTO);
        AboutUs aboutUs = aboutUsMapper.toEntity(aboutUsDTO);
        aboutUs = aboutUsRepository.save(aboutUs);
        return aboutUsMapper.toDto(aboutUs);
    }

    /**
     * Update a aboutUs.
     *
     * @param aboutUsDTO the entity to save.
     * @return the persisted entity.
     */
    public AboutUsDTO update(AboutUsDTO aboutUsDTO) {
        log.debug("Request to update AboutUs : {}", aboutUsDTO);
        AboutUs aboutUs = aboutUsMapper.toEntity(aboutUsDTO);
        aboutUs = aboutUsRepository.save(aboutUs);
        return aboutUsMapper.toDto(aboutUs);
    }

    /**
     * Partially update a aboutUs.
     *
     * @param aboutUsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AboutUsDTO> partialUpdate(AboutUsDTO aboutUsDTO) {
        log.debug("Request to partially update AboutUs : {}", aboutUsDTO);

        return aboutUsRepository
            .findById(aboutUsDTO.getId())
            .map(existingAboutUs -> {
                aboutUsMapper.partialUpdate(existingAboutUs, aboutUsDTO);

                return existingAboutUs;
            })
            .map(aboutUsRepository::save)
            .map(aboutUsMapper::toDto);
    }

    /**
     * Get one aboutUs by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AboutUsDTO> findOne(Long id) {
        log.debug("Request to get AboutUs : {}", id);
        return aboutUsRepository.findById(id).map(aboutUsMapper::toDto);
    }

    /**
     * Delete the aboutUs by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete AboutUs : {}", id);
        aboutUsRepository.deleteById(id);
    }
}
