package com.sab.lombokbasics.services;

import com.sab.lombokbasics.model.JuiceDTO;
import com.sab.lombokbasics.model.JuiceStyle;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JuiceService {
        List<JuiceDTO> listJuices(String juiceName, JuiceStyle juiceStyle, Boolean showInventory, Integer pageNumber, Integer size);
        JuiceDTO saveNewJuice(JuiceDTO juiceDto);
        Optional<JuiceDTO> getJuiceById(UUID id);
        Optional<JuiceDTO> updateJuice(UUID id, JuiceDTO juiceDto);
        Boolean deleteJuice(UUID id);
        Optional<JuiceDTO> patchJuiceById(UUID Id, JuiceDTO juiceDto);
}
