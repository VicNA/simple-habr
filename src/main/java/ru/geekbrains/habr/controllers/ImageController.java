package ru.geekbrains.habr.controllers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.geekbrains.habr.dtos.ResponseMessage;
import ru.geekbrains.habr.services.ImageService;

@RestController
@RequestMapping("/api/v1/image")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping()
    private ResponseMessage addImage(@RequestParam("file") MultipartFile file){
        return new ResponseMessage(imageService.saveImage(file));
    }
}

