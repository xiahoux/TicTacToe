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

    // задание размера карты
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

    // печать карты
    public static void printMap() {
        printMapHeader();

        printMapRow();
    }

    // печать нумерации сверху
    private static void printMapHeader() {
        System.out.print(HEADER_DOT + EMPTY);
        for(int i = 0; i < size; i++){
            System.out.print(i + 1 + EMPTY);
        }
        System.out.println();
    }

    // печать нумерации сбоку и самой карты
    public static void printMapRow() {
        for(int i = 0; i < size; i++){
            System.out.print(i + 1 + EMPTY);
            for(int j = 0; j < size; j++){
                System.out.print(map[i][j] + EMPTY);
            }
            System.out.println();
        }
    }

    // запуск игры
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

            // поиск выигрышного хода
            if(aiWinningMove(DOT_AI)) System.out.println("П-О-Б-Е-Д-А!");
            else if(aiBlock()) System.out.println("Заблокировал))"); // поиск выигрышного хода противника и его блокировка
            else aiMove(); // рандомный ход за неимением лучшего
            // * печать поля
            printMap();
            // * проверка победы компьютера
            if(isVictory(DOT_AI)){
                System.out.println("Плоть слаба! Машины победили!");
                System.exit(0);
            }
            // проверка на ничью
            if(isDraw()){
                printMap();
                System.out.println("Ничья");
                System.exit(0);
            }

        }


    }


    // * ход человека и его проверка
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

    // проверка попытки сделать ход
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

    // Проверка - является ли ход победным
    private static boolean isVictory(char dot) {
        // * проверка победы
        int counterRow = 0; // счётчик сделанных ходов в строке
        int counterColumn = 0; // в столбике
        int counterDiagonal = 0; // в диагонали
        int counterAuxDiagonal = 0; // в побочной диагонали


        // проверка на победу
        if (isVictoryRow(dot, counterRow, counterColumn)) return true; // проверка строк
        else if(isVictoryColumn(dot, counterRow, counterColumn)) return true; // проверка столбиков
        else if(isVictoryDiagonal(dot,counterDiagonal)) return true; // проверка диагоналей
        else return isVictoryAuxDiagonal(dot, counterAuxDiagonal); // проверка побочных диагоналей

    }

    // проверка строк на победу
    private static boolean isVictoryRow(char dot, int counterRow, int counterColumn) {
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++) {
                if(map[i][j] == dot) counterRow++;
                else counterRow = 0;
                if(counterRow == chips) return true;
            }
            counterRow = 0;
            counterColumn = 0;
        }
        return false;
    }

    // проверка столбиков на победу
    private static boolean isVictoryColumn(char dot, int counterRow, int counterColumn) {
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++) {
                if(map[j][i] == dot) counterColumn++;
                else counterColumn = 0;
                if(counterColumn == chips) return true;
            }
            counterRow = 0;
            counterColumn = 0;
        }
        return false;
    }

    // проверка диагоналей на победу
    private static boolean isVictoryDiagonal(char dot, int counterDeltaDiagoinal) {
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++) {
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
            }
        }
        return false;
    }

    // проверка побочных диагоналей на победу
    private static boolean isVictoryAuxDiagonal(char dot, int counterDeltaAuxDiagonal) {
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++) {
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
            }
        }
        return false;
    }

    // проверка на ничью
    public static boolean isDraw(){
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                if(map[i][j] == DOT_EMPTY) return false;
            }
        }
        return true;
    }

    // рандомный ход компьютера
    public static void aiMove(){
        do{
            rowNumber = random.nextInt(size) + 1;
            columnNumber = random.nextInt(size) + 1;
        }
        while(!isCellValid(rowNumber, columnNumber, DOT_AI));

        map[rowNumber - 1][columnNumber - 1] = DOT_AI;

    }

    // проверка входных типов данных при вводе строки и столбца для хода
    private static boolean isCellValid(int row, int column, char dot) {
        if(row < 1 || row > size || column < 1 || column > size) {
            return false;
        }
        return map[row - 1][column - 1] == DOT_EMPTY;

    }

    // поиск выигрышного хода
    public static boolean aiWinningMove(char dot){
        int counterRow = 0;
        int counterColumn = 0;
        int counterDiagonal = 0;
        int counterAuxDiagonal = 0;

        if(aiWinningMoveRow(dot, counterRow)) return true; // поиск по строкам
        else if(aiWinningMoveColumn(dot, counterColumn)) return true; // по столбикам
        else if(aiWinningMoveDiagonal(dot, counterDiagonal)) return true; // по диагоналям
        else return aiWinningMoveAuxDiagonal(dot, counterAuxDiagonal); // по побочным диагоналям
    }


    // поиск выигрышного хода по строкам
    private static boolean aiWinningMoveRow(char dot, int counterRow) {
        int columnNumber = 0; // номер столбик для запоминания пустой ячейки
        int rowNumber = 0; // номер строки для запоминания пустой ячейки
        int flagEmpty = 0;  // флаг для указания, что пустая ячейка в строке была

        // проходимся по массиву
        for(int i = 0; i < size; i++){
            for (int j = 0; j < size; j++) {
                if((j + (chips - 1)) < size) { // если из этой ячейки возможно провести выигрышную комбинацию по строке

                    for (int k = 0; k < chips; k++) { // проверка конкретной строки на возможность выигрышного хода
                        if (map[i][j + k] == dot) counterRow++; // если следующая ячейка в выбранной строке заполнена
                        else if(map[i][j + k] == DOT_EMPTY && counterRow == (chips - 1)) { // если следующая ячейка пустая и в этой строке осталось сделать последний ход для победы
                            map[i][j + k] = DOT_AI; // занять эту пустую ячейку
                            System.out.println("Выигрышный ход строка 1-1");
                            return true;
                        }
                        else if(map[i][j + k] == DOT_EMPTY) { // иначе запомнить пустую клетку в строке
                            flagEmpty = 1;
                            rowNumber = i;
                            columnNumber = (j + k);
                        }
                    }
                    if(counterRow == (chips - 1) && flagEmpty == 1) { //если есть одна пустая клетка, необходимая для победы
                        map[rowNumber][columnNumber] = DOT_AI;
                        System.out.println("Выигрышный ход строка 1-2");
                        return true;
                    }
                    flagEmpty = 0;
                    rowNumber = 0;
                    columnNumber = 0;
                    counterRow = 0;


                }
            }
        }
        return false;

    }
    private static boolean aiWinningMoveColumn(char dot, int counterColumn) {
        int columnNumber = 0; // номер столбик для запоминания пустой ячейки
        int rowNumber = 0; // номер строки для запоминания пустой ячейки
        int flagEmpty = 0; // флаг для указания, что пустая ячейка в строке была


        for(int i = 0; i < size; i++){
            for (int j = 0; j < size; j++) {
                if((i + (chips - 1)) < size) {

                    for (int k = 0; k < chips; k++) {
                        if (map[i + k][j] == dot) counterColumn++;
                        else if(map[i + k][j] == DOT_EMPTY && counterColumn == (chips - 1)) {
                            map[i + k][j] = DOT_AI;
                            System.out.println("Выигрышный ход столбик 2-1");
                            return true;
                        }
                        else if(map[i + k][j] == DOT_EMPTY) {
                            flagEmpty = 1;
                            rowNumber = (i + k);
                            columnNumber = j;
                        }
                    }
                    if(counterColumn == (chips - 1) && flagEmpty == 1) {
                        map[rowNumber][columnNumber] = DOT_AI;
                        System.out.println("Выигрышный ход столбик 2-2");
                        return true;
                    }
                    flagEmpty = 0;
                    rowNumber = 0;
                    columnNumber = 0;
                    counterColumn = 0;


                }
            }
        }
        return false;

    }
    private static boolean aiWinningMoveDiagonal(char dot, int counterDiagonal) {
        int rowNumber = 0;
        int ColumnNumber = 0;
        int flagEmpty = 0;

        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++) {
                if((i + (chips - 1)) < size && (j + (chips - 1)) < size){

                    for (int k = 0; k < chips; k++) {
                        if(map[i + k][j + k] == dot) counterDiagonal++;
                        else if(map[i + k][j + k] == DOT_EMPTY && counterDiagonal == (chips - 1)){
                            map[i + k][j + k] = DOT_AI;
                            System.out.println("Выигрышный ход диагональ 3-1");
                            return true;
                        }
                        else if(map[i + k][j + k] == DOT_EMPTY){
                            flagEmpty = 1;
                            rowNumber = (i + k);
                            columnNumber = (j + k);
                        }
                    }
                    if(counterDiagonal == (chips -1) && flagEmpty == 1){
                        map[rowNumber][columnNumber] = DOT_AI;
                        System.out.println("Выигрышный ход диагональ 3-2");
                        return true;
                    }
                    flagEmpty = 0;
                    rowNumber = 0;
                    columnNumber = 0;
                    counterDiagonal = 0;
                }


            }
        }
        return false;
    }

    private static boolean aiWinningMoveAuxDiagonal(char dot, int counterAuxDiagonal) {
        int rowNumber = 0;
        int columnNumber = 0;
        int flagEmpty = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if ((i + (chips - 1)) < size && (j - (chips - 1)) >= 0) {

                    for (int k = 0; k < chips; k++) {
                        if (map[i + k][j - k] == dot) counterAuxDiagonal++;
                        else if (map[i + k][j - k] == DOT_EMPTY && counterAuxDiagonal == (chips - 1)) {
                            map[i + k][j - k] = DOT_AI;
                            System.out.println("Выигрышный ход побочная диагональ 4-1");
                            return true;
                        } else if (map[i + k][j - k] == DOT_EMPTY) {
                            flagEmpty = 1;
                            rowNumber = (i + k);
                            columnNumber = (j - k);
                        }
                    }
                    if (counterAuxDiagonal == (chips - 1) && flagEmpty == 1) {
                        map[rowNumber][columnNumber] = DOT_AI;
                        System.out.println("Выигрышный ход побочная диагональ 4-2");
                        return true;
                    }
                    flagEmpty = 0;
                    rowNumber = 0;
                    columnNumber = 0;
                    counterAuxDiagonal = 0;
                }


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
            for (int j = 0; j < size; j++) {
                if((j + (chips - 1)) < size) {

                    for (int k = 0; k < chips; k++) {
                        if (map[i][j + k] == DOT_HUMAN) counterRow++;
                        else if(map[i][j + k] == DOT_EMPTY && counterRow == (chips - 1)) {
                            map[i][j + k] = DOT_AI;
                            System.out.println("Строка человека заблокирована 1-1");
                            return true;
                        }
                        else if(map[i][j + k] == DOT_EMPTY) {
                            flagEmpty = 1;
                            rowNumber = i;
                            columnNumber = (j + k);
                        }
                    }
                    if(counterRow == (chips - 1) && flagEmpty == 1) {
                        map[rowNumber][columnNumber] = DOT_AI;
                        System.out.println("Строка человека заблокирована 1-2");
                        return true;
                    }
                    flagEmpty = 0;
                    rowNumber = 0;
                    columnNumber = 0;
                    counterRow = 0;


                }
            }
        }
        return false;
    }

    private static boolean blockColumn(int counterColumn) {
        int rowNumber = 0;
        int columnNumber = 0;
        int flagEmpty = 0;

        for(int i = 0; i < size; i++){
            for (int j = 0; j < size; j++) {
                if((i + (chips - 1)) < size) {

                    for (int k = 0; k < chips; k++) {
                        if (map[i + k][j] == DOT_HUMAN) counterColumn++;
                        else if(map[i + k][j] == DOT_EMPTY && counterColumn == (chips - 1)) {
                            map[i + k][j] = DOT_AI;
                            System.out.println("Столбик человека заблокирован 2-1");
                            return true;
                        }
                        else if(map[i + k][j] == DOT_EMPTY) {
                            flagEmpty = 1;
                            rowNumber = (i + k);
                            columnNumber = j;
                        }
                    }
                    if(counterColumn == (chips - 1) && flagEmpty == 1) {
                        map[rowNumber][columnNumber] = DOT_AI;
                        System.out.println("Столбик человека заблокирован 2-2");
                        return true;
                    }
                    flagEmpty = 0;
                    rowNumber = 0;
                    columnNumber = 0;
                    counterColumn = 0;


                }
            }
        }
        return false;

    }

    private static boolean blockDiagonal(int counterDiagonal) {
        int rowNumber = 0;
        int columnNumber = 0;
        int flagEmpty = 0;

        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++) {
                if((i + (chips - 1)) < size && (j + (chips - 1)) < size){

                    for (int k = 0; k < chips; k++) {
                        if(map[i + k][j + k] == DOT_HUMAN) counterDiagonal++;
                        else if(map[i + k][j + k] == DOT_EMPTY && counterDiagonal == (chips - 1)){
                            map[i + k][j + k] = DOT_AI;
                            System.out.println("Диагональ человека заблокирована 3-1");
                            return true;
                        }
                        else if(map[i + k][j + k] == DOT_EMPTY){
                            flagEmpty = 1;
                            rowNumber = (i + k);
                            columnNumber = (j + k);
                        }
                    }
                    if(counterDiagonal == (chips -1) && flagEmpty == 1){
                        map[rowNumber][columnNumber] = DOT_AI;
                        System.out.println("Диагональ человека заблокирована 3-2");
                        return true;
                    }
                    flagEmpty = 0;
                    rowNumber = 0;
                    columnNumber = 0;
                    counterDiagonal = 0;
                }


            }
        }
        return false;
    }

    private static boolean blockAuxDiagonal(int counterAuxDiagonal) {
        int rowNumber = 0;
        int columnNumber = 0;
        int flagEmpty = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if ((i + (chips - 1)) < size && (j - (chips - 1)) >= 0) {

                    for (int k = 0; k < chips; k++) {
                        if (map[i + k][j - k] == DOT_HUMAN) counterAuxDiagonal++;
                        else if (map[i + k][j - k] == DOT_EMPTY && counterAuxDiagonal == (chips - 1)) {
                            map[i + k][j - k] = DOT_AI;
                            System.out.println("Побочная диагональ человека заблокирована 4-1");
                            return true;
                        } else if (map[i + k][j - k] == DOT_EMPTY) {
                            flagEmpty = 1;
                            rowNumber = (i + k);
                            columnNumber = (j - k);
                        }
                    }
                    if (counterAuxDiagonal == (chips - 1) && flagEmpty == 1) {
                        map[rowNumber][columnNumber] = DOT_AI;
                        System.out.println("Побочная диагональ человека заблокирована 4-2");
                        return true;
                    }
                    flagEmpty = 0;
                    rowNumber = 0;
                    columnNumber = 0;
                    counterAuxDiagonal = 0;
                }
            }
        }
        return false;
    }
}
