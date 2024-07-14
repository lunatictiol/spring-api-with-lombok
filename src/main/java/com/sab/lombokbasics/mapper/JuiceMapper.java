package com.sab.lombokbasics.mapper;

import com.sab.lombokbasics.entities.Juice;
import com.sab.lombokbasics.model.JuiceDTO;
import org.mapstruct.Mapper;

@Mapper
public interface JuiceMapper {

    Juice JuiceDTOToJuice(JuiceDTO dto);
    JuiceDTO JuiceToJuiceDTO(Juice juice);
}
