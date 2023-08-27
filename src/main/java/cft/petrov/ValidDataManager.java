package cft.petrov;

import java.util.Comparator;
import java.util.List;

/**
 * Класс манипулирует отобранными валидными данными входных файлов.
 */
public class ValidDataManager {
    /**
     * Метод создает массив данных на сортировку, запускает методы сортировки массива
     * и записи отсортированных данных в выходной файл.
     *
     * @param dataToSort     данные на сортировку
     * @param sortMode       режим сортировки
     * @param dataType       тип данных
     * @param outputFileName имя выходного файла
     */
    public void sortAndWrite(List<String> dataToSort, SortMode sortMode, DataType dataType, String outputFileName,
                             Comparator<String> comparator) {

        OutputFileWriter outputFileWriter = new OutputFileWriter();

        if (dataType == DataType.INTEGER) {
            Integer[] intArr = dataToSort.stream().mapToInt(Integer::parseInt).boxed().toArray(Integer[]::new);
            System.out.println("Создан массив целых чисел на сортировку слиянием.");

            IntegerMergeSort intMergeSort = new IntegerMergeSort();
            intMergeSort.divideIntArray(intArr, sortMode);
            System.out.println("Массив целых чисел отсортирован.");

            outputFileWriter.writeArrayToFile(intArr, outputFileName);
        }
        else {
            String[] stringArr = dataToSort.toArray(String[]::new);
            System.out.println("Создан массив строковых данных на сортировку слиянием.");

            StringMergeSort stringMergeSort = new StringMergeSort();
            stringMergeSort.divideStringArray(stringArr, sortMode, comparator);
            System.out.println("Массив строковых данных отсортирован.");

            outputFileWriter.writeArrayToFile(stringArr, outputFileName);
        }
    }

}
