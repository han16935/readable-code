package cleancode.minesweeper.tobe;

import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {

    public static final int BOARD_ROW_SIZE = 8;
    public static final int BOARD_COL_SIZE = 10;
    public static final int LANDMINE_COUNT = 10;
    private static final String[][] board = new String[BOARD_ROW_SIZE][BOARD_COL_SIZE];
    private static final Integer[][] landMineCounts = new Integer[BOARD_ROW_SIZE][BOARD_COL_SIZE];
    private static final boolean[][] landMines = new boolean[BOARD_ROW_SIZE][BOARD_COL_SIZE];
    public static final String FLAG_SIGN = "⚑";
    public static final String BOOM_SIGN = "☼";
    public static final String CLOSED_CELL_SIGN = "□";
    public static final String OPEN_CELL_SIGN = "■";

    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public static void main(String[] args) {
        showGameStartComments();
        Scanner scanner = new Scanner(System.in);
        initializeGame();
        while (true) {
            showBoard();
            if (doesUserWinTheGame()) {
                System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                break;
            }
            if (doesUserLoseTheGame()) {
                System.out.println("지뢰를 밟았습니다. GAME OVER!");
                break;
            }

            String cellInput = getCellInputFromUser(scanner);
            String userActionInput = getUserActionInputFromUser(scanner);

            int selectedColIndex = getSelectedColIndex(cellInput);
            int selectedRowIndex = getSelectedRowIndex(cellInput);
            if (doesUserChooseToPlantFlag(userActionInput)) {
                board[selectedRowIndex][selectedColIndex] = FLAG_SIGN;
                checkIfGameIsOver();
            }
            else if (doesUserChooseToOpenCell(userActionInput)) {
                if (isLandMineCell(selectedColIndex, selectedRowIndex)) {
                    board[selectedRowIndex][selectedColIndex] = BOOM_SIGN;
                    changeGameStatusToLose();
                    continue;
                } else {
                    open(selectedRowIndex, selectedColIndex);
                }
                checkIfGameIsOver();
            } else {
                System.out.println("잘못된 번호를 선택하셨습니다.");
            }
        }
    }

    private static void changeGameStatusToLose() {
        gameStatus = -1;
    }

    private static boolean isLandMineCell(int selectedColIndex, int selectedRowIndex) {
        return landMines[selectedRowIndex][selectedColIndex];
    }

    private static boolean doesUserChooseToOpenCell(String userActionInput) {
        return userActionInput.equals("1");
    }

    private static boolean doesUserChooseToPlantFlag(String userActionInput) {
        return userActionInput.equals("2");
    }

    private static int getSelectedRowIndex(String cellInput) {
        char cellInputRow = cellInput.charAt(1);
        int selectedRowIndex = convertRowFrom(cellInputRow);
        return selectedRowIndex;
    }

    private static String getUserActionInputFromUser(Scanner scanner) {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
        String userActionInput = scanner.nextLine();
        return userActionInput;
    }

    private static String getCellInputFromUser(Scanner scanner) {
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
        String cellInput = scanner.nextLine();
        return cellInput;
    }

    private static boolean doesUserLoseTheGame() {
        return gameStatus == -1;
    }

    private static boolean doesUserWinTheGame() {
        return gameStatus == 1;
    }

    private static void checkIfGameIsOver() {
        boolean isAllOpened = isAllCellOpened();
        if (isAllOpened) {
            changeGameStatusToWin();
        }
    }

    private static void changeGameStatusToWin() {
        gameStatus = 1;
    }

    private static boolean isAllCellOpened() {
        boolean isAllOpened = true;
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                if (board[row][col].equals(CLOSED_CELL_SIGN)) {
                    isAllOpened = false;
                }
            }
        }
        return isAllOpened;
    }

    private static int convertRowFrom(char cellInputRow) {
        return Character.getNumericValue(cellInputRow) - 1;
    }

    private static int getSelectedColIndex(String cellInput) {
        char cellInputCol = cellInput.charAt(0);
        switch (cellInputCol) {
            case 'a':
                return 0;
            case 'b':
                return 1;
            case 'c':
                return 2;
            case 'd':
                return 3;
            case 'e':
                return 4;
            case 'f':
                return 5;
            case 'g':
                return 6;
            case 'h':
                return 7;
            case 'i':
                return 8;
            case 'j':
                return 9;
            default:
                return -1;
        }
    }

    private static void showBoard() {
        System.out.println("   a b c d e f g h i j");
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            System.out.printf("%d  ", row + 1);
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                System.out.print(board[row][col] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void initializeGame() {
        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
             for (int col = 0; col < BOARD_COL_SIZE; col++) {
                 board[row][col] = CLOSED_CELL_SIGN;
             }
         }

        // 지뢰 세팅
        for (int i = 0; i < LANDMINE_COUNT; i++) {
            int col = new Random().nextInt(BOARD_COL_SIZE);
            int row = new Random().nextInt(BOARD_ROW_SIZE);
            landMines[row][col] = true;
        }

        for (int row = 0; row < BOARD_ROW_SIZE; row++) {
            for (int col = 0; col < BOARD_COL_SIZE; col++) {
                int count = 0;

                // 지뢰가 아닐 경우
                if (!isLandMineCell(col, row)) {
                    // 왼쪽 대각선 칸의 landMines가 true일 경우
                    if (row - 1 >= 0 && col - 1 >= 0 && isLandMineCell(col - 1, row - 1)) {
                        count++;
                    }

                    //
                    if (row - 1 >= 0 && isLandMineCell(col, row - 1)) {
                        count++;
                    }
                    if (row - 1 >= 0 && col + 1 < BOARD_COL_SIZE && isLandMineCell(col + 1, row - 1)) {
                        count++;
                    }
                    if (col - 1 >= 0 && isLandMineCell(col - 1, row)) {
                        count++;
                    }
                    if (col + 1 < BOARD_COL_SIZE && isLandMineCell(col + 1, row)) {
                        count++;
                    }
                    if (row + 1 < BOARD_ROW_SIZE && col - 1 >= 0 && isLandMineCell(col - 1, row + 1)) {
                        count++;
                    }
                    if (row + 1 < BOARD_ROW_SIZE && isLandMineCell(col, row + 1)) {
                        count++;
                    }
                    if (row + 1 < BOARD_ROW_SIZE && col + 1 < BOARD_COL_SIZE && isLandMineCell(col + 1, row + 1)) {
                        count++;
                    }

                    // 현재 칸의 주변에 지뢰가 몇 개 잇는지
                    landMineCounts[row][col] = count;
                    continue;
                }

                // 지뢰인 경우
                landMineCounts[row][col] = 0;
            }
        }
    }

    private static void showGameStartComments() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private static void open(int row, int col) {
        if (row < 0 || row >= BOARD_ROW_SIZE || col < 0 || col >= BOARD_COL_SIZE) {
            return;
        }
        if (!board[row][col].equals(CLOSED_CELL_SIGN)) {
            return;
        }
        if (isLandMineCell(col, row)) {
            return;
        }
        if (landMineCounts[row][col] != 0) {
            board[row][col] = String.valueOf(landMineCounts[row][col]);
            return;
        } else {
            board[row][col] = OPEN_CELL_SIGN;
        }
        open(row - 1, col - 1);
        open(row - 1, col);
        open(row - 1, col + 1);
        open(row, col - 1);
        open(row, col + 1);
        open(row + 1, col - 1);
        open(row + 1, col);
        open(row + 1, col + 1);
    }

}
