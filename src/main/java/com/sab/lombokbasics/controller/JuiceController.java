package com.sab.lombokbasics.controller;

import com.sab.lombokbasics.model.JuiceDTO;
import com.sab.lombokbasics.model.JuiceStyle;
import com.sab.lombokbasics.services.JuiceService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController

public class JuiceController {
    public static final String JUICE_PATH = "/api/v1/Juice";
    public static final String JUICE_PATH_WITH_ID = JUICE_PATH + "/{id}";
    private final JuiceService juiceService;

    @GetMapping(path = JUICE_PATH)
    public List<JuiceDTO> listJuices(@RequestParam(required = false) String juiceName,
                                     @RequestParam(required = false) JuiceStyle juiceStyle,
                                     @RequestParam(required = false) Boolean showInventory,
                                     @RequestParam(required = false) Integer pageNumber,
                                     @RequestParam(required = false) Integer size) {
        return juiceService.listJuices(juiceName, juiceStyle, showInventory, 1, 25);
    }

    @GetMapping(path = JUICE_PATH_WITH_ID)
    public JuiceDTO getJuiceById(@PathVariable UUID id) {

        log.debug("Get Beer by Id - in controller");

        return juiceService.getJuiceById(id).orElseThrow(NotFoundException::new);
    }

    @PostMapping(path = JUICE_PATH)
    public ResponseEntity createJuice(@Validated @RequestBody JuiceDTO juiceDto) {
        JuiceDTO savedJuiceDTO = juiceService.saveNewJuice(juiceDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("location", "api/v1/juice/" + savedJuiceDTO.getId().toString());
        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping(path = JUICE_PATH_WITH_ID)
    public ResponseEntity updateJuice(@PathVariable UUID id, @Validated @RequestBody JuiceDTO juiceDto) {
        if(juiceService.updateJuice(id, juiceDto).isEmpty()){
            throw new NotFoundException();
        }
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping(path = JUICE_PATH_WITH_ID)
    public ResponseEntity deleteJuice(@PathVariable UUID id) {
        if (juiceService.deleteJuice(id)){
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        else {
            throw new NotFoundException();
        }


    }
    @PatchMapping(path = JUICE_PATH_WITH_ID)
    public ResponseEntity patchJuice(@PathVariable UUID id, @RequestBody JuiceDTO juiceDto) {
        juiceService.patchJuiceById(id, juiceDto);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
