package ru.geekbrains.habr.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.geekbrains.habr.entities.Status;
import ru.geekbrains.habr.repositories.StatusRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatusService {
    private final StatusRepository statusRepository;

    public Optional<Status> findByName(String name) {
        return statusRepository.findByName(name);
    }

}
