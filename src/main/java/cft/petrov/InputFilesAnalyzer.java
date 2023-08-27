package cft.petrov;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Класс-фильтр данных входных файлов.
 * В нем происходит построчное чтение каждого входного файла с анализом содержимого каждой строки.
 * Сперва отсеиваются все пустые и содержащие пробелы строки.
 * Оставшиеся строки файла проходят проверку на соответствие выбранному типу данных
 * (на случай, если в файле присутствуют как целочисленные данные, так и строки типа String),
 * и успешно прошедшие проходят отсев на нарушение "естественного порядка сортировки" (Natural Sort Order).
 * Пережившие все проверки строки попадают в общий список валидных данных всех входных файлов (allValidData).
 * Это именно те данные, которые будут преданы сортировке слиянием.
 */
public class InputFilesAnalyzer {

    /**
     * Метод отбирает валидные строки к-го входного файла и сохраняет их в общий список валидных данных.
     *
     * @param inputFileNames список имен входных файлов
     * @param dataType       тип данных, с к-м работает программа
     * @param comparator     объект-компаратор
     * @return               общий список валидных данных всех входных файлов
     */
    public List<String> selectValidData(List<String> inputFileNames, DataType dataType, Comparator<String> comparator) {
        // список строк файла с валидным типом данных
        List<String> validByDataTypeFileLines = new ArrayList<>();
        // общий список валидных данных всех входных файлов
        List<String> allValidData = new ArrayList<>();

        String fileLine; // строчка входного файла
        int numFileLine; // номер строчки входного файла

        System.out.print("""
                ==========================================================
                Начинается построчная проверка содержимого входных файлов.
                """);

        Path path;
        for (String fileName : inputFileNames) {
            path = Path.of(fileName);
            try (BufferedReader in = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
                numFileLine = 0; // для к-го файла счетчик строк обнуляется

                System.out.printf("""
                        ==========================================================
                        Проверка содержимого файла %s
                        """, path.toAbsolutePath());

                if (!in.ready()) {  //  если в буфере нет данных
                    System.out.printf("Файл \"%s\" пуст.\n", fileName);
                    continue;  // иду к следующему файлу, с этого нечего взять
                }

                while ((fileLine = in.readLine()) != null) {
                    numFileLine++;
                    if (!fileLine.isEmpty() && !fileLine.contains(" ")) {
                        checkFileLineDataType(dataType, fileLine, numFileLine, validByDataTypeFileLines);
                    }
                    else if (fileLine.isEmpty()) {
                        System.out.printf("Строка %d пустая и не войдет в выходной файл.\n", numFileLine);
                    }
                    else {
                        System.out.printf("Строка %d содержит пробел(-ы) и не войдет в выходной файл.\n", numFileLine);
                    }
                }

                if (validByDataTypeFileLines.isEmpty()) {
                    System.out.printf("В файле \"%s\" не оказалось валидных данных.\n", fileName);
                }
                else {
                    System.out.printf("Список значений отобранных строк валидного типа данных:\n%s\n",
                            validByDataTypeFileLines);

                    checkNaturalOrdering(dataType, validByDataTypeFileLines, allValidData, comparator);

                    // список очищается перед заполнением валидными строками следующего вх. файла
                    validByDataTypeFileLines.clear();
                }
            }
            catch (NoSuchFileException e) {
                System.out.printf("""
                        ==========================================================
                        По пути %s не найден указанный в аргументах файл "%s".
                        Сортировка будет выполнена без учета файла "%s".
                        """, System.getProperty("user.dir"), fileName, fileName);
            }
            catch (IOException e) {
                System.out.printf("Ошибка при чтении входного файла " + fileName, e);
            }
        }
        return allValidData;
    }

    /**
     * Метод проверяет каждую непустую и не содержащую пробел(-ы) строчку входного файла
     * на соответствие указанному в аргументах типу данных и либо отклоняет ее,
     * либо добавляет в список строк файла с валидным типом данных.
     *
     * @param dataType                 тип данных, с к-м работает программа
     * @param fileLine                 проверяемая строчка файла
     * @param numFileLine              номер строчки файла
     * @param validByDataTypeFileLines список строк файла с валидным типом данных
     */
    private static void checkFileLineDataType(DataType dataType, String fileLine, int numFileLine,
                                              List<String> validByDataTypeFileLines) {
        if (dataType == DataType.INTEGER) {
            if (isInteger(fileLine)) {
                validByDataTypeFileLines.add(fileLine);
            }
            else {
                System.out.printf("Строка %d не содержит целочисленных данных и будет отброшена.\n", numFileLine);
            }
        }
        else {
            if (!isInteger(fileLine)) {
                validByDataTypeFileLines.add(fileLine);
            }
            else {
                System.out.printf("Строка %d может быть приведена к целочисленному типу данных и будет отброшена.\n",
                        numFileLine);
            }
        }
    }

    /**
     * Метод сравнивает каждый эл-т списка строк файла с валидным типом данных с предыдущим эл-ом,
     * и если он не нарушает "естественный порядок сортировки", добавляет эл-т в общий список валидных данных.
     *
     * @param dataType                 тип данных, с к-м работает программа
     * @param validByDataTypeFileLines список строк файла с валидным типом данных
     * @param allValidData             общий список валидных данных всех входных файлов
     */
    private static void checkNaturalOrdering(DataType dataType, List<String> validByDataTypeFileLines,
                                             List<String> allValidData, Comparator<String> comparator) {
        // предыдущий элемент списка
        String previous = null;

        if (dataType == DataType.INTEGER) {
            for (String current : validByDataTypeFileLines) {
                if (previous == null || (Integer.parseInt(current) >= Integer.parseInt(previous))) {
                    allValidData.add(current);
                    previous = current;
                }
                else {
                    System.out.printf("Строка \"%s\" нарушает естественный порядок сортировки и будет отброшена.\n",
                            current);
                }
            }
        }
        else {
            for (String current : validByDataTypeFileLines) {
                if (previous == null || comparator.compare(current, previous) >= 0) {
                    allValidData.add(current);
                    previous = current;
                }
                else {
                    System.out.printf("Строка \"%s\" нарушает естественный порядок сортировки и будет отброшена.\n",
                            current);
                }
            }
        }
    }

    /**
     * Метод проверяет, можно ли из строки извлечь целочисленные данные.
     *
     * @param fileLine строка файла
     * @return результат проверки (можно / нельзя)
     */
    private static boolean isInteger(String fileLine) {
        try {
            Integer.parseInt(fileLine);
        }
        catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
