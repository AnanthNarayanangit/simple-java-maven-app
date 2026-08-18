package com.mycompany.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("GreetingService Tests")
class GreetingServiceTest {

    private GreetingService greetingService;

    @BeforeEach
    void setUp() {
        greetingService = new GreetingService();
    }

    @Nested
    @DisplayName("Standard Greeting Tests")
    class StandardGreetingTests {

        @Test
        @DisplayName("Should greet named user correctly")
        void shouldGreetNamedUser() {
            assertEquals("Hello Alice!", greetingService.getGreeting("Alice"));
        }

        @ParameterizedTest(name = "Input: \"{0}\" -> Expected: \"Hello World!\"")
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        @DisplayName("Should fallback to World when name is null or blank")
        void shouldFallbackToWorldForBlankNames(String input) {
            assertEquals("Hello World!", greetingService.getGreeting(input));
        }

        @Test
        @DisplayName("Should trim whitespace from names")
        void shouldTrimWhitespace() {
            assertEquals("Hello Bob!", greetingService.getGreeting("  Bob  "));
        }
    }

    @Nested
    @DisplayName("Time-Aware Greeting Tests")
    class TimeAwareGreetingTests {

        @ParameterizedTest(name = "Hour {0}: {1}")
        @CsvSource({
                "6, Good morning, Alice!",
                "9, Good morning, Alice!",
                "12, Good afternoon, Alice!",
                "15, Good afternoon, Alice!",
                "18, Good evening, Alice!",
                "20, Good evening, Alice!",
                "22, Good night, Alice!",
                "2, Good night, Alice!"
        })
        @DisplayName("Should return appropriate time-of-day greeting")
        void shouldReturnAppropriateTimeGreeting(int hour, String expectedGreeting) {
            LocalTime time = LocalTime.of(hour, 30);
            assertEquals(expectedGreeting, greetingService.getTimeAwareGreeting("Alice", time));
        }

        @Test
        @DisplayName("Should throw NullPointerException when time is null")
        void shouldThrowExceptionWhenTimeIsNull() {
            assertThrows(NullPointerException.class, () -> greetingService.getTimeAwareGreeting("Alice", null));
        }
    }

    @Nested
    @DisplayName("Multilingual Greeting Tests")
    class MultilingualGreetingTests {

        @ParameterizedTest(name = "Lang: {0} -> Greeting: {1}")
        @CsvSource({
                "ES, ¡Hola Maria!",
                "SPANISH, ¡Hola Maria!",
                "FR, Bonjour Maria!",
                "FRENCH, Bonjour Maria!",
                "DE, Hallo Maria!",
                "GERMAN, Hallo Maria!",
                "IT, Ciao Maria!",
                "ITALIAN, Ciao Maria!",
                "JA, こんにちは, Maria!",
                "EN, Hello Maria!",
                "UNKNOWN, Hello Maria!"
        })
        @DisplayName("Should support multiple languages")
        void shouldSupportMultilingualGreetings(String lang, String expected) {
            assertEquals(expected, greetingService.getPersonalizedMessage("Maria", lang));
        }

        @Test
        @DisplayName("Should handle null language smoothly")
        void shouldHandleNullLanguage() {
            assertEquals("Hello Maria!", greetingService.getPersonalizedMessage("Maria", null));
        }
    }
}
