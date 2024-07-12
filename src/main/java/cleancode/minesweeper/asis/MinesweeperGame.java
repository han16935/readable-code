package cleancode.minesweeper.asis;

import java.util.Random;
import java.util.Scanner;

public class MinesweeperGame {

    private static final int BOARD_ROW = 8;
    private static final int BOARD_COL = 10;

    private static final String FLAG_SIGN = "⚑";
    private static final String EMPTY_SIGN = "□";
    private static final String OPEN_SIGN = "■";
    private static final String LANDMINE_SIGN = "☼";

    private static final String[][] BOARD = new String[BOARD_ROW][BOARD_COL];
    private static final Integer[][] LAND_MINE_COUNTS = new Integer[BOARD_ROW][BOARD_COL];
    private static final boolean[][] LAND_MINES = new boolean[BOARD_ROW][BOARD_COL];


    private static final Scanner SCANNER = new Scanner(System.in);
    private static final Random landMineGenerator = new Random();
    private static int gameStatus = 0; // 0: 게임 중, 1: 승리, -1: 패배

    public static void main(String[] args) {
        startToDeclareMineSweeperGame();
        initializeBoard();

        // 지뢰찾기 메인 로직
        while (doesGameNotEnded()) {
            // 지뢰찾기 보드 출력
            printBoard();

            if (AreAllMinesFind()) {
                System.out.println("지뢰를 모두 찾았습니다. GAME CLEAR!");
                break;
            }
            if (IsMineBoomed()) {
                System.out.println("지뢰를 밟았습니다. GAME OVER!");
                break;
            }

            String cellFromUser = getCellFromUser();
            int colIndex = extractColIndexFrom(cellFromUser);
            int rowIndex = extractRowIndexFrom(cellFromUser);

            String actionOnCell = getActionOnCell();
            if (doesUserChooseToFlag(actionOnCell)) {
                BOARD[rowIndex][colIndex] = FLAG_SIGN;

                boolean isOpened = doesAllCellOpened();

                if (isOpened) {
                    winGame();
                }
            }
            if (doesUserChooseToOpen(actionOnCell)) {
                if (isLandmine(rowIndex, colIndex)) {
                    BOARD[rowIndex][colIndex] = LANDMINE_SIGN;
                    loseGame();
                    continue;
                } else {
                    open(rowIndex, colIndex);
                }
                
                // 모든 칸이 체크되엇는지 검사
                boolean open = doesAllCellOpened();
                if (open) {
                    winGame();
                }

            } else {
                System.out.println("잘못된 번호를 선택하셨습니다.");
            }
        }
    }

    private static boolean doesGameNotEnded() {
        return gameStatus == 0;
    }

    private static boolean doesUserChooseToOpen(String actionOnCell) {
        return actionOnCell.equals("1");
    }

    private static void loseGame() {
        gameStatus = -1;
    }

    private static void winGame() {
        gameStatus = 1;
    }

    private static boolean doesAllCellOpened() {
        boolean open = true;
        for (int row = 0; row < BOARD_ROW; row++) {
            for (int col = 0; col < BOARD_COL; col++) {
                if (isEmptyCell(row, col)) {
                    open = false;
                }
            }
        }
        return open;
    }

    private static boolean doesUserChooseToFlag(String actionOnCell) {
        return actionOnCell.equals("2");
    }

    private static boolean IsMineBoomed() {
        return gameStatus == -1;
    }

    private static boolean AreAllMinesFind() {
        return gameStatus == 1;
    }

    private static int extractRowIndexFrom(String cellFromUser) {
        char rowIndex = cellFromUser.charAt(1);
        return Character.getNumericValue(rowIndex) - 1;
    }

    private static String getActionOnCell() {
        System.out.println("선택한 셀에 대한 행위를 선택하세요. (1: 오픈, 2: 깃발 꽂기)");
        String actionOnCell = SCANNER.nextLine();
        return actionOnCell;
    }

    private static String getCellFromUser() {
        System.out.println();
        System.out.println("선택할 좌표를 입력하세요. (예: a1)");
        String input = SCANNER.nextLine();
        return input;
    }

    private static void printBoard() {
        System.out.println("   a b c d e f g h i j");
        for (int i = 0; i < BOARD_ROW; i++) {
            System.out.printf("%d  ", i + 1);
            for (int j = 0; j < BOARD_COL; j++) {
                System.out.print(BOARD[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void initializeBoard() {
        // 지뢰찾기 전체 보드 초기화 (가로 8, 세로 10)
        for (int row = 0; row < BOARD_ROW; row++) {
            for (int col = 0; col < BOARD_COL; col++) {
                BOARD[row][col] = EMPTY_SIGN;
            }
        }

        // 지뢰 10개 랜덤하게 생성
        for (int i = 0; i < BOARD_COL; i++) {
            int row = landMineGenerator.nextInt(BOARD_ROW);
            int col = landMineGenerator.nextInt(BOARD_COL);
            LAND_MINES[row][col] = true;
        }

        // 특정 칸 주위 8칸에 지뢰가 몇 개 있는지 검사 후 landMineCounts에 넣음
        for (int row = 0; row < BOARD_ROW; row++) {
            for (int col = 0; col < BOARD_COL; col++) {
                int count = 0;

                if (isNotLandmine(row, col)) {
                    if (row - 1 >= 0 && col - 1 >= 0 && LAND_MINES[row - 1][col - 1]) {
                        count++;
                    }
                    if (row - 1 >= 0 && isLandmine(row - 1, col)) {
                        count++;
                    }
                    if (row - 1 >= 0 && col + 1 < BOARD_COL && isLandmine(row - 1, col + 1)) {
                        count++;
                    }
                    if (col - 1 >= 0 && isLandmine(row, col - 1)) {
                        count++;
                    }
                    if (col + 1 < BOARD_COL && isLandmine(row, col + 1)) {
                        count++;
                    }
                    if (row + 1 < BOARD_ROW && col - 1 >= 0 && isLandmine(row + 1, col - 1)) {
                        count++;
                    }
                    if (row + 1 < BOARD_ROW && isLandmine(row+1, col)) {
                        count++;
                    }
                    if (row + 1 < BOARD_ROW && col + 1 < BOARD_COL && isLandmine(row + 1, col + 1)) {
                        count++;
                    }
                    LAND_MINE_COUNTS[row][col] = count;
                    continue;
                }
                LAND_MINE_COUNTS[row][col] = 0;
            }
        }
    }

    private static void startToDeclareMineSweeperGame() {
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        System.out.println("지뢰찾기 게임 시작!");
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
    }

    private static int extractColIndexFrom(String cellFromUser){
        char colIndex = cellFromUser.charAt(0);

        // colIndex 값에 따라 가로 index 검사 (ex) a1는 a로 시작하므로 col = 0)
        int col;
        switch (colIndex) {
            case 'a':
                col = 0;
                break;
            case 'b':
                col = 1;
                break;
            case 'c':
                col = 2;
                break;
            case 'd':
                col = 3;
                break;
            case 'e':
                col = 4;
                break;
            case 'f':
                col = 5;
                break;
            case 'g':
                col = 6;
                break;
            case 'h':
                col = 7;
                break;
            case 'i':
                col = BOARD_ROW;
                break;
            case 'j':
                col = 9;
                break;
            default:
                col = -1;
                break;
        }
        return col;
    }

    // 셀 오픈 (셀을 열면서 주변 칸도 동시 오픈)
    private static void open(int row, int col) {
        if (row < 0 || row >= BOARD_ROW || col < 0 || col >= BOARD_COL) {
            return;
        }
        if (isNotEmptyCell(row, col) || isLandmine(row, col)) {
            return;
        }

        if (LAND_MINE_COUNTS[row][col] != 0) {
            BOARD[row][col] = String.valueOf(LAND_MINE_COUNTS[row][col]);
            return;
        } else {
            BOARD[row][col] = OPEN_SIGN;
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

    private static boolean isNotEmptyCell(int row, int col) {
        return !isEmptyCell(row, col);
    }

    private static boolean isEmptyCell(int row, int col) {
        return BOARD[row][col].equals(EMPTY_SIGN);
    }

    private static boolean isLandmine(int row, int col) {
        return LAND_MINES[row][col];
    }
    private static boolean isNotLandmine(int row, int col) {
        return !isLandmine(row, col);
    }

}
