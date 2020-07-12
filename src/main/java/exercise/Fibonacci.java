package exercise;

public class Fibonacci {

    public int fib(int N) {
        if (N == 0 || N == 1) {
            return N;
        }

        int[] numbers = new int[N + 1];
        numbers[0] = 0;
        numbers[1] = 1;

        for (int i = 1; i < N + 1; i++) {
            int num = numbers[i - 1] + numbers[i];
            if (i != N) {
                numbers[i + 1] = num;
            }
            System.out.println("found " + i + " fib num " + num);
        }

        return numbers[N];
    }


    /*public int fib(int N) {
        if (N == 0) {
            return 0;
        }
        int prev = 0;
        int current = 1;
        int result = 0;

        for (int i = 1; i < N; i++) {
            result = prev + current;
            prev = current;
            current = result;
        }
        return result;
    }*/

}
