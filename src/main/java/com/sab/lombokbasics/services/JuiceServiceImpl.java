package com.sab.lombokbasics.services;

import com.sab.lombokbasics.model.JuiceDTO;
import com.sab.lombokbasics.model.JuiceStyle;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class JuiceServiceImpl implements JuiceService {
    private Map<UUID, JuiceDTO> JuiceMap;
   public JuiceServiceImpl() {
       this.JuiceMap = new HashMap<>();

       JuiceDTO juiceDTO1 = JuiceDTO.builder()
               .id(UUID.randomUUID())
               .version(1)
               .juiceName("Galaxy Cat")
               .upc("12356")
               .juiceStyle(JuiceStyle.PALE_ALE)
               .price(new BigDecimal("12.99"))
               .quantityOnHand(122)
               .createdDate(LocalDateTime.now())
               .updateDate(LocalDateTime.now())
               .build();

       JuiceDTO juiceDTO2 = JuiceDTO.builder()
               .id(UUID.randomUUID())
               .version(1)
               .juiceName("Crank")
               .juiceStyle(JuiceStyle.PALE_ALE)
               .upc("12356222")
               .price(new BigDecimal("11.99"))
               .quantityOnHand(392)
               .createdDate(LocalDateTime.now())
               .updateDate(LocalDateTime.now())
               .build();

       JuiceDTO juiceDTO3 = JuiceDTO.builder()
               .id(UUID.randomUUID())
               .version(1)
               .juiceStyle(JuiceStyle.PALE_ALE)
               .juiceName("Sunshine City")
               .upc("12356")
               .price(new BigDecimal("13.99"))
               .quantityOnHand(144)
               .createdDate(LocalDateTime.now())
               .updateDate(LocalDateTime.now())
               .build();

       JuiceMap.put(juiceDTO1.getId(), juiceDTO1);
       JuiceMap.put(juiceDTO2.getId(), juiceDTO2);
       JuiceMap.put(juiceDTO3.getId(), juiceDTO3);
   }

    @Override
    public List<JuiceDTO> listJuices(String juiceName, JuiceStyle juiceStyle, Boolean showInventory, Integer pageNumber, Integer size) {
        return new ArrayList<>(JuiceMap.values());
    }

    @Override
    public JuiceDTO saveNewJuice(JuiceDTO juiceDto) {
        JuiceDTO savedJuiceDTO = JuiceDTO.builder()
                .id(UUID.randomUUID())
                .version(1)
                .juiceStyle(juiceDto.getJuiceStyle())
                .createdDate(LocalDateTime.now())
                .updateDate(LocalDateTime.now())
                .juiceName(juiceDto.getJuiceName())
                .quantityOnHand(juiceDto.getQuantityOnHand())
                .upc(juiceDto.getUpc())
                .price(juiceDto.getPrice())
                .build();

        JuiceMap.put(savedJuiceDTO.getId(), savedJuiceDTO);

        return savedJuiceDTO;
    }

    @Override
    public Optional<JuiceDTO> getJuiceById(UUID id) {
        return Optional.of(JuiceMap.get(id));
    }

    @Override
    public Optional<JuiceDTO> updateJuice(UUID id, JuiceDTO juiceDto) {
       JuiceDTO existingJuiceDTO = JuiceMap.get(id);
       existingJuiceDTO.setJuiceName(juiceDto.getJuiceName());
       existingJuiceDTO.setPrice(juiceDto.getPrice());
       existingJuiceDTO.setUpc(juiceDto.getUpc());
       existingJuiceDTO.setJuiceStyle(juiceDto.getJuiceStyle());
       existingJuiceDTO.setQuantityOnHand(juiceDto.getQuantityOnHand());


       JuiceMap.put(existingJuiceDTO.getId(), existingJuiceDTO);
       return Optional.of(existingJuiceDTO);
    }

    @Override
    public Boolean deleteJuice(UUID id) {
        JuiceMap.remove(id);
        return true;
    }

    @Override
    public Optional<JuiceDTO> patchJuiceById(UUID id, JuiceDTO juiceDto) {
        JuiceDTO existing = JuiceMap.get(id);

        if (StringUtils.hasText(juiceDto.getJuiceName())){
            existing.setJuiceName(juiceDto.getJuiceName());
        }


        if (juiceDto.getPrice() != null) {
            existing.setPrice(juiceDto.getPrice());
        }
        if (juiceDto.getJuiceStyle() !=null){
            existing.setJuiceStyle(juiceDto.getJuiceStyle());
        }

        if (juiceDto.getQuantityOnHand() != null){
            existing.setQuantityOnHand(juiceDto.getQuantityOnHand());
        }


        if (StringUtils.hasText(juiceDto.getUpc())) {
            existing.setUpc(juiceDto.getUpc());
        }
   return Optional.of(existing);
   }


}
