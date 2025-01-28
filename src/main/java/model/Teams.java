package main.java.model;

import lombok.*;

import java.util.HashMap;

/**
 * Teams class
 *
 * @author sarabarbaraam
 * @version 1.0
 * @since 23/01/2025
 */

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class Teams {

    /**
     * The teamName
     */

    private String teamName;

    /**
     * The teamTurn
     */

    private int teamTurn;

    /**
     * The quesitos
     */

    private int quesitos;

    /**
     * The categoriesWon
     */

    @Builder.Default
    private HashMap<String, Boolean> categoriesWon = new HashMap<>();

    public void markCategoryWon(String category) {
        this.categoriesWon.put(category, true);
    }

    public boolean hasWonCategory(String category) {
        return this.categoriesWon.getOrDefault(category, false);
    }

}
