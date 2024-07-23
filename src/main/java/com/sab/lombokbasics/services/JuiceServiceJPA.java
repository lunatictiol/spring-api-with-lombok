package com.sab.lombokbasics.services;

import com.sab.lombokbasics.entities.Juice;
import com.sab.lombokbasics.mapper.JuiceMapper;
import com.sab.lombokbasics.model.JuiceDTO;
import com.sab.lombokbasics.model.JuiceStyle;
import com.sab.lombokbasics.repository.JuiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class JuiceServiceJPA implements JuiceService {
    private final JuiceRepository juiceRepository;
    private final JuiceMapper juiceMapper;
    private static final int DEFAULT_PAGE = 0;
    private static final int DEFAULT_PAGE_SIZE = 25;



    @Override
    public Page<JuiceDTO> PageJuice(String juiceName, JuiceStyle juiceStyle,
                                    Boolean showInventory,
                                    Integer pageNumber,
                                    Integer size)
    {
        Page<Juice> juicePage;
        PageRequest pageRequest = buildPageRequest(pageNumber, size);

        if (StringUtils.hasText(juiceName) && juiceStyle ==null) {
            juicePage = getJuicesByName(juiceName,pageRequest);
        }
        else if (StringUtils.hasText(juiceName) && juiceStyle !=null){
            juicePage = getJuicesByNameAndJuiceStyle(juiceName,juiceStyle,pageRequest);
        }
        else if (!StringUtils.hasText(juiceName) && juiceStyle != null ) {
            juicePage = getJuicesByJuiceStyle(juiceStyle,pageRequest);
        }
        else {
            juicePage =  juiceRepository.findAll(pageRequest);

        }
        if (showInventory != null && !showInventory) {
            juicePage.forEach(juice -> juice.setQuantityOnHand(null));
        }


        return juicePage.map(juiceMapper::JuiceToJuiceDTO);
    }

    Page<Juice> getJuicesByJuiceStyle(JuiceStyle juiceStyleName, Pageable pageable) {
        return juiceRepository.findAllByJuiceStyle(juiceStyleName, pageable);
    }
    Page<Juice> getJuicesByName(String name,Pageable pageable) {
        return juiceRepository.findAllByJuiceNameIsLikeIgnoreCase("%"+name+"%", pageable);
    }
    Page<Juice> getJuicesByNameAndJuiceStyle(String name, JuiceStyle juiceStyle,Pageable pageable) {
        return juiceRepository.findAllByJuiceNameIsLikeIgnoreCaseAndJuiceStyle("%"+name+"%",juiceStyle, pageable);
    }
    public PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber;
        int queryPageSize;

        if (pageNumber != null && pageNumber > 0) {
            queryPageNumber = pageNumber - 1;
        } else {
            queryPageNumber = DEFAULT_PAGE;
        }

        if (pageSize == null) {
            queryPageSize = DEFAULT_PAGE_SIZE;
        } else {
            if (pageSize > 1000) {
                queryPageSize = 1000;
            } else {
                queryPageSize = pageSize;
            }
        }
        Sort sort = Sort.by(Sort.Order.asc("juiceName"));



        return PageRequest.of(queryPageNumber, queryPageSize,sort);
    }

    @Override
    public Optional<JuiceDTO> getJuiceById(UUID id) {
        return Optional.ofNullable(
                juiceMapper.JuiceToJuiceDTO(juiceRepository.findById(id).orElse(null))
        );
    }

    @Override
    public JuiceDTO saveNewJuice(JuiceDTO juiceDto) {
        return juiceMapper.JuiceToJuiceDTO(juiceRepository.save(juiceMapper.JuiceDTOToJuice(juiceDto)));
    }



    @Override
    public Optional<JuiceDTO> updateJuice(UUID id, JuiceDTO juiceDto) {
        AtomicReference<Optional<JuiceDTO>> atomicReference = new AtomicReference<>();
        juiceRepository.findById(id).ifPresentOrElse( foundJuice -> {
         foundJuice.setJuiceName(juiceDto.getJuiceName());
         foundJuice.setPrice(juiceDto.getPrice());
         foundJuice.setUpc(juiceDto.getUpc());
         foundJuice.setJuiceStyle(juiceDto.getJuiceStyle());
         //foundJuice.setQuantityOnHand(juiceDto.getQuantityOnHand());

        atomicReference.set(Optional.of(juiceMapper.JuiceToJuiceDTO(juiceRepository.save(foundJuice))));

     },()->{
          atomicReference.set(Optional.empty());
        });
    return atomicReference.get();
    }

    @Override
    public Boolean deleteJuice(UUID id) {
        if (juiceRepository.existsById(id)){
           juiceRepository.deleteById(id);
           return true;
        }
        return false;
    }

    @Override
    public Optional<JuiceDTO> patchJuiceById(UUID Id, JuiceDTO juiceDto) {
        AtomicReference<Optional<JuiceDTO>> atomicReference = new AtomicReference<>();

        juiceRepository.findById(Id).ifPresentOrElse(foundJuice -> {
            if (StringUtils.hasText(juiceDto.getJuiceName())){
                foundJuice.setJuiceName(juiceDto.getJuiceName());
            }

            if (StringUtils.hasText(juiceDto.getUpc())){
                foundJuice.setUpc(juiceDto.getUpc());
            }
            if (juiceDto.getPrice() != null){
                foundJuice.setPrice(juiceDto.getPrice());
            }
            if (juiceDto.getJuiceStyle() != null){
                foundJuice.setJuiceStyle(juiceDto.getJuiceStyle());
            }
            if (juiceDto.getQuantityOnHand() != null){
                foundJuice.setQuantityOnHand(juiceDto.getQuantityOnHand());
            }
            atomicReference.set(Optional.of(juiceMapper
                    .JuiceToJuiceDTO(juiceRepository.save(foundJuice))));
        }, () -> {
            atomicReference.set(Optional.empty());
        });

        return atomicReference.get();

    }
}
