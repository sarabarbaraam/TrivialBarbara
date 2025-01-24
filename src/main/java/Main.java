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

        int turnNumber = 0;

        if (teamList.stream().allMatch(teams -> teams.getTeamTurn() == 0)) {

            int whoStart = getRandomInt(teamList.size());

            String firstTeam = teamList.get(whoStart).getTeamName();
            System.out.println();
            System.out.println("¡" + firstTeam.toUpperCase() + " van primero!");

            turnNumber++;

            teamList.get(whoStart).setTeamTurn(turnNumber);

            roundsOfQuestions(firstTeam);

        }


    }

    private static void roundsOfQuestions(@NotNull String team) {
        // initialize questions

        System.out.println();
        System.out.println(team.toUpperCase() + ", responde a la pregunta: ");

        Questions question = getQuestions().get(getRandomInt(getQuestions().size()));

        // mostrar preguntas

        System.out.println(question.getQuestion() + "\n\n"
                + "1. " + question.getAnswer1() + "\n"
                + "2. " + question.getAnswer2() + "\n"
                + "3. " + question.getAnswer3() + "\n"
                + "4. " + question.getAnswer4() + "\n");

        // respuesta del usuario

        System.out.print("Respuesta: ");

        String answer = scanner.nextLine();
        esTransformableAEntero(answer);

        int userAnswer = Integer.parseInt(answer);

        if (userAnswer == question.getRightOption()) {

            System.out.println("¡Respuesta correcta!");

            // mostrar clasificacion

        } else {

            System.out.println("¡Has fallado!");
        }


    }

    public static @NotNull ArrayList<Questions> getQuestions() {

        ArrayList<Questions> list = new ArrayList<>();

        File folder = new File("src/main/java/questions");
        if (!folder.exists()) {

            title("Error al cargar el fichero");

        } else {

            File[] filesList = folder.listFiles();

            for (File file : filesList) {

                if (file.isFile() && file.getName().endsWith(".txt")) {

                    var topicName = file.getName().substring(0, file.getName().length() - 4);

                    // TODO create topic


                    // Read the question

                    try (BufferedReader br = new BufferedReader(new FileReader(file))) {

                        String line;
                        ArrayList<String> block = new ArrayList<>();
                        Questions questions = Questions.builder().build();

                        while ((line = br.readLine()) != null) {

                            block.add(line);

                            if (block.size() == 6) { // número de lineas que componen una pregunta

                                var question = block.get(0);
                                var answer1 = block.get(1);
                                var answer2 = block.get(2);
                                var answer3 = block.get(3);
                                var answer4 = block.get(4);
                                var rightOption = Integer.parseInt(block.get(5));

                                questions = Questions.builder()
                                        .question(question)
                                        .answer1(answer1)
                                        .answer2(answer2)
                                        .answer3(answer3)
                                        .answer4(answer4)
                                        .rightOption(rightOption)
                                        .build();

                                block.clear();
                            }
                        }

                        list.add(questions);

                    } catch (IOException e) {

                        e.printStackTrace();
                    }

                }
            }
        }

        return list;
    }

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

    private static int getRandomInt(int max) {

        return new Random().nextInt(max);
    }

    public static boolean esTransformableAEntero(String cadena) {

        try {

            Integer.parseInt(cadena);

            return true;

        } catch (NumberFormatException e) {

            return false;
        }
    }

}