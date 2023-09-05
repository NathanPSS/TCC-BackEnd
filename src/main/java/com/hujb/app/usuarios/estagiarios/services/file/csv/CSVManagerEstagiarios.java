package com.hujb.app.usuarios.estagiarios.services.file.csv;

import com.hujb.app.usuarios.estagiarios.entities.Estagiario;
import com.hujb.app.usuarios.estagiarios.repositories.EstagiariosRepository;
import com.hujb.app.usuarios.estagiarios.services.file.FileManagerEstagiarios;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.List;
@Service
public class CSVManagerEstagiarios implements FileManagerEstagiarios<CSVParser> {

    private final EstagiariosRepository repository;

    @Autowired
    public CSVManagerEstagiarios(EstagiariosRepository repository) {
        this.repository = repository;
    }

    @Override
    public void readFile(MultipartFile file) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream()));
        CSVParser csvParser  = new CSVParser(bufferedReader,CSVFormat.EXCEL);
        saveInDatabase(csvParser);
        }
        @Override
        public void saveInDatabase(CSVParser csv) {
        List<CSVRecord> csvRecords = csv.getRecords();
        for (CSVRecord record : csvRecords) {
            Estagiario estagiario = new Estagiario(record.get("matricula"));
            repository.save(estagiario);
        }
    }
}
