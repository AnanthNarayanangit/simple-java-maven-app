package com.mycompany.app.service;

import java.time.LocalTime;
import java.util.Objects;

/**
 * Service for generating localized and time-aware greeting messages.
 */
public class GreetingService {

    /**
     * Returns a standard greeting for the given name.
     *
     * @param name target recipient name
     * @return formatted greeting message
     */
    public String getGreeting(String name) {
        String safeName = (name == null || name.isBlank()) ? "World" : name.trim();
        return "Hello " + safeName + "!";
    }

    /**
     * Returns a time-of-day greeting (Morning, Afternoon, Evening, Night).
     *
     * @param name target recipient name
     * @param time local time reference
     * @return formatted time-aware greeting
     */
    public String getTimeAwareGreeting(String name, LocalTime time) {
        String safeName = (name == null || name.isBlank()) ? "World" : name.trim();
        Objects.requireNonNull(time, "Time must not be null");

        int hour = time.getHour();
        String timeGreeting = switch (hour) {
            case 5, 6, 7, 8, 9, 10, 11 -> "Good morning";
            case 12, 13, 14, 15, 16 -> "Good afternoon";
            case 17, 18, 19, 20 -> "Good evening";
            default -> "Good night";
        };

        return String.format("%s, %s!", timeGreeting, safeName);
    }

    /**
     * Returns a multilingual greeting.
     *
     * @param name target recipient name
     * @param language ISO language code or name (e.g. ES, FR, DE, IT, JA)
     * @return localized greeting
     */
    public String getPersonalizedMessage(String name, String language) {
        String safeName = (name == null || name.isBlank()) ? "World" : name.trim();
        String lang = (language == null) ? "EN" : language.trim().toUpperCase();

        return switch (lang) {
            case "ES", "SPANISH" -> "¡Hola " + safeName + "!";
            case "FR", "FRENCH" -> "Bonjour " + safeName + "!";
            case "DE", "GERMAN" -> "Hallo " + safeName + "!";
            case "IT", "ITALIAN" -> "Ciao " + safeName + "!";
            case "JA", "JAPANESE" -> "こんにちは, " + safeName + "!";
            default -> "Hello " + safeName + "!";
        };
    }
}
