package exercise;

import java.util.ArrayList;
import java.util.List;

public class PascalsTriangle {

    public List<Integer> getRow(int rowIndex) {
        List<int[]> pascalsTriangle = new ArrayList<>();

        for (int i = 0; i < rowIndex + 1; i++) {
            int[] row = new int[i + 1];
            row[0] = row[i] = 1;
            pascalsTriangle.add(row);
        }

        int[] row;

        if (rowIndex >= 2) {
            for (int i = 2; i < rowIndex + 1; i++) {
                int[] prevRow = pascalsTriangle.get(i - 1);
                int[] currentRow = pascalsTriangle.get(i);
                for (int j = 1; j < currentRow.length - 1; j++) {
                    currentRow[j] = prevRow[j - 1] + prevRow[j];
                }
            }
        }
        row = pascalsTriangle.get(rowIndex);

        List<Integer> result = new ArrayList<>();

        for (int num : row) {
            result.add(num);
        }

        return result;
    }

}
