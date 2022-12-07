package ru.geekbrains.habr.services;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${upload.path}")
    private String uploadPath;

    public String saveImage(MultipartFile file) {
        String filename;
        if (!file.isEmpty()) {
            try {
                filename = String.format("%s.%s", UUID.randomUUID(), file.getOriginalFilename());
                String fullName = uploadPath + File.separator + filename;
                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(
                                new FileOutputStream(fullName));
                stream.write(bytes);
                stream.close();
                System.out.println("Мы загрузили файл   " + filename);
                return fullName;
            } catch (Exception e) {
                throw new RuntimeException("Ошибка загрузки файла");
            }
        } else {
            throw new RuntimeException("А картинки-то нету");
        }
    }

    public boolean deleteImage(String path){
        File file = new File(path);

        if(file.isFile() && file.exists()){
            System.out.println("Мы удалили файл   " + file.getName());
            return file.delete();
        }

        return false;
    }

    /**
     * Создает каталог, для хранения изображений если его не существует
     */
    @Bean
    private void checkPath(){
        new File(uploadPath).getAbsoluteFile().mkdirs();
    }
}
