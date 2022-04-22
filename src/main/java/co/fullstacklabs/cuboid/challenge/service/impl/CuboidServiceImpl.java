package co.fullstacklabs.cuboid.challenge.service.impl;

import co.fullstacklabs.cuboid.challenge.dto.CuboidDTO;
import co.fullstacklabs.cuboid.challenge.exception.InternalServerException;
import co.fullstacklabs.cuboid.challenge.exception.ResourceNotFoundException;
import co.fullstacklabs.cuboid.challenge.exception.UnprocessableEntityException;
import co.fullstacklabs.cuboid.challenge.model.Bag;
import co.fullstacklabs.cuboid.challenge.model.Cuboid;
import co.fullstacklabs.cuboid.challenge.repository.BagRepository;
import co.fullstacklabs.cuboid.challenge.repository.CuboidRepository;
import co.fullstacklabs.cuboid.challenge.service.CuboidService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation class for BagService
 *
 * @author FullStack Labs
 * @version 1.0
 * @since 2021-10-22
 */
@Service
public class CuboidServiceImpl implements CuboidService {

    private final CuboidRepository repository;
    private final BagRepository bagRepository;
    private final ModelMapper mapper;

    @Autowired
    public CuboidServiceImpl(@Autowired final CuboidRepository repository,
                             final BagRepository bagRepository, final ModelMapper mapper) {
        this.repository = repository;
        this.bagRepository = bagRepository;
        this.mapper = mapper;
    }

    /**
     * Create a new cuboid and add it to its bag checking the bag available capacity.
     *
     * @param cuboidDTO DTO with cuboid properties to be persisted
     * @return CuboidDTO with the data created
     */
    @Override
    @Transactional
    public CuboidDTO create(final CuboidDTO cuboidDTO) {
        final Bag bag = getBagById(cuboidDTO.getBagId());
        final Double bagVolume = bag.getVolume();
        final float cubeVolume = cuboidDTO.getWidth() * cuboidDTO.getDepth() *  cuboidDTO.getHeight();
        Cuboid cuboid = mapper.map(cuboidDTO, Cuboid.class);
        cuboid.setBag(bag);
        if(cubeVolume >  bagVolume){
            throw new UnprocessableEntityException("Bag has not enough capacity to hold the cuboid");
        }
        cuboid = repository.save(cuboid);
        return mapper.map(cuboid, CuboidDTO.class);
    }

    @Override
    public void delete(final Long id) {
        try{
            repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cuboid not found"));
            repository.deleteById(id);
        }catch (final Exception ex){
            if(ex instanceof ResourceNotFoundException || ex instanceof UnprocessableEntityException){
                throw ex;
            }
            throw new InternalServerException("Something went wrong while trying to delete a cuboid");
        }

    }

    @Override
    @Transactional
    public CuboidDTO update(final Long id, final CuboidDTO cuboidDTO) {
        Cuboid persistedCuboid;
        try{
            final Cuboid _cuboid = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cuboid not found"));
            final Bag bag = getBagById(cuboidDTO.getBagId());
            final Double bagVolume = bag.getVolume();
            final float cubeVolume = cuboidDTO.getWidth() * cuboidDTO.getDepth() *  cuboidDTO.getHeight();
            if(cubeVolume >  bagVolume){
                throw new UnprocessableEntityException("Bag has not enough capacity to hold the cuboid");
            }
            _cuboid.setDepth(cuboidDTO.getDepth());
            _cuboid.setHeight(cuboidDTO.getHeight());
            _cuboid.setWidth(cuboidDTO.getWidth());
            _cuboid.setBag(bag);
            persistedCuboid = repository.save(_cuboid);
        }catch (final Exception ex){
            if(ex instanceof ResourceNotFoundException || ex instanceof UnprocessableEntityException){
                throw ex;
            }
            throw new InternalServerException("Something went wrong while trying to update a cuboid");
        }
        return mapper.map(persistedCuboid, CuboidDTO.class);
    }

    /**
     * List all cuboids
     * @return List<CuboidDTO>
     */
    @Override
    @Transactional(readOnly = true)
    public List<CuboidDTO> getAll() {
        final List<Cuboid> cuboids = repository.findAll();
        return cuboids.stream().map(bag -> mapper.map(bag, CuboidDTO.class))
                .collect(Collectors.toList());
    }
    private Bag getBagById(final long bagId) {
        return bagRepository.findById(bagId).orElseThrow(() -> new ResourceNotFoundException("Bag not found"));
    }



}
