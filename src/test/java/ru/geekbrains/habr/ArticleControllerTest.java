package ru.geekbrains.habr;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.geekbrains.habr.converters.ArticleConverter;
import ru.geekbrains.habr.dtos.ArticleDto;
import ru.geekbrains.habr.entities.*;
import ru.geekbrains.habr.services.ArticleService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@SpringBootTest
@AutoConfigureMockMvc
public class ArticleControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    ArticleService articleService;

    @MockBean
    ArticleConverter articleConverter;

    @BeforeEach
    public void initTest() {
        Role role = new Role();
        role.setId(3L);
        role.setName("administrator");

        User user = new User();
        user.setId(1L);
        user.setUsername("Piter");
        user.setRoles(List.of(role));

        Status status = new Status();
        status.setId(3L);
        status.setName("published");

        Category category = new Category();
        category.setId(4L);
        category.setName("Web dev");

        Article article = new Article();
        article.setId(22L);
        article.setTitle("Заголовок");
        article.setText("Текст статьи");
        article.setDtCreated(LocalDateTime.now());
        article.setDtPublished(LocalDateTime.now());
        article.setUser(user);
        article.setStatus(status);
        article.setCategories(List.of(category));

        ArticleDto articleDto = new ArticleDto(
                article.getId(),
                article.getTitle(),
                article.getText(),
                article.getUser(),
                article.getStatus(),
                article.getDtCreated(),
                article.getDtPublished()
        );

        Article article2 = new Article();
        article2.setId(33L);
        article2.setTitle("Заголовок2");
        article2.setText("Текст статьи_2");
        article2.setDtCreated(LocalDateTime.now());
        article2.setDtPublished(LocalDateTime.now());
        article.setUser(user);
        article2.setStatus(status);
        article2.setCategories(List.of(category));

        ArticleDto articleDto2 = new ArticleDto(
                article2.getId(),
                article2.getTitle(),
                article2.getText(),
                article2.getUser(),
                article2.getStatus(),
                article2.getDtCreated(),
                article2.getDtPublished()
        );

        List<Article> articleList = new ArrayList<>(List.of(article, article2));

        Mockito.doReturn(Optional.of(article)).when(articleService).findById(22L);
        Mockito.doReturn(articleDto).when(articleConverter).entityToDto(article);
        Mockito.doReturn(articleDto2).when(articleConverter).entityToDto(article2);
        Mockito.doReturn(articleList).when(articleService).findAllSortDescPage(1);

    }

    @Test
    public void findAll() throws Exception {
        mockMvc.perform(
                get("/api/v1/articles")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    public void findById() throws Exception {
        mockMvc
                .perform(
                        get("/api/v1/articles/view/22")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists());
    }
}
