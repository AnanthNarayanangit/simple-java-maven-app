package com.mycompany.app.service;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.List;

/**
 * Service providing mathematical operations, expression evaluation, and statistical summaries.
 */
public class CalculatorService {

    public double add(double a, double b) {
        return a + b;
    }

    public double subtract(double a, double b) {
        return a - b;
    }

    public double multiply(double a, double b) {
        return a * b;
    }

    public double divide(double a, double b) {
        if (b == 0.0) {
            throw new ArithmeticException("Division by zero is not allowed.");
        }
        return a / b;
    }

    public double power(double base, double exponent) {
        return Math.pow(base, exponent);
    }

    public long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Factorial is not defined for negative numbers: " + n);
        }
        if (n > 20) {
            throw new ArithmeticException("Factorial overflow for n > 20.");
        }
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    /**
     * Evaluates simple binary expressions like "10 + 20", "5 * 4", "100 / 2", "2 ^ 8".
     *
     * @param expr String mathematical expression
     * @return calculated result
     */
    public double evaluateSimpleExpression(String expr) {
        if (expr == null || expr.isBlank()) {
            throw new IllegalArgumentException("Expression cannot be null or empty.");
        }

        String cleaned = expr.replaceAll("\\s+", "");

        char op = ' ';
        int opIndex = -1;
        for (int i = 0; i < cleaned.length(); i++) {
            char c = cleaned.charAt(i);
            if ((c == '+' || c == '-' || c == '*' || c == '/' || c == '^') && i > 0) {
                op = c;
                opIndex = i;
                break;
            }
        }

        if (opIndex == -1) {
            return Double.parseDouble(cleaned);
        }

        double left = Double.parseDouble(cleaned.substring(0, opIndex));
        double right = Double.parseDouble(cleaned.substring(opIndex + 1));

        return switch (op) {
            case '+' -> add(left, right);
            case '-' -> subtract(left, right);
            case '*' -> multiply(left, right);
            case '/' -> divide(left, right);
            case '^' -> power(left, right);
            default -> throw new IllegalArgumentException("Unsupported operator: " + op);
        };
    }

    public record StatsResult(
            long count,
            double sum,
            double min,
            double max,
            double average,
            double median
    ) {}

    /**
     * Computes statistical metrics (count, sum, min, max, average, median) on a list of numbers.
     *
     * @param numbers non-empty list of double values
     * @return StatsResult record
     */
    public StatsResult calculateStats(List<Double> numbers) {
        if (numbers == null || numbers.isEmpty()) {
            throw new IllegalArgumentException("Numbers list must not be empty.");
        }

        DoubleSummaryStatistics stats = numbers.stream()
                .mapToDouble(Double::doubleValue)
                .summaryStatistics();

        double[] sorted = numbers.stream().mapToDouble(Double::doubleValue).sorted().toArray();
        double median;
        int n = sorted.length;
        if (n % 2 == 1) {
            median = sorted[n / 2];
        } else {
            median = (sorted[(n / 2) - 1] + sorted[n / 2]) / 2.0;
        }

        return new StatsResult(stats.getCount(), stats.getSum(), stats.getMin(), stats.getMax(), stats.getAverage(), median);
    }
}
