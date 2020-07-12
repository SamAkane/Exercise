package exercise;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/**
 * Привет, твой коллега забыл пин-код от двери в офис.
 * Клавиатура выглядит следующим образом:
 * 1 2 3
 * 4 5 6
 * 7 8 9
 *   0
 * Коллега вспоминает, что, кажется, пин код был 1357, но возможно каждая цифра должна быть сдвинута по горизонтали или вертикали, но не по диагонали.
 * Например, вместо 1 может быть 2 или 4, а вместо 5 может быть 2, 4, 6 или 8.
 * Помоги коллеге попасть в офис.
 * <p>
 * Входные данные
 * Строка с пин-кодом. Длина строки от 1 до 8 цифр, например, 1357.
 * <p>
 * Выходные данные
 * Исходный пин-код и возможные варианты пин-кодов с учетом сдвигов, отсортированные и разделенные запятой.
 * <p>
 * Не забудь, что пин-коды должны быть строками, так как могут начинаться с 0, и должны быть отсортированы, как строки.
 * <p>
 * Пример
 * Входные данные: 11
 * Выходные данные: 11,12,14,21,22,24,41,42,44
 * <p>
 * Входные данные: 8
 * Выходные данные: 0,5,7,8,9
 * <p>
 * Входные данные: 46
 * Выходные данные: 13,15,16,19,43,45,46,49,53,55,56,59,73,75,76,79
 */
public class Pin {
    private static String[][] keyboard = {{"1", "2", "3"}, {"4", "5", "6"}, {"7", "8", "9"}, {null, "0", null}};

    public static void findPinOption(String usersPin) {
        List<List<String>> allOptions = getOptionsList(usersPin);

        List<String> list = new ArrayList<>();
        combinations(allOptions, "", 0, list);

        Collections.sort(list);

        for(int i = 0; i < list.size();i++) {
            System.out.print(list.get(i));
            if(i < list.size() -1) {
                System.out.print(",");
            }
        }
    }

    private static void combinations(List<List<String>> allOptions, String str, int externalId, List<String> list) {
        if(externalId == allOptions.size() -1) {
            for(String s : allOptions.get(externalId)) {
                list.add(str + s);
            }
        } else {
            for(String s : allOptions.get(externalId)) {
                combinations(allOptions, str + s, externalId + 1, list);
            }
        }
    }

    public static String getUsersPin() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите пин:");
        String res = scanner.nextLine();
        while (!isValidPin(res)) {
            System.out.println("Длинна пина от 1 до 8 символов. Введите пин, состоящий из цифр:");
            res = scanner.nextLine();
        }
        scanner.close();
        return res;
    }

    private static boolean isValidPin(String pin) {
        return !pin.equals("") && pin.length() < 9 && pin.equals(pin.replaceAll("[^0-9]+", ""));
    }

    static void printKeyboard() {
        for (String[] strings : keyboard) {
            for (int id = 0; id < keyboard[0].length; id++) {
                if (strings[id] != null) {
                    System.out.print(strings[id] + "\t");
                } else {
                    System.out.print("\t");
                }
            }
            System.out.println();
        }
    }

    private static List<List<String>> getOptionsList(String userPin) {
        List<List<String>> options = new ArrayList<>();
        char[] pinByChar = userPin.toCharArray();
        for (char c : pinByChar) {
            Coordinate coordinate = findPositionOnKeyboard(String.valueOf(c));
            options.add(getCharOptions(coordinate));
        }
        return options;
    }

    private static List<String> getCharOptions(Coordinate coordinate) {
        int row = coordinate.getRow();
        int id = coordinate.getCell();
        return setOptions(getValue(row, id),
                getValue(row + 1, id),
                getValue(row - 1, id),
                getValue(row, id + 1),
                getValue(row, id - 1));
    }

    private static List<String> setOptions(String ... option) {
        List<String> options = new ArrayList<>();
        for(String s : option) {
            if(s != null) {
                options.add(s);
            }
        }
        return options;
    }

    private static String getValue(int row, int id) {
        String val = null;
        try {
            val = keyboard[row][id];
        } catch (Exception ignored) {
        }
        return val;
    }

    private static Coordinate findPositionOnKeyboard(String key) {
        Coordinate coordinate = null;
        for (int row = 0; row < keyboard.length; row++) {
            for (int id = 0; id < keyboard[0].length; id++) {
                if (keyboard[row][id] != null && keyboard[row][id].equals(key)) {
                    coordinate = new Coordinate(row, id);
                    break;
                }
            }
        }
        return coordinate;
    }

    private static class Coordinate {
        private int row;
        private int cell;

        Coordinate(int row, int id) {
            this.row = row;
            this.cell = id;
        }

        int getRow() {
            return row;
        }

        int getCell() {
            return cell;
        }
    }

}
