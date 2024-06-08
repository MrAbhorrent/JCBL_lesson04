package ru.geekbrains;


import java.util.Random;
import java.util.Scanner;

public class Main {

    // variable size if matrix
    public static final int SIZE = 3;

    public static final String welcomeMessage = "Давайте сыграем в крестики нолики";
    public static final String selectSymbol = "Выберете каким символом вы будете играть";
    public static final String errorSelectSymbol = "Неправильно введены символы. Необходимо ввести 1 или 2";

    public static final char DOT_EMPTY = '•';
    public static final char DOT_PLAYER1 = '+';
    public static final char DOT_PLAYER2 = 'o';

    public static char DOT_HUMAN, DOT_AI;

    public static char[][] map = new char[SIZE][SIZE];

    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println(welcomeMessage);
        selectPlayerSymbol();
        initializeMap(map, SIZE);
        printMap(map);
        playGame();
    }

    private static void selectPlayerSymbol() {
        System.out.println(selectSymbol + ":\n1. " + DOT_PLAYER1 + "\n2. " + DOT_PLAYER2);

        int selectPlayer;
        boolean checkCorrectSelectionSymbol = false;
        do {
            try {
                selectPlayer = scanner.nextInt();
                switch (selectPlayer) {
                    case 1:
                        DOT_HUMAN = DOT_PLAYER1;
                        DOT_AI = DOT_PLAYER2;
                        checkCorrectSelectionSymbol = true;
                        break;
                    case 2:
                        DOT_HUMAN = DOT_PLAYER2;
                        DOT_AI = DOT_PLAYER1;
                        checkCorrectSelectionSymbol = true;
                        break;
                    default:
                        System.out.println(errorSelectSymbol);
                }
            } catch (NumberFormatException ex) {
                System.out.println(errorSelectSymbol);
            }
        } while (!checkCorrectSelectionSymbol);
        //scanner.close();
    }

    private static void initializeMap(char[][] array, int size) {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                array[i][j] = DOT_EMPTY;
    }

    private static void printMap(char[][] map) {
        System.out.printf("%3c", ' ');
        for (int i = 0; i < map.length; i++) {
            System.out.printf("%3d", i + 1);
        }
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
        boolean flagCritical = false;
        int criticalRow = -1;
        int criticalColumn = -1;
        System.out.println("Ход компьютера.");
        int rowNumber, colNumber;
        Random random = new Random();
        do {
            // Компьютер выполняет проверку символов в строке, если количество символов меньше размера поля на один, что
            // говорит о том что следующим ходом человек победит фиксирует эту строку и ставит в нее свой символ
            for (int i = 0; i < SIZE; i++) {
                if (checkRow(i, DOT_HUMAN) == SIZE - 1) {
                    flagCritical = true;
                    criticalRow = i;
                    break;
                }
            }
            if (flagCritical) {
                rowNumber = criticalRow;
            } else {
                rowNumber = random.nextInt(SIZE) + 1;
            }

            // Компьютер выполняет проверку символов в колонке, если количество символов меньше размера поля на один, что
            // говорит о том что следующим ходом человек победит фиксирует эту колонку и ставит в нее свой символ на свободное место
            for (int i = 0; i < SIZE; i++) {
                if (checkColumn(i, DOT_HUMAN) == SIZE - 1) {
                    flagCritical = true;
                    criticalColumn = i;
                    break;
                }
            }
            if (flagCritical) {
                colNumber = criticalColumn;
            } else {
                colNumber = random.nextInt(SIZE) + 1;
            }

            // Аналогичным образом надо проверить диагонали

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

        boolean flagWin = false;
        // Сравниваем значение символов в колонках и строках и на диагоналях с размером матрицы
        for (int i = 0; i < SIZE; i++) {
            if (SIZE == checkRow(i, simbol)) flagWin = true;
        }
        for (int j = 0; j < SIZE; j++) {
            if (SIZE == checkColumn(j, simbol)) flagWin = true;
        }
        if (SIZE == checkMainDiagonal(simbol) && SIZE == checkBackDiagonal(simbol)) flagWin = true;

        return flagWin;
    }

    private static int checkColumn(int columnNumber, char simbol) {
        int count = 0;
        char vremChar;
        for (int i = 0; i < SIZE; i++) {
            vremChar = map[i][columnNumber];
            if (simbol == vremChar) count++;
        }
        return count;
    }

    //
    private static int checkRow(int rowNumber, char simbol) {
        int count = 0;
        char vremChar;
        for (int i = 0; i < SIZE; i++) {
            vremChar = map[rowNumber][i];
            if (simbol == vremChar) count++;
        }
        return count;
    }

    private static int checkMainDiagonal(char simbol) {
        int count = 0;
        char vremChar;
        for (int i = 0; i < SIZE; i++) {
            vremChar = map[i][i];
            if (simbol == vremChar) count++;
        }
        return count;
    }

    private static int checkBackDiagonal(char simbol) {
        int count = 0;
        char vremChar;
        for (int i = 0; i < SIZE; i++) {
            vremChar = map[i][SIZE - i - 1];
            if (simbol == vremChar) count++;
        }
        return count;
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
