package com.hujb.app.usuarios.estagiarios.services.file;

import org.apache.commons.csv.CSVRecord;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.util.List;

public interface FileManagerEstagiarios<T> {

     void readFile(MultipartFile file) throws IOException;

     void saveInDatabase(T file);

}
