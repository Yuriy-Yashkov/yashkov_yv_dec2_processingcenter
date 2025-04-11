package ru.edme.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.edme.service.TableService;

import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class DeleteClearTablesControllerTest {

    private final MockMvc mockMvc;

    @MockBean
    private TableService tableService;

    @Test
    void deleteTables_ShouldReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/tables/delete"))
                .andExpect(status().isOk())
                .andExpect(content().string("Все таблицы удалены."));

        Mockito.verify(tableService, times(1)).deleteTables();
    }

    @Test
    void clearTables_ShouldReturnOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/tables/clear"))
                .andExpect(status().isOk())
                .andExpect(content().string("Все таблицы очищены."));

        Mockito.verify(tableService, times(1)).clearTables();
    }
}
