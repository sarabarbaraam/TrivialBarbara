package main.java;

import main.java.model.Questions;
import main.java.model.Teams;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        // initialize questions

        getQuestions();

        // initialize teams

        ArrayList<Teams> teamList = new ArrayList<>();

        System.out.println("Pulse la letra 'q' para comenzar el juego");
        do {

            System.out.print("¿Cómo se llama el equipo?: ");

            String teamName = scanner.nextLine();

            if ("q".equals(teamName)) {

                break;
            }

            Teams team = Teams.builder().teamName(teamName).build();

            teamList.add(team);

        } while (true);

        turns(teamList);
    }

    /**
     * Cambia el turno entre equipos de manera aleatoria descartando a los que ya han sido preguntados
     *
     * @param teamList la lista de equipos
     */

    private static void turns(@NotNull ArrayList<Teams> teamList) {

        while (!isWinner(teamList)) {

            for (Teams team : teamList) {

                roundsOfQuestions(team, team.getQuesitos());

                if (isWinner(teamList)) {

                    title("FIN DEL JUEGO. " + team.getTeamName().toUpperCase() + " GANA");
                    ranking(teamList);
                    return;
                }
            }

            // ranking despues de que todos los equipos hayan respondido
            ranking(teamList);
        }

    }

    /**
     * Preguntas para cada ronda
     *
     * @param team     el equipo al que le toca jugar
     * @param quesitos quesitos del equipo
     */

    private static void roundsOfQuestions(@NotNull Teams team, int quesitos) {

        System.out.println();
        System.out.println(team.getTeamName().toUpperCase() + ", responde a la pregunta: ");

        ArrayList<Questions> questionList = getQuestions();
        ArrayList<Questions> filteredQuestions = new ArrayList<>(); // lista temporal para filtrar preguntas

        // Filtrar preguntas por categorías no ganadas
        for (Questions question : questionList) {

            if (!team.hasWonCategory(question.getCategory())) {

                filteredQuestions.add(question);
            }
        }

        Questions randomQuestion = filteredQuestions.get(getRandomInt(filteredQuestions.size()));

        System.out.println(randomQuestion.getQuestion() + "\n\n"
                + "1. " + randomQuestion.getAnswer1() + "\n"
                + "2. " + randomQuestion.getAnswer2() + "\n"
                + "3. " + randomQuestion.getAnswer3() + "\n"
                + "4. " + randomQuestion.getAnswer4() + "\n");

        System.out.print("Respuesta: ");
        String answer = scanner.nextLine();

        while (!esTransformableAEntero(answer)) {

            System.out.println("Por favor, introduce un número válido.");
            System.out.print("Respuesta: ");
            answer = scanner.nextLine();

        }

        int userAnswer = Integer.parseInt(answer);

        if (userAnswer == randomQuestion.getRightOption()) {

            System.out.println("¡Respuesta correcta!");
            quesitos++;
            team.setQuesitos(quesitos);

            // marcar categoría como ganada
            team.markCategoryWon(randomQuestion.getCategory());

            System.out.println(team.getTeamName().toUpperCase() + " obtiene 1 quesito");
            System.out.println();

        } else {

            System.out.println("¡Has fallado!");
            System.out.println();
        }

    }

    /**
     * devuelve si hay un ganador o no
     *
     * @param teamList lista de los equipos participantes
     *
     * @return true o false
     */

    private static boolean isWinner(@NotNull ArrayList<Teams> teamList) {

        for (Teams teams : teamList) {

            if (teams.getQuesitos() == 5) {

                return true;
            }
        }

        return false;
    }

    /**
     * Ranking de los equipos
     *
     * @param teamList lista de los equipos que juegan
     */
    private static void ranking(@NotNull ArrayList<Teams> teamList) {
        title("RANKING");
        for (Teams teamScore : teamList) {

            System.out.println(teamScore.getTeamName().toUpperCase() + ": " + teamScore.getQuesitos() + " "
                    + "quesitos");
        }
    }

    /**
     * Coge las preguntas del fichero de preguntas
     *
     * @return lista de preguntas
     */

    public static @NotNull ArrayList<Questions> getQuestions() {

        ArrayList<Questions> list = new ArrayList<>();

        // ruta donde están los archivos de preguntas
        File folder = new File("src/main/java/questions");

        if (!folder.exists()) {

            title("Error al cargar el fichero");

        } else {

            File[] filesList = folder.listFiles();

            for (File file : filesList) {

                if (file.isFile() && file.getName().endsWith(".txt")) {

                    // del archivo como categoría (sin extensión .txt)
                    var topicName = file.getName().substring(0, file.getName().length() - 4);

                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {

                        String line;
                        ArrayList<String> block = new ArrayList<>();

                        while ((line = br.readLine()) != null) {

                            block.add(line);

                            // Cada bloque de 6 líneas es una pregunta

                            if (block.size() == 6) {

                                // Crear pregunta con sus datos

                                var question = block.get(0);
                                var answer1 = block.get(1);
                                var answer2 = block.get(2);
                                var answer3 = block.get(3);
                                var answer4 = block.get(4);
                                var rightOption = Integer.parseInt(block.get(5));

                                // Crear objeto Questions

                                Questions questions = Questions.builder()
                                        .question(question)
                                        .category(topicName)
                                        .answer1(answer1)
                                        .answer2(answer2)
                                        .answer3(answer3)
                                        .answer4(answer4)
                                        .rightOption(rightOption)
                                        .build();

                                list.add(questions);

                                block.clear();
                            }
                        }

                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }
            }
        }

        return list;
    }

    /**
     * Pone el título bonito
     *
     * @param text el texto que va dentro de los #
     */

    public static void title(@NotNull String text) {

        int length = text.length();
        printHashtagLine(length + 4); // Bordes

        System.out.println("# " + text + " #");

        printHashtagLine(length + 4);
    }

    public static void printHashtagLine(int length) {

        for (int i = 0; i < length; i++) {

            System.out.print("#");
        }

        System.out.println();
    }

    /**
     * Coge un número aletorio
     *
     * @param max el tamaño máximo de la lista
     *
     * @return un número aleatorio
     */

    private static int getRandomInt(int max) {

        return new Random().nextInt(max);
    }

    /**
     * Comprueba que la cadena que pasa el usuario se puede transformar a numero entero
     *
     * @param cadena la cadena pasada
     *
     * @return true o false
     */

    public static boolean esTransformableAEntero(String cadena) {

        try {

            Integer.parseInt(cadena);

            return true;

        } catch (NumberFormatException e) {

            return false;
        }
    }

}