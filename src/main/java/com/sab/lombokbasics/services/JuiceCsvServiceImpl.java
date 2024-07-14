package com.sab.lombokbasics.services;

import com.opencsv.bean.CsvToBeanBuilder;
import com.sab.lombokbasics.model.JuiceCSVRecord;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
@Service

public class JuiceCsvServiceImpl implements JuiceCsvService {
    @Override
    public List<JuiceCSVRecord> convertCSV(File csvFile) {
        try {
            return new CsvToBeanBuilder<JuiceCSVRecord>(new FileReader(csvFile))
                    .withType(JuiceCSVRecord.class)
                    .build().parse();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
