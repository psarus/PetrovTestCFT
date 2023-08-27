package cft.petrov;

import java.util.Comparator;

/**
 * В классе осуществляется сортировка слиянием массива строк по возрастанию / убыванию.
 */

public class StringMergeSort {

    /**
     * Метод рекурсивно делит массив на 2 части, пока в каждом подмассиве не окажется только 1 эл-т,
     * а затем запускается метод сортировки подмассивов со слиянием их воедино.
     *
     * @param stringArr  неотсортированный массив валидных строк вх. файлов
     * @param sortMode   режим сортировки
     * @param comparator компаратор строк
     */
    void divideStringArray(String[] stringArr, SortMode sortMode, Comparator<String> comparator) {
        int arrLength = stringArr.length;

        // если в stringArr 1 эл-т - прекращается деление на подмассивы
        if (arrLength == 1) {
            return;
        }

        int mid = arrLength / 2;                      // середина массива stringArr
        String[] left = new String[mid];              // подмассив с левой половиной эл-ов stringArr
        String[] right = new String[arrLength - mid]; // подмассив с правой половиной эл-ов stringArr

        // копирование в подмассив left первом половины эл-ов stringArr
        System.arraycopy(stringArr, 0, left, 0, mid);
        // копирование в подмассив right второй половины эл-ов stringArr
        System.arraycopy(stringArr, mid, right, 0, arrLength - mid);

        // подмассивы left и right и дальше (рекурсивно) делятся пополам,
        // пока в к-м новом не останется по 1 эл-ту, а массив из 1 эл-та - отсортирован:
        divideStringArray(left, sortMode, comparator);
        divideStringArray(right, sortMode, comparator);

        // по окончанию деления подмассивы сливаются в один:
        sortArray(stringArr, left, right, sortMode, comparator);
    }

    /**
     * Метод сортировки эл-ов подмассивов (по возрастанию или убыванию) и слияния их воедино.
     *
     * @param stringArr  исходный массив, куда сольются отсортированные подмассивы
     * @param left       "левый" подмассив
     * @param right      "правый" подмассив
     * @param sortMode   режим сортировки
     * @param comparator компаратор строк
     */
    private static void sortArray(String[] stringArr, String[] left, String[] right, SortMode sortMode,
                                  Comparator<String> comparator) {
        int leftLength = left.length;
        int rightLength = right.length;
        int i = 0, j = 0, indexArr = 0; // индексы эл-ов left, right и stringArr

        if (sortMode == SortMode.ASC) {
            // пока не прошлись до конца left и right
            while (i < leftLength && j < rightLength) {
                // в stringArr записывается мин. на текущий момент эл-т
                stringArr[indexArr++] = comparator.compare(left[i], right[j]) < 0 ? left[i++] : right[j++];
            }
        }
        else {
            while (i < leftLength && j < rightLength) {
                // в stringArr записывается макс. на текущий момент эл-т
                stringArr[indexArr++] = comparator.compare(left[i], right[j]) > 0 ? left[i++] : right[j++];
            }
        }
        // см. класс IntegerMergeSort
        for (int indexLeft = i; indexLeft < leftLength; indexLeft++) {
            stringArr[indexArr++] = left[indexLeft];
        }
        for (int indexRight = j; indexRight < rightLength; indexRight++) {
            stringArr[indexArr++] = right[indexRight];
        }
    }

}
