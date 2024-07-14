package com.sab.lombokbasics.services;

import com.sab.lombokbasics.model.JuiceCSVRecord;

import java.io.File;
import java.util.List;

public interface JuiceCsvService {

    List<JuiceCSVRecord> convertCSV(File csvFile);
}
