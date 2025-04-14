package ru.edme.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.edme.model.Card;
import ru.edme.service.CardAllService;
import ru.edme.util.TestData;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RequiredArgsConstructor
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@WithMockUser(username = "yriy", password = "123", authorities = "ADMIN")
class CardControllerTest {

    private final ObjectMapper objectMapper;
    private final MockMvc mockMvc;

    @MockBean
    private CardAllService cardAllService;

    TestData testData = new TestData();

    @Test
    void create() throws Exception {
        Card card = testData.cardId;
        String jsonRequest = objectMapper.writeValueAsString(card);

        Mockito.when(cardAllService.save(any(Card.class))).thenReturn(card);

        mockMvc.perform(MockMvcRequestBuilders.post("/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(card.getId()))
                .andExpect(jsonPath("$.cardNumber").value(card.getCardNumber()));

        Mockito.verify(cardAllService, times(1)).save(any(Card.class));
    }

    @Test
    void findById() throws Exception {
        Card card = testData.cardId;
        Long id = card.getId();

        Mockito.when(cardAllService.findById(id)).thenReturn(card);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(card.getId()))
                .andExpect(jsonPath("$.cardNumber").value(card.getCardNumber()));

        Mockito.verify(cardAllService, times(1)).findById(id);
    }

    @Test
    void findAll() throws Exception {
        List<Card> cards = List.of(testData.cardId, testData.card);

        Mockito.when(cardAllService.findAll()).thenReturn(cards);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(cards.size()));

        Mockito.verify(cardAllService, times(1)).findAll();
    }

    @Test
    void update() throws Exception {
        Card updatedCard = testData.cardId;
        String jsonRequest = objectMapper.writeValueAsString(updatedCard);

        Mockito.when(cardAllService.update(any(Card.class))).thenReturn(updatedCard);

        mockMvc.perform(MockMvcRequestBuilders.put("/v1/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isUpgradeRequired())
                .andExpect(jsonPath("$.id").value(updatedCard.getId()))
                .andExpect(jsonPath("$.cardNumber").value(updatedCard.getCardNumber()));

        Mockito.verify(cardAllService, times(1)).update(any(Card.class));
    }

    @Test
    void deleteShouldReturnOk() throws Exception {
        Long id = testData.cardId.getId();

        Mockito.when(cardAllService.delete(id)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/cards/{id}", id))
                .andExpect(status().isOk());

        Mockito.verify(cardAllService, times(1)).delete(id);
    }

    @Test
    void deleteShouldReturnNotFound() throws Exception {
        Long id = testData.cardId.getId();

        Mockito.when(cardAllService.delete(id)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/v1/cards/{id}", id))
                .andExpect(status().isNotFound());

        Mockito.verify(cardAllService, times(1)).delete(id);
    }
}
