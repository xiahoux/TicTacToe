package ru;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Main {

    public static Scanner scanner = new Scanner(System.in);
    public static Random random = new Random();

    public static final char DOT_EMPTY = '•';
    public static final char DOT_HUMAN = 'X';
    public static final char DOT_AI = 'Ѳ';
    public static final String HEADER_DOT = "♠";
    public static final String EMPTY = "  ";
    public static int rowNumber;
    public static int columnNumber;
    public static int size = 1;
    public static char [][] map;


    public static void main(String[] args) {
        turnGame();
    }

    public static void turnGame(){
        initMap();
        printMap();
        playGame();
        // *



    }


    public static void initMap() {
        int size = sizeMap();
        map = new char[size][size];
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                map[i][j] = DOT_EMPTY;
            }
        }


    }

    public static int sizeMap(){
        boolean validInput = false;
        do{
            System.out.println("Введите желаемый размер игрового поля: ");
            if(scanner.hasNextInt()) {
                size = scanner.nextInt();
                validInput = true;
            }else{
                System.out.println("Вы ввели некорректное значение.");
                scanner.nextLine();
            }
        }while (!validInput);
        return size;

    }

    public static void printMap() {
        printMapHeader();

        printMapRow();
    }

    private static void printMapHeader() {
        System.out.print(HEADER_DOT + EMPTY);
        for(int i = 0; i < size; i++){
            System.out.print(i + 1 + EMPTY);
        }
        System.out.println();
    }

    public static void printMapRow() {
        for(int i = 0; i < size; i++){
            System.out.print(i + 1 + EMPTY);
            for(int j = 0; j < size; j++){
                System.out.print(map[i][j] + EMPTY);
            }
            System.out.println();
        }
    }

    public static void playGame() {
        // * ход человека
        System.out.println("Ход Человека.");
        while(true){
            humanMove();
            printMap();
            if(isVictory(DOT_HUMAN)){
                System.out.println("Плоть сильна! Мы победили!");
                System.exit(0);
            }
            if(isDraw()){
                System.out.println("Ничья!");
                System.exit(0);
            }

            // * ход компьютера
            System.out.println("Ход Компьютера.");
            map[2][0] = DOT_AI;
            map[1][1] = DOT_AI;
            map[1][2] = DOT_AI;
            //printMap();
            if(aiWinningMove(DOT_AI)) System.out.println("ыыыыы");
            else aiMove();
            // * печать поля
            printMap();
            // * проверка победы компьютера
            if(isVictory(DOT_AI)){
                printMap();
                System.out.println("Плоть слаба! Машины победили!");
                System.exit(0);
            }
            if(isDraw()){
                printMap();
                System.out.println("Ничья");
                System.exit(0);
            }

        }


    }



    private static void humanMove() {
        do{
            System.out.print("Строка = ");
            rowNumber = scanner.nextInt();
            System.out.println();

            System.out.print("Столбик = ");
            columnNumber = scanner.nextInt();
            System.out.println();
        }while(!isCellValid(rowNumber, columnNumber));

        map[rowNumber-1][columnNumber-1] = DOT_HUMAN;


    }

    private static boolean isCellValid(int row, int column) {
        if(row < 1 || row > size || column < 1 || column > size) {
            System.out.println("Введите корректные значения");
            return false;
        }
        if(map[row-1][column-1] != DOT_EMPTY){
            System.out.println("Эта клетка уже занята.");
            return false;
        }
        return true;
    }

    private static boolean isVictory(char dot) {
        // * проверка победы
        int counterRow = 0;
        int counterColumn = 0;
        int counterDiagonal = 0;
        int counterAuxDiagonal = 0;
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++) {
                if(map[i][j] == dot) counterRow++;
                else counterRow = 0;
                if(counterRow == 3) return true;

                if(map[j][i] == dot) counterColumn++;
                else counterColumn = 0;
                if(counterColumn == 3) return true;


                if (map[j][j] == dot) counterDiagonal++;
                else counterDiagonal = 0;

                if(counterDiagonal == 3) return true;

                if( (i + j) == (size - 1) ){
                    if(map[i][j] == dot) counterAuxDiagonal++;
                    else counterAuxDiagonal = 0;
                }
                if(counterAuxDiagonal == 3) return true;
            }
            counterRow = 0;
            counterColumn = 0;
        }
        return false;

    }
    public static boolean isDraw(){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(map[i][j] == DOT_EMPTY) return false;
            }
        }
        return true;
    }

    public static void aiMove(){
        do{
            rowNumber = random.nextInt(3) + 1;
            columnNumber = random.nextInt(3) + 1;
        }
        while(!isCellValid(rowNumber, columnNumber));

        map[rowNumber - 1][columnNumber - 1] = DOT_AI;

    }

    public static boolean aiWinningMove(char dot){
        int counterRow = 0;
        int counterColumn = 0;
        int counterDiagonal = 0;
        int counterAuxDiagonal = 0;

        if(aiWinningMoveRow(dot, counterRow)) return true;
        else if(aiWinningMoveColumn(dot, counterColumn)) return true;
        else if(aiWinningMoveDiagonal(dot, counterDiagonal)) return true;
        else if(aiWinningMoveAuxDiagonal(dot, counterAuxDiagonal)) return true;
        else return false;
    }



    private static boolean aiWinningMoveRow(char dot, int counterRow) {
        int columnNumber = 0;
        int rowNumber = 0;
        int flagEmpty = 0;
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(map[i][j] == dot) counterRow++;
                else if(map[i][j] == DOT_EMPTY && counterRow == 2){
                    map[i][j] = dot;
                    System.out.println("DOT_EMPTY && counterRow == 2");
                    return true;
                }
                else if(map[i][j] == DOT_EMPTY){
                    flagEmpty = 1;
                    rowNumber = i;
                    columnNumber = j;
                }


            }
            if(counterRow == 2 && flagEmpty == 1){
                map[rowNumber][columnNumber] = dot;
                System.out.println("WinningRowAndFlag");
                return true;
            }
            flagEmpty = 0;
            counterRow = 0;
            columnNumber = 0;
            rowNumber = 0;
        }
        return false;
    }
    private static boolean aiWinningMoveColumn(char dot, int counterColumn) {
        int columnNumber = 0;
        int rowNumber = 0;
        int flagEmpty = 0;
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(map[j][i] == dot) counterColumn++;
                else if(map[j][i] == DOT_EMPTY && counterColumn == 2){
                    map[j][i] = dot;
                    System.out.println("DOT_EMPTY && counterColumn");
                    return true;
                }else if(map[j][i] == DOT_EMPTY){
                    flagEmpty = 1;
                    rowNumber = j;
                    columnNumber = i;
                }


            }
            if(counterColumn == 2 && flagEmpty == 1){
                map[rowNumber][columnNumber] = dot;
                System.out.println("WinningColumnAndFlag");
                return true;
            }
            flagEmpty = 0;
            counterColumn = 0;
            columnNumber = 0;
            rowNumber = 0;
        }
        return false;

    }
    private static boolean aiWinningMoveDiagonal(char dot, int counterDiagonal) {
        int rowAndColumnNumber = 0;
        int flagEmpty = 0;
        for(int i = 0; i < size; i++){
            if(map[i][i] == dot) counterDiagonal++;
            else if(map[i][i] == DOT_EMPTY && counterDiagonal == 2){
                map[i][i] = dot;
                System.out.println("DOT_EMPTY && counterDiagonal");
                return true;
            }
            else if(map[i][i] == DOT_EMPTY){
                flagEmpty = 1;
                rowAndColumnNumber = i;
            }

        }
        if(counterDiagonal == 2 && flagEmpty == 1){
            map[rowAndColumnNumber][rowAndColumnNumber] = dot;
            System.out.println("WinningDiagonalAndFlag");
            return true;
        }
        return false;
    }

    private static boolean aiWinningMoveAuxDiagonal(char dot, int counterAuxDiagonal) {
        int rowNumber = 0;
        int columnNumber = 0;
        int flagEmpty = 0;
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if((i + j) == (size - 1)){
                    if(map[i][j] == dot) counterAuxDiagonal++;
                    else if(map[i][j] == DOT_EMPTY && counterAuxDiagonal == 2){
                        map[i][j] = dot;
                        System.out.println("DOT_EMPTY && counterAuxDiagonal");
                        return true;
                    }
                    else if(map[i][j] == DOT_EMPTY){
                        flagEmpty = 1;
                        rowNumber = i;
                        columnNumber = j;
                    }
                }
            }
            if(counterAuxDiagonal == 2 && flagEmpty == 1){
                map[rowNumber][columnNumber] = dot;
                System.out.println("WinningAuxDiagonalAndFlag");
            }

        }
        return false;
    }
}
