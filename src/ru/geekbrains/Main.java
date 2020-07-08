package ru.geekbrains;


import java.util.Random;
import java.util.Scanner;

public class Main {

    // variable size if matrix
    public static final int SIZE = 3;

    public static final String WelcomeMessage = "Давайте сыграем в крестики нолики";
    public static final String SelectSimbol = "Выберете каким символом вы будете играть";
    public static final String ErrorSelectSimbol = "Неправильно введены символы. Необходимо ввести 1 или 2";

    public static final char DOT_EMPTY = '•';
    public static final char DOT_PLAYER1 = '+';
    public static final char DOT_PLAYER2 = 'o';

    public static char DOT_HUMAN, DOT_AI;

    public static char[][] map = new char[SIZE][SIZE];;
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println(WelcomeMessage);
        selectPlayerSimbol();
        initializeMap(map, SIZE);
        printMap(map);

        playGame();

    }

    private static void selectPlayerSimbol() {
        System.out.println(SelectSimbol + ":\n1. " + DOT_PLAYER1 + "\n2. " + DOT_PLAYER2);

        int selectPlayer;
        boolean checkCorrectSelectionSimbol = false;
        do {
            try {
                selectPlayer = scanner.nextInt();
                switch (selectPlayer) {
                    case 1:
                        DOT_HUMAN = DOT_PLAYER1;
                        DOT_AI = DOT_PLAYER2;
                        checkCorrectSelectionSimbol = true;
                        break;
                    case 2:
                        DOT_HUMAN = DOT_PLAYER2;
                        DOT_AI = DOT_PLAYER1;
                        checkCorrectSelectionSimbol = true;
                        break;
                    default:
                        System.out.println(ErrorSelectSimbol);
                }
            } catch (NumberFormatException ex) {
                System.out.println(ErrorSelectSimbol);
            }
        } while (!checkCorrectSelectionSimbol);
        //scanner.close();
    }

    private static void initializeMap(char[][] array, int size) {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                array[i][j] = DOT_EMPTY;
    }

    private static void printMap(char[][] map) {
        System.out.printf("%3c", ' ');
        for (int i = 0; i < map.length; i++)
            System.out.printf("%3d", i+1);
        System.out.println();
        for (int i = 0; i < map.length; i++) {
            System.out.printf("%3d", i + 1);
            for (int j = 0; j < map.length; j++)
                System.out.printf("%3c", map[i][j]);
            System.out.println();
        }
    }

    private static void humanTurn() {
        String BaseStr = "Введите номер ";
        System.out.println("Ваш ход.");
        int rowNumber, colNumber;

        do {
            System.out.print(BaseStr + "строки: ");
            rowNumber = scanner.nextInt();
            System.out.print(BaseStr + "колонки: ");
            colNumber = scanner.nextInt();

            //scanner.close();
        } while (!isCellValid(rowNumber, colNumber));
        map[rowNumber - 1][colNumber - 1] = DOT_HUMAN;
    }

    private static boolean isCellValid(int rowNumber, int colNumber) {
        
        if ((rowNumber < 1) || (rowNumber > SIZE) || (colNumber < 1) || (colNumber > SIZE)) {
            System.out.println("Некорректно введены значения строки или столбца. Попробуйте снова");
            return false;
        }
        if (map[rowNumber][colNumber] != DOT_EMPTY) {
            System.out.println("Эта ячейка уже занята. Выберите другую");
            return false;
        }
        return true;
    }

    private static void playGame() {

        do {
            humanTurn();
            printMap(map);
            checkMap(map, DOT_HUMAN);
            aiTurn();
            printMap(map);
            checkMap(map, DOT_AI);
        } while (true);

    }

    private static void aiTurn() {
        System.out.println("Ход компьютера.");
        int rowNumber, colNumber;
        Random random = new Random();
        do {
            rowNumber = random.nextInt(SIZE) + 1;
            colNumber = random.nextInt(SIZE) + 1;
        } while (map[rowNumber - 1][colNumber - 1] != DOT_EMPTY);
        map[rowNumber - 1][colNumber - 1] = DOT_AI;
    }

    private static void checkMap(char[][] map, char simbol) {

        if (checkWin(map, simbol)) {
            if (simbol == DOT_HUMAN) {
                System.out.println("Поздравляем!! Вы победили.");
                System.exit(0);
            } else if (simbol == DOT_AI) {
                System.out.println("Победил компьютер");
                System.exit(0);
            } else {
                throw new IllegalStateException("Unexpected value: " + simbol);
            }

        }
        if (isMapFilled(map)) {
            System.out.println("Ничья");
            System.exit(0);
        }

    }

    private static boolean checkWin(char[][] map, char simbol) {

        //
        return false;
    }

    private static boolean isMapFilled(char[][] map) {

        for (char[] chars : map) {
            for (char aChar : chars) {
                if (aChar == DOT_EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
}
