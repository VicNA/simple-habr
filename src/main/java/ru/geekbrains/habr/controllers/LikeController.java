package ru.geekbrains.habr.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.geekbrains.habr.dtos.LikeDto;
import ru.geekbrains.habr.services.LikeService;

@RestController
@RequestMapping("/api/v1/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestBody LikeDto likeDto) {
        likeService.add(likeDto);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
