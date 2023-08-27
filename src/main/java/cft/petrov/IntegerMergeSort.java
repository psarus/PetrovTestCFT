package cft.petrov;

/**
 * В классе осуществляется сортировка слиянием массива целочисленных данных по возрастанию / убыванию.
 */
public class IntegerMergeSort {

    /**
     * Метод рекурсивно делит массив на 2 части, пока в каждом подмассиве не окажется только 1 эл-т,
     * а затем запускается метод сортировки подмассивов со слиянием их воедино.
     *
     * @param intArr   неотсортированный массив валидных строк вх. файлов
     * @param sortMode режим сортировки
     */
    void divideIntArray(Integer[] intArr, SortMode sortMode) {
        int arrLength = intArr.length;

        // если в intArr 1 эл-т - прекращается деление на подмассивы
        if (arrLength == 1) {
            return;
        }

        int mid = arrLength / 2;                        // середина массива intArr
        Integer[] left = new Integer[mid];              // подмассив с левой половиной эл-ов intArr
        Integer[] right = new Integer[arrLength - mid]; // подмассив с правой половиной эл-ов intArr

        // копирование в подмассив left первом половины эл-ов intArr
        System.arraycopy(intArr, 0, left, 0, mid);
        // копирование в подмассив right второй половины эл-ов intArr
        System.arraycopy(intArr, mid, right, 0, arrLength - mid);

        // подмассивы left и right и дальше (рекурсивно) делятся пополам,
        // пока в к-м новом не останется по 1 эл-ту, а массив из 1 эл-та - отсортирован:
        divideIntArray(left, sortMode);
        divideIntArray(right, sortMode);

        // по окончанию деления подмассивы сливаются в один:
        sortArray(intArr, left, right, sortMode);
    }

    /**
     * Метод сортировки эл-ов подмассивов (по возрастанию или убыванию) и слияния их воедино.
     *
     * @param intArr   исходный массив, куда сольются отсортированные подмассивы
     * @param left     "левый" подмассив
     * @param right    "правый" подмассив
     * @param sortMode режим сортировки
     */
    private static void sortArray(Integer[] intArr, Integer[] left, Integer[] right, SortMode sortMode) {
        int leftLength = left.length;
        int rightLength = right.length;
        int i = 0, j = 0, indexArr = 0; // индексы эл-ов left, right и intArr

        if (sortMode == SortMode.ASC) {
            // пока не прошлись до конца left И right
            while (i < leftLength && j < rightLength) {
                // в intArr всегда записывается мин. на текущий момент эл-т
                intArr[indexArr++] = left[i] < right[j] ? left[i++] : right[j++];
            }
        }
        else {
            while (i < leftLength && j < rightLength) {
                // в intArr записывается макс. на текущий момент эл-т
                intArr[indexArr++] = left[i] > right[j] ? left[i++] : right[j++];
            }
        }
        // Поскольку у цикла два одновременных условия: i < leftLength && j < rightLength,
        // могут остаться эл-ты, которые не сохранились в массив intArr.
        // Если к примеру все эл-ты right < всех эл-ов left, то j++ быстро дойдет до конца (rightLength)
        // и while остановится. А эл-ты left НЕ сохранились, ибо все они > к-го эл-та right.
        // Ситуация м.б. обратной или же имеем комбо: сохранили часть left, часть right, часть left, right, ...
        // И по одному подмассиву прошли до конца, а по другому, опять-таки - нет.
        // Чтобы "добить" массивы до конца, я использую доп. переменные indexLeft и indexRight,
        // связанные с индексами эл-ов массивов i и j до которых НЕ дошли:
        for (int indexLeft = i; indexLeft < leftLength; indexLeft++) {
            intArr[indexArr++] = left[indexLeft];
        }
        for (int indexRight = j; indexRight < rightLength; indexRight++) {
            intArr[indexArr++] = right[indexRight];
        }
    }

}
