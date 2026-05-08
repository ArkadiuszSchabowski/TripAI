package com.example.tripai_backend;

import dev.langchain4j.model.chat.ChatLanguageModel;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
class TripAiBackendApplicationTests {

    @MockitoBean
    private ChatLanguageModel chatLanguageModel;

    @Test
    void contextLoads() {
    }

}
