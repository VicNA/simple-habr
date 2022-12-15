package ru.geekbrains.habr.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.geekbrains.habr.entities.Status;
import ru.geekbrains.habr.repositories.StatusRepository;

import java.util.Optional;

/**
 * Сервис для работы со статусами
 *
 * @author Татьяна Коваленко
 *
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class StatusService {
    private final StatusRepository statusRepository;

    /**
     * Находит статус по наименованию
     *
     * @param name Наименование статуса
     * @return Статус
     */
    public Optional<Status> findByName(String name) {
        return statusRepository.findByName(name);
    }

}
