package ru.geekbrains.habr.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.habr.services.enums.ErrorMessage;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.UUID;

/**
 * Сервис для работы с с изображениями
 *
 * @author Рожко Алексей
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class ImageService {

    @Value("${upload.path}")
    private String uploadPath;

    /**
     * Сохраняет изображение по локальному пути uploadPath.
     *
     * @param file файл
     * @return String локальный путь до файла
     */
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

                return fullName;
            } catch (Exception e) {
                throw new RuntimeException(ErrorMessage.FILE_LOAD_ERROR.getField());
            }
        } else {
            throw new RuntimeException(ErrorMessage.IMAGE_ERROR.getField());
        }
    }

    /**
     * Удаляет изображение если оно существует.
     *
     * @param path путь до файла
     */
    public void deleteImage(String path) {
        File file = new File(path);

        if (file.isFile() && file.exists()) {
            file.delete();
        }

    }

    /**
     * Создает каталог, для хранения изображений если его не существует
     */
    @Bean
    private void checkPath() {
        new File(uploadPath).getAbsoluteFile().mkdirs();
    }
}
