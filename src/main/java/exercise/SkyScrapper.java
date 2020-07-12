package exercise;

import java.util.*;

/**
 * Необходимо расставить небоскрёбы в городе размером 6х6 клеток, учитывая следующие ограничения:
 *
 * 1. Высота любого небоскрёба: 1 - 6 этажей.
 *
 * 2. Количество этажей у небоскрёба должно быть уникальным по строке и по столбцу.
 *
 * 3. За более высокими небоскрёбами не видны более низкие.
 *
 * 4. Количество видимых небоскрёбов для строки или столбца (0 - любое количество).
 *
 *
 * Рассмотрим одну строку с двумя ограничениями на количество видимых небоскрёбов.
 *
 *  6	|	|	|	|	|	|	1
 *
 * Слева должно быть видно шесть зданий, а справа – только одно.
 *
 * Более высокие здания блокируют видимость более низких, поэтому возможен единственный способ расстановки:
 *
 *  6	1	2	3	4	5	6	1
 *
 * Входные данные
 * Ограничения по количеству видимых небоскрёбов заданы строкой, состоящей из 24 чисел, разделенных запятыми.
 *
 * Ограничения расположены по часовой стрелке.
 *
 * Каждое число на рисунке обозначает порядковый номер числа-ограничения в строке.
 *
 *        0	  1   2	  3	  4	  5
 *  23	|	|	|	|	|	|	6
 *  22	|	|	|	|	|	|	7
 *  21	|	|	|	|	|	|	8
 *  20	|	|	|	|	|	|	9
 *  19	|	|	|	|	|	|	10
 *  18	|	|	|	|	|	|	11
 *       17	 16	 15  14  13  12
 *
 * Выходные данные
 * 36 чисел, разделенные запятой.
 *
 * Первые 6 - высоты небоскребов в верхней строке карты, вторые 6 чисел - высоты небоскребов во второй строке карты и т.д.
 *
 *
 * Примечания
 * Задача имеет единственное решение.
 *
 * Напоминаем, что 0 в строке ограничений означает отсутствие ограничений.
 *
 * Пример
 *                     2  2
 *      |	|	|	|	|	|	|
 *      |	|	|	|	|	|	|
 *  3	|	|	|	|	|	|	|
 *      |	|	|	|	|	|	|   6
 *  4	|	|	|	|	|	|	|	3
 *  4	|	|	|	|	|	|	|
 *                        4
 *
 *                  2	2
 *      5	6	1	4	3	2
 *      4	1	3	2	6	5
 *  3	2	3	6	1	5	4
 *      6	5	4	3	2	1	6
 *  4	1	2	5	6	4	3	3
 *  4	3	4	2	5	1	6
 *                  4
 * Входные данные: 0,0,0,2,2,0,  0,0,0,6,3,0,  0,4,0,0,0,0,  4,4,0,3,0,0
 * Входные данные: 0,0,0,2,2,0,0,0,0,6,3,0,0,4,0,0,0,0,4,4,0,3,0,0
 * Выходные данные: 5,6,1,4,3,2,4,1,3,2,6,5,2,3,6,1,5,4,6,5,4,3,2,1,1,2,5,6,4,3,3,4,2,5,1,6
 */
public class SkyScrapper {

    private static int SIZE = 6;

    private static int sumPowers = 0;

    static {
        for (int i = 0; i < SIZE; ++i) {
            sumPowers += 1 << i;
        }
    }

    private static Map<Integer, Pair<Set<Integer>>> cache = new HashMap<>();

    public static int[] getClues() {
        int[] list = new int[24];
        Scanner scanner = new Scanner(System.in);
        String res = scanner.nextLine();
        while (!isValidEnter(res)) {
            res = scanner.nextLine();
        }
        scanner.close();

        char[] charSequence = res.replaceAll("[^0-6]+", "").toCharArray();
        for(int i = 0; i < charSequence.length; i++) {
            list[i] = Integer.parseInt(String.valueOf(charSequence[i]));
        }
        return list;
    }

    private static boolean isValidEnter(String string) {
        return string.replaceAll("[^0-6]+", "").length() == 24;
    }

    public static void printPuzzle(int[][] puzzle) {
        for(int i = 0; i < puzzle.length; i++) {
            for(int j = 0; j < puzzle[0].length; j++){
                System.out.print(puzzle[i][j]);
                if (i != puzzle.length - 1 || j != puzzle[0].length - 1) {
                    System.out.print(",");
                }
            }
        }
    }


    public static int[][] solvePuzzle(int[] clues) {
        List<Set<Integer>> rowSets = new ArrayList<>(SIZE);
        List<Set<Integer>> colSets = new ArrayList<>(SIZE);
        for (int i = 0; i < SIZE; ++i) {
            Set<Integer> leftToRight = getCombinationsSet(clues[4 * SIZE - 1 - i]).first;
            Set<Integer> rightToLeft = getCombinationsSet(clues[SIZE + i]).second;
            rowSets.add(intersect(leftToRight, rightToLeft));

            Set<Integer> topToBottom = getCombinationsSet(clues[i]).first;
            Set<Integer> bottomToTop = getCombinationsSet(clues[3 * SIZE - 1 - i]).second;
            colSets.add(intersect(topToBottom, bottomToTop));
        }

        int[][] board = new int[SIZE][SIZE];
        int[][] variants = new int[SIZE * SIZE][];
        long variantCount;
        while (true) {
            variantCount = 1;
            for (int r = 0; r < SIZE; ++r) {
                for (int c = 0; c < SIZE; ++c) {
                    int ind = r * SIZE + c;
                    int[] arr;
                    Set<Integer> rowVariants = getVariants(rowSets.get(r), c);
                    Set<Integer> colVariants = getVariants(colSets.get(c), r);
                    Set<Integer> merged = intersect(rowVariants, colVariants);
                    if (merged.size() == 0) {
                        throw new RuntimeException("merged is empty");
                    }
                    arr = new int[merged.size()];
                    int i = 0;
                    for (Integer val : merged) {
                        arr[i++] = val;
                    }
                    variants[ind] = arr;
                    board[r][c] = variants[ind][0];
                    variantCount *= arr.length;
                }
            }

            boolean changed = false;
            for (int i = 0; i < SIZE * SIZE; ++i) {
                int[] legal = variants[i];
                int row = i / SIZE;
                int col = i % SIZE;
                Set<Integer> values = rowSets.get(row);
                Set<Integer> fixed = excludeNotInPosition(values, legal, col);
                if (fixed.size() < values.size()) {
                    rowSets.set(row, fixed);
                    changed = true;
                }

                values = colSets.get(col);
                fixed = excludeNotInPosition(values, legal, row);
                if (fixed.size() < values.size()) {
                    colSets.set(col, fixed);
                    changed = true;
                }
            }

            if (! changed) break;
        }

        // We assume all tests will cause only single solution (i.e. variantCount == 1).
        // But just in case I do check all possibilities.
        if (variantCount > 1) {
            System.out.println("Variants to more then 1: " + variantCount);

            Line[] lines = getLines(clues, board);

            int[] counter = new int[SIZE * SIZE];
            boolean changed = true;
            while (changed && !isGood(lines)) {
                changed = false;
                for (int i = SIZE * SIZE - 1; i >= 0; i--) {
                    int limit = variants[i].length - 1;
                    if (counter[i] == limit) {
                        counter[i] = 0;
                        board[i / SIZE][i % SIZE] = variants[i][0];
                    } else {
                        counter[i]++;
                        board[i / SIZE][i % SIZE] = variants[i][counter[i]];
                        changed = true;
                        break;
                    }
                }
            }

            if (!changed) {
                throw new RuntimeException("Not found solution");
            }
        }

        return board;
    }

    private static Set<Integer> excludeNotInPosition(Set<Integer> values, int[] legal, int digitIndex) {
        Set<Integer> result = new HashSet<>();
        for (Integer val : values) {
            int digit = (val >> (digitIndex * 3)) & 7;
            boolean isLegal = false;
            for (int value : legal) {
                if (digit == value) {
                    isLegal = true;
                    break;
                }
            }
            if (isLegal) {
                result.add(val);
            }
        }
        return result;
    }

    private static Set<Integer> getVariants(Set<Integer> integers, int index) {
        Set<Integer> result = new HashSet<>();
        for (Integer val : integers) {
            int digit = (val >> (index * 3)) & 7;
            result.add(digit);
        }
        return result;
    }

    private static boolean isGood(Line[] lines) {
        for (Line line : lines) {
            if (!line.isValid()) return false;
        }
        return true;
    }

    private static Line[] getLines(int[] clues, int[][] board) {
        Line[] lines = new Line[4 * SIZE];
        int index = 0;
        for (int c = 0; c < SIZE; ++c) {
            lines[index] = new Line(board, clues[index]);
            lines[3 * SIZE - 1 - index] = new Line(board, clues[3 * SIZE - 1 - index]);
            for (int r = 0; r < SIZE; ++r) {
                lines[index].setCell(r, r, c);
                lines[3 * SIZE - 1 - index].setCell(r, SIZE - 1 - r, c);
            }
            index++;
        }
        for (int r = 0; r < SIZE; ++r) {
            lines[index] = new Line(board, clues[index]);
            lines[5 * SIZE - 1 - index] = new Line(board, clues[5 * SIZE - 1 - index]);
            for (int c = 0; c < SIZE; ++c) {
                lines[index].setCell(c, r, SIZE - 1 - c);
                lines[5 * SIZE - 1 - index].setCell(c, r, c);
            }
            index++;
        }
        return lines;
    }

    private static Set<Integer> intersect(Set<Integer> a, Set<Integer> b) {
        Set<Integer> result = new LinkedHashSet<>();
        for (Integer val : a) {
            if (b.contains(val)) {
                result.add(val);
            }
        }
        return result;
    }

    private static Pair<Set<Integer>> getCombinationsSet(int skyScrapers) {
        if (cache.containsKey(skyScrapers)) return cache.get(skyScrapers);
        Pair<ArrayList<Integer>> combinations = calcCombinations(skyScrapers);
        cache.put(skyScrapers, new Pair<>(new LinkedHashSet<>(combinations.first), new LinkedHashSet<>(combinations.second)));
        return cache.get(skyScrapers);
    }

    private static Pair<ArrayList<Integer>> calcCombinations(int skyScrapers) {
        ArrayList<Integer> result = new ArrayList<>();
        ArrayList<Integer> resultInv = new ArrayList<>();

        int[] temp3 = new int[SIZE];
        int[] temp = new int[SIZE];

        while (true) {
            for (int i = 0; i < SIZE; ++i) {
                temp[i] = i + 1;
            }

            int value = 0;
            int valueInv = 0;
            int max = 0;
            int visible = 0;
            for (int i = 0; i < SIZE && (visible <= skyScrapers || skyScrapers == 0); ++i) {
                int index = temp3[i];
                for (int j = 0; j < SIZE; ++j) {
                    if (temp[j] > 0) {
                        if (index == 0) {
                            if (temp[j] > max) {
                                max = temp[j];
                                visible++;
                                if (skyScrapers > 0 && visible > skyScrapers) {
                                    break;
                                }
                            }
                            value |= temp[j] << (3 * i);
                            valueInv |= temp[j] << (3 * (SIZE - 1 - i));
                            temp[j] = 0;
                            break;
                        } else {
                            index--;
                        }
                    }
                }
            }

            if (skyScrapers == 0 || visible == skyScrapers) {
                result.add(value);
                resultInv.add(valueInv);
            }

            boolean advanced = false;
            for (int i = SIZE - 1; i >= 0; i--) {
                int limit = SIZE - 1 - i;
                if (temp3[i] < limit) {
                    temp3[i]++;
                    advanced = true;
                    break;
                } else {
                    temp3[i] = 0;
                }
            }

            if (!advanced) break;
        }

        return new Pair<>(result, resultInv);
    }

    private static class Line {
        private int[] cells = new int[SIZE];
        private int skyScrapers;
        private int[][] board;

        Line(int[][] board, int skyScrapers) {
            this.skyScrapers = skyScrapers;
            this.board = board;
        }

        void setCell(int index, int row, int column) {
            cells[index] = row * 10 + column;
        }

        int get(int cellIndex) {
            return board[getRow(cells[cellIndex])][getColumn(cells[cellIndex])];
        }

        public void set(int cellIndex, int val) {
            board[getRow(cells[cellIndex])][getColumn(cells[cellIndex])] = val;
        }

        private int getRow(int index) {
            return index / 10;
        }

        private int getColumn(int index) {
            return index % 10;
        }

        boolean isValid() {
            int max = 0;
            int visible = 0;
            int magicSum = 0;

            for (int i = 0; i < SIZE; ++i) {
                int height = get(i);
                if (height > max) {
                    visible++;
                    max = height;
                }
                magicSum += 1 << (height - 1);
            }

            return magicSum == sumPowers && (skyScrapers == 0 || visible == skyScrapers);
        }
    }

    private static class Pair<T> {
        T first;
        T second;

        Pair(T first, T second) {
            this.first = first;
            this.second = second;
        }
    }

}
