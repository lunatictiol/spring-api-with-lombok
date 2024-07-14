package com.sab.lombokbasics.services;

import com.sab.lombokbasics.entities.Juice;
import com.sab.lombokbasics.mapper.JuiceMapper;
import com.sab.lombokbasics.model.JuiceDTO;
import com.sab.lombokbasics.model.JuiceStyle;
import com.sab.lombokbasics.repository.JuiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.PageRequest;
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
    public List<JuiceDTO> listJuices(String juiceName, JuiceStyle juiceStyle,
                                     Boolean showInventory,
                                     Integer pageNumber,
                                     Integer size)
    {
        List<Juice> juiceList;
        PageRequest pageRequest = buildPageRequest(pageNumber, size);

        if (StringUtils.hasText(juiceName) && juiceStyle ==null) {
            juiceList = getJuicesByName(juiceName);
        }
        else if (StringUtils.hasText(juiceName) && juiceStyle !=null){
            juiceList = getJuicesByNameAndJuiceStyle(juiceName,juiceStyle);
        }
        else if (!StringUtils.hasText(juiceName) && juiceStyle != null ) {
            juiceList = getJuicesByJuiceStyle(juiceStyle);
        }
        else {
            juiceList =  juiceRepository.findAll();

        }
        if (showInventory != null && !showInventory) {
            juiceList.forEach(juice -> juice.setQuantityOnHand(null));
        }


        return juiceList.stream().map(juiceMapper::JuiceToJuiceDTO)
                .collect(Collectors.toList());
    }

    List<Juice> getJuicesByJuiceStyle(JuiceStyle juiceStyleName) {
        return juiceRepository.findAllByJuiceStyle(juiceStyleName);
    }
    List<Juice> getJuicesByName(String name) {
        return juiceRepository.findAllByJuiceNameIsLikeIgnoreCase("%"+name+"%");
    }
    List<Juice> getJuicesByNameAndJuiceStyle(String name, JuiceStyle juiceStyle) {
        return juiceRepository.findAllByJuiceNameIsLikeIgnoreCaseAndJuiceStyle("%"+name+"%",juiceStyle);
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

        return PageRequest.of(queryPageNumber, queryPageSize);
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
