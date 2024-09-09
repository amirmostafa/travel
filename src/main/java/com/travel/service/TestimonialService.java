package com.travel.service;

import com.travel.domain.Testimonial;
import com.travel.repository.TestimonialRepository;
import com.travel.service.dto.TestimonialDTO;
import com.travel.service.mapper.TestimonialMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.travel.domain.Testimonial}.
 */
@Service
@Transactional
public class TestimonialService {

    private static final Logger log = LoggerFactory.getLogger(TestimonialService.class);

    private final TestimonialRepository testimonialRepository;

    private final TestimonialMapper testimonialMapper;

    public TestimonialService(TestimonialRepository testimonialRepository, TestimonialMapper testimonialMapper) {
        this.testimonialRepository = testimonialRepository;
        this.testimonialMapper = testimonialMapper;
    }

    /**
     * Save a testimonial.
     *
     * @param testimonialDTO the entity to save.
     * @return the persisted entity.
     */
    public TestimonialDTO save(TestimonialDTO testimonialDTO) {
        log.debug("Request to save Testimonial : {}", testimonialDTO);
        Testimonial testimonial = testimonialMapper.toEntity(testimonialDTO);
        testimonial = testimonialRepository.save(testimonial);
        return testimonialMapper.toDto(testimonial);
    }

    /**
     * Update a testimonial.
     *
     * @param testimonialDTO the entity to save.
     * @return the persisted entity.
     */
    public TestimonialDTO update(TestimonialDTO testimonialDTO) {
        log.debug("Request to update Testimonial : {}", testimonialDTO);
        Testimonial testimonial = testimonialMapper.toEntity(testimonialDTO);
        testimonial = testimonialRepository.save(testimonial);
        return testimonialMapper.toDto(testimonial);
    }

    /**
     * Partially update a testimonial.
     *
     * @param testimonialDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TestimonialDTO> partialUpdate(TestimonialDTO testimonialDTO) {
        log.debug("Request to partially update Testimonial : {}", testimonialDTO);

        return testimonialRepository
            .findById(testimonialDTO.getId())
            .map(existingTestimonial -> {
                testimonialMapper.partialUpdate(existingTestimonial, testimonialDTO);

                return existingTestimonial;
            })
            .map(testimonialRepository::save)
            .map(testimonialMapper::toDto);
    }

    /**
     * Get one testimonial by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TestimonialDTO> findOne(Long id) {
        log.debug("Request to get Testimonial : {}", id);
        return testimonialRepository.findById(id).map(testimonialMapper::toDto);
    }

    /**
     * Delete the testimonial by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Testimonial : {}", id);
        testimonialRepository.deleteById(id);
    }
}
