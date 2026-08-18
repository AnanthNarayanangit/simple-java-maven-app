package com.mycompany.app.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CalculatorService Tests")
class CalculatorServiceTest {

    private CalculatorService calculatorService;

    @BeforeEach
    void setUp() {
        calculatorService = new CalculatorService();
    }

    @Nested
    @DisplayName("Basic Arithmetic Tests")
    class BasicArithmeticTests {

        @Test
        @DisplayName("Should correctly add numbers")
        void shouldAddNumbers() {
            assertEquals(15.0, calculatorService.add(10.0, 5.0));
            assertEquals(-2.0, calculatorService.add(-5.0, 3.0));
        }

        @Test
        @DisplayName("Should correctly subtract numbers")
        void shouldSubtractNumbers() {
            assertEquals(5.0, calculatorService.subtract(10.0, 5.0));
            assertEquals(-8.0, calculatorService.subtract(-5.0, 3.0));
        }

        @Test
        @DisplayName("Should correctly multiply numbers")
        void shouldMultiplyNumbers() {
            assertEquals(50.0, calculatorService.multiply(10.0, 5.0));
            assertEquals(0.0, calculatorService.multiply(10.0, 0.0));
        }

        @Test
        @DisplayName("Should correctly divide numbers")
        void shouldDivideNumbers() {
            assertEquals(2.0, calculatorService.divide(10.0, 5.0));
        }

        @Test
        @DisplayName("Should throw ArithmeticException when dividing by zero")
        void shouldThrowOnDivideByZero() {
            Exception ex = assertThrows(ArithmeticException.class, () -> calculatorService.divide(10.0, 0.0));
            assertTrue(ex.getMessage().contains("Division by zero"));
        }

        @Test
        @DisplayName("Should compute power correctly")
        void shouldComputePower() {
            assertEquals(8.0, calculatorService.power(2.0, 3.0));
            assertEquals(1.0, calculatorService.power(5.0, 0.0));
        }

        @ParameterizedTest(name = "Factorial of {0} is {1}")
        @CsvSource({
                "0, 1",
                "1, 1",
                "3, 6",
                "5, 120",
                "10, 3628800"
        })
        @DisplayName("Should calculate factorial correctly")
        void shouldCalculateFactorial(int n, long expected) {
            assertEquals(expected, calculatorService.factorial(n));
        }

        @Test
        @DisplayName("Should throw exception for negative factorial")
        void shouldThrowForNegativeFactorial() {
            assertThrows(IllegalArgumentException.class, () -> calculatorService.factorial(-1));
        }

        @Test
        @DisplayName("Should throw exception for factorial overflow > 20")
        void shouldThrowForFactorialOverflow() {
            assertThrows(ArithmeticException.class, () -> calculatorService.factorial(21));
        }
    }

    @Nested
    @DisplayName("Expression Evaluation Tests")
    class ExpressionEvaluationTests {

        @ParameterizedTest(name = "Expression: \"{0}\" -> Result: {1}")
        @CsvSource({
                "'10 + 20', 30.0",
                "'100 - 45', 55.0",
                "'7 * 8', 56.0",
                "'81 / 9', 9.0",
                "'2 ^ 10', 1024.0",
                "'42', 42.0"
        })
        @DisplayName("Should evaluate valid simple expressions")
        void shouldEvaluateValidExpressions(String expr, double expected) {
            assertEquals(expected, calculatorService.evaluateSimpleExpression(expr), 0.0001);
        }

        @ParameterizedTest
        @ValueSource(strings = {"", "   "})
        @DisplayName("Should throw exception on blank expressions")
        void shouldThrowOnBlankExpression(String expr) {
            assertThrows(IllegalArgumentException.class, () -> calculatorService.evaluateSimpleExpression(expr));
        }

        @Test
        @DisplayName("Should throw exception on null expression")
        void shouldThrowOnNullExpression() {
            assertThrows(IllegalArgumentException.class, () -> calculatorService.evaluateSimpleExpression(null));
        }
    }

    @Nested
    @DisplayName("Statistics Tests")
    class StatisticsTests {

        @Test
        @DisplayName("Should calculate correct stats for odd-sized list")
        void shouldCalculateStatsOdd() {
            List<Double> data = List.of(1.0, 3.0, 5.0, 7.0, 9.0);
            CalculatorService.StatsResult stats = calculatorService.calculateStats(data);

            assertEquals(5, stats.count());
            assertEquals(25.0, stats.sum());
            assertEquals(1.0, stats.min());
            assertEquals(9.0, stats.max());
            assertEquals(5.0, stats.average());
            assertEquals(5.0, stats.median());
        }

        @Test
        @DisplayName("Should calculate correct stats for even-sized list")
        void shouldCalculateStatsEven() {
            List<Double> data = List.of(10.0, 20.0, 30.0, 40.0);
            CalculatorService.StatsResult stats = calculatorService.calculateStats(data);

            assertEquals(4, stats.count());
            assertEquals(100.0, stats.sum());
            assertEquals(10.0, stats.min());
            assertEquals(40.0, stats.max());
            assertEquals(25.0, stats.average());
            assertEquals(25.0, stats.median());
        }

        @Test
        @DisplayName("Should throw exception for empty list")
        void shouldThrowForEmptyList() {
            assertThrows(IllegalArgumentException.class, () -> calculatorService.calculateStats(List.of()));
            assertThrows(IllegalArgumentException.class, () -> calculatorService.calculateStats(null));
        }
    }
}
