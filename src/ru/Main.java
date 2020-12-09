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
    public static int chips = 3;
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
        if(size <= 5) chips = 3;
        else if(size >= 10) chips = 5;
        else chips = 4;

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
            // ТЕСТ
            /*map[1][1] = DOT_AI;
            map[3][3] = DOT_AI;
            map[4][4] = DOT_AI;*/





            if(aiWinningMove(DOT_AI)) System.out.println("ыыыыы");
            else if(aiBlock()) System.out.println("Заблокировал))");
            else aiMove();
            // * печать поля
            printMap();
            // * проверка победы компьютера
            if(isVictory(DOT_AI)){
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

        int counterDeltaDiagoinal = 0; // тестовая переменная для главной диагонали
        int delta = (size - chips); // переменная разрыва

        int counterDeltaAuxDiagonal = 0; // тестовая переменная для побочной диагонали
        int deltaAux = chips - 1;  // переменная разрыва

        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++) {
                if(map[i][j] == dot) counterRow++;
                else counterRow = 0;
                if(counterRow == chips) return true;

                if(map[j][i] == dot) counterColumn++;
                else counterColumn = 0;
                if(counterColumn == chips) return true;

                /*
                if (map[j][j] == dot) counterDiagonal++;
                else counterDiagonal = 0;

                if(counterDiagonal == chips) return true;

                if( (i + j) == (size - 1) ){
                    if(map[i][j] == dot) counterAuxDiagonal++;
                    else counterAuxDiagonal = 0;
                }
                if(counterAuxDiagonal == chips) return true;
                */
                if((i + (chips - 1)) < size && (j + (chips - 1)) < size){
                    if(map[i][j] == dot){
                        counterDeltaDiagoinal++;
                        for (int k = 1; k < chips; k++) {
                            if(map[i + k][j + k] == dot) counterDeltaDiagoinal++;
                            else counterDeltaDiagoinal = 0;
                        }
                    }
                    if(counterDeltaDiagoinal == chips){
                        System.out.println("Тест сработал");
                        return true;
                    }
                }

                if((i + (chips - 1)) < size && (j - (chips - 1)) >= 0){
                    if(map[i][j] == dot){
                        counterDeltaAuxDiagonal++;
                        for (int k = 1; k < chips; k++) {
                            if(map[i + k][j - k] == dot) counterDeltaAuxDiagonal++;
                            else counterDeltaAuxDiagonal = 0;
                        }
                    }
                    if(counterDeltaAuxDiagonal == chips){
                        System.out.println("Тест 2 сработал");
                        return true;
                    }
                }


                /*for (int k = 0; k < size; k++) {
                    if((j + k) == deltaAux){
                        if(map[j][k] == dot){
                            counterDeltaAuxDiagonal++;
                        }else counterDeltaAuxDiagonal = 0;
                    }
                    if(counterDeltaAuxDiagonal == chips){
                        System.out.println("Тест 2 сработал!");
                        return true;
                    }

                }*/


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
            rowNumber = random.nextInt(size) + 1;
            columnNumber = random.nextInt(size) + 1;
        }
        while(!isCellValid(rowNumber, columnNumber, DOT_AI));

        map[rowNumber - 1][columnNumber - 1] = DOT_AI;

    }

    private static boolean isCellValid(int row, int column, char dot) {
        if(row < 1 || row > size || column < 1 || column > size) {
            return false;
        }
        return map[row - 1][column - 1] == DOT_EMPTY;

    }

    public static boolean aiWinningMove(char dot){
        int counterRow = 0;
        int counterColumn = 0;
        int counterDiagonal = 0;
        int counterAuxDiagonal = 0;

        if(aiWinningMoveRow(dot, counterRow)) return true;
        else if(aiWinningMoveColumn(dot, counterColumn)) return true;
        else if(aiWinningMoveDiagonal(dot, counterDiagonal)) return true;
        else return aiWinningMoveAuxDiagonal(dot, counterAuxDiagonal);
    }



    private static boolean aiWinningMoveRow(char dot, int counterRow) {
        int columnNumber = 0;
        int rowNumber = 0;
        int flagEmpty = 0;
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(map[i][j] == dot) counterRow++;
                else if(map[i][j] == DOT_EMPTY && counterRow == (chips - 1)){
                    map[i][j] = dot;
                    System.out.println("Выигрышный ход строка 1-1");
                    return true;
                }
                else if(map[i][j] == DOT_EMPTY){
                    flagEmpty = 1;
                    rowNumber = i;
                    columnNumber = j;
                }


            }
            if(counterRow == (chips - 1) && flagEmpty == 1){
                map[rowNumber][columnNumber] = dot;
                System.out.println("Выигрышный ход строка 1-2");
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
                else if(map[j][i] == DOT_EMPTY && counterColumn == (chips - 1)){
                    map[j][i] = dot;
                    System.out.println("Выигрышный ход столбец 2-1");
                    return true;
                }else if(map[j][i] == DOT_EMPTY){
                    flagEmpty = 1;
                    rowNumber = j;
                    columnNumber = i;
                }


            }
            if(counterColumn == (chips - 1) && flagEmpty == 1){
                map[rowNumber][columnNumber] = dot;
                System.out.println("Выигрышный ход столбец 2-2");
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
        int rowNumber = 0;
        int ColumnNumber = 0;
        int flagEmpty = 0;
        int delta = -(size - chips);
        for(int i = 0; i < size; i++){
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    if((j - k) == delta){
                        if(map[j][k] == dot) counterDiagonal++;
                        else if(map[j][k] == DOT_EMPTY && counterDiagonal == (chips - 1)){
                            map[j][k] = dot;
                            System.out.println("Выигрышный ход диагональ 3-1");
                            return true;
                        }
                        else if(map[j][k] == DOT_EMPTY){
                            flagEmpty = 1;
                            rowNumber = j;
                            columnNumber = k;
                        }
                    }

                }

            }
            if(counterDiagonal == (chips - 1) && flagEmpty == 1){
                map[rowNumber][columnNumber] = dot;
                System.out.println("Выигрышный ход диагональ 3-2");
                return true;
            }

            flagEmpty = 0;
            counterDiagonal = 0;
            delta += 1;

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
                    else if(map[i][j] == DOT_EMPTY && counterAuxDiagonal == (chips - 1)){
                        map[i][j] = dot;
                        System.out.println("Выигрышный ход побочная диагональ 4-1");
                        return true;
                    }
                    else if(map[i][j] == DOT_EMPTY){
                        flagEmpty = 1;
                        rowNumber = i;
                        columnNumber = j;
                    }
                }
            }
            if(counterAuxDiagonal == (chips - 1) && flagEmpty == 1){
                map[rowNumber][columnNumber] = dot;
                System.out.println("Выигрышный ход побочная диагональ 4-2");
                return true;
            }

        }
        return false;
    }

    public static boolean aiBlock(){
        int counterRow = 0;
        int counterColumn = 0;
        int counterDiagonal = 0;
        int counterAuxDiagonal = 0;

        if(blockRow(counterRow)) return true;
        else if(blockColumn(counterColumn)) return true;
        else if(blockDiagonal(counterDiagonal)) return true;
        else return (blockAuxDiagonal(counterAuxDiagonal));
    }

    private static boolean blockRow(int counterRow) {
        int rowNumber = 0;
        int columnNumber = 0;
        int flagEmpty = 0;

        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(map[i][j] == DOT_HUMAN) counterRow++;
                else if(map[i][j] == DOT_EMPTY && counterRow == (chips - 1)){
                    map[i][j] = DOT_AI;
                    System.out.println("Строка человека заблокирована 1-1");
                    return true;
                }
                else if(map[i][j] == DOT_EMPTY){
                    flagEmpty = 1;
                    rowNumber = i;
                    columnNumber = j;
                }
            }
            if(counterRow == (chips - 1) && flagEmpty == 1){
                map[rowNumber][columnNumber] = DOT_AI;
                System.out.println("Строка человека заблокирована 1-2");
                return true;
            }
            flagEmpty = 0;
            rowNumber = 0;
            columnNumber = 0;
            counterRow = 0;
        }

        return false;

    }

    private static boolean blockColumn(int counterColumn) {
        int rowNumber = 0;
        int columnNumber = 0;
        int flagEmpty = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(map[j][i] == DOT_HUMAN) counterColumn++;
                else if(map[j][i] == DOT_EMPTY && counterColumn == (chips - 1)){
                    map[j][i] = DOT_AI;
                    System.out.println("Столбик человека заблокирован 2-1");
                    return true;
                }
                else if(map[j][i] == DOT_EMPTY){
                    flagEmpty = 1;
                    rowNumber = j;
                    columnNumber = i;
                }

            }
            if(counterColumn == (chips - 1) && flagEmpty ==1){
                map[rowNumber][columnNumber] = DOT_AI;
                System.out.println("Столбик человека заблокирован 2-2");
                return true;
            }
            flagEmpty = 0;
            rowNumber = 0;
            columnNumber = 0;
            counterColumn = 0;
        }
        return false;
    }

    private static boolean blockDiagonal(int counterDiagonal) {
        int rowAndColumnNumber = 0;
        int flagEmpty = 0;

        for (int i = 0; i < size; i++) {
            if(map[i][i] == DOT_HUMAN) counterDiagonal++;
            else if(map[i][i] == DOT_EMPTY && counterDiagonal == (chips - 1)){
                map[i][i] = DOT_AI;
                System.out.println("Диагональ человека заблокирована 3-1");
                return true;
            }
            else if(map[i][i] ==  DOT_EMPTY){
                flagEmpty = 1;
                rowAndColumnNumber = i;
            }

        }
        if(counterDiagonal == (chips - 1) && flagEmpty == 1){
            map[rowAndColumnNumber][rowAndColumnNumber] = DOT_AI;
            System.out.println("Диагональ человека заблокирована 3-2");
            return true;
        }
        return false;
    }

    private static boolean blockAuxDiagonal(int counterAuxDiagonal) {
        int rowNumber = 0;
        int columnNumber = 0;
        int flagEmpty = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if((i + j) == (size - 1)){
                    if(map[i][j] == DOT_HUMAN) counterAuxDiagonal++;
                    else if(map[i][j] == DOT_EMPTY && counterAuxDiagonal == (chips - 1)){
                        map[i][j] = DOT_AI;
                        System.out.println("Побочная диагональ человека заблокирована 4-1");
                        return true;
                    }
                    else if(map[i][j] == DOT_EMPTY){
                        flagEmpty = 1;
                        rowNumber = i;
                        columnNumber = j;
                    }
                }

            }
            if(counterAuxDiagonal == (chips - 1) && flagEmpty == 1){
                map[rowNumber][columnNumber] = DOT_AI;
                System.out.println("Побочная диагональ человека заблокирована 4-2");
                return true;
            }

        }
        return false;
    }
}
