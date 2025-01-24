package main.java.model;

import lombok.*;

/**
 * Questions class
 *
 * @author sarabarbaraam
 * @version 1.0
 * @since 24/01/2025
 */

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class Questions {

    /**
     * The question
     */

    private String question;

    /**
     * The answer1
     */

    private String answer1;

    /**
     * The answer2
     */

    private String answer2;

    /**
     * The answer3
     */

    private String answer3;

    /**
     * The answer4
     */

    private String answer4;

    /**
     * The rightOption
     */

    private int rightOption;

}
