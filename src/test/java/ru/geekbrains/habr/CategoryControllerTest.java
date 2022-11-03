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
import ru.geekbrains.habr.converters.CategoryConverter;
import ru.geekbrains.habr.dtos.CategoryDto;
import ru.geekbrains.habr.entities.Category;
import ru.geekbrains.habr.services.CategoryService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CategoryService categoryService;

    @MockBean
    CategoryConverter categoryConverter;

    @BeforeEach
    public void initTest() {
        Category category1 = new Category();
        category1.setId(1L);
        category1.setName("Marketing");

        CategoryDto categoryDto1 = new CategoryDto(category1.getId(), category1.getName());

        Category category2 = new Category();
        category2.setId(2L);
        category2.setName("Design");

        CategoryDto categoryDto2 = new CategoryDto(category2.getId(), category2.getName());

        List<Category> categoryList = new ArrayList<>(Arrays.asList(category1, category2));

        Mockito.doReturn(categoryDto1).when(categoryConverter).entityToDto(category1);
        Mockito.doReturn(categoryDto2).when(categoryConverter).entityToDto(category2);
        Mockito.doReturn(categoryList).when(categoryService).findAll();
    }

    @Test
    public void findAllTest() throws Exception {
        mockMvc.
                perform(
                        get("/api/v1/categories")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(2)));
    }
}
