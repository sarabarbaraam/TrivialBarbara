package main.java.model;

import lombok.*;

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

}
