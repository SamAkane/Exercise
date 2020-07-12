package exercise;

import org.junit.Test;

public class ExerciseTest {

    @Test
    public void fibonacciTest() {
        System.out.println(new Fibonacci().fib(0));
        System.out.println(new Fibonacci().fib(1));
        System.out.println(new Fibonacci().fib(2));
        System.out.println(new Fibonacci().fib(3));
        System.out.println(new Fibonacci().fib(4));
        System.out.println(new Fibonacci().fib(5));
    }

    @Test
    public void pTriangleTest() {
        System.out.println(new PascalsTriangle().getRow(0));
        System.out.println(new PascalsTriangle().getRow(1));
        System.out.println(new PascalsTriangle().getRow(2));
        System.out.println(new PascalsTriangle().getRow(3));
        System.out.println(new PascalsTriangle().getRow(4));
        System.out.println(new PascalsTriangle().getRow(5));
        System.out.println(new PascalsTriangle().getRow(6));
        System.out.println(new PascalsTriangle().getRow(7));
        System.out.println(new PascalsTriangle().getRow(8));
        System.out.println(new PascalsTriangle().getRow(9));
        System.out.println(new PascalsTriangle().getRow(10));
    }

    @Test
    public void pinTest() {
        Pin.printKeyboard();

        String userPin = Pin.getUsersPin();

        Pin.findPinOption(userPin);
    }

    @Test
    public void scyScrapperTest() {
        int[] clues = SkyScrapper.getClues();
        int[][] solvePuzzle = SkyScrapper.solvePuzzle(clues);
        SkyScrapper.printPuzzle(solvePuzzle);
    }

}
