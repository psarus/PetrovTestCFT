package cft.petrov;

import java.util.Comparator;
import java.util.List;

public class Main {
    // -d -i out.txt int.txt int3.txt s.txt e.txt int2.txt q.txt empty.txt s2.txt
    // -s NUL.txt COM.txt com0.txt PRN.txt PRN1.txt LPT5.txt LPT.txt f:.txt ;@.txt *.txt <h>.txt .txt а,2п.ап.txt
    public static void main(String[] args) {

        ArgsManager argsManager = new ArgsManager(args);
        ArgsValidator argsValidator = new ArgsValidator(args);

        if (!argsValidator.isValidArgs()) {
            argsValidator.printArgsHint();
            return;
        }

        List<String> inputFileNames = argsManager.findAndGetInputFileNames();
        DataType dataType = argsManager.findAndGetDataType();
        Comparator<String> comparator = new CustomComparator();
        InputFilesAnalyzer inputFilesAnalyzer = new InputFilesAnalyzer();
        List<String> dataToSort = inputFilesAnalyzer.selectValidData(inputFileNames, dataType, comparator);

        if (dataToSort.isEmpty()) {
            System.out.println("""
                    ==========================================================
                    Общий список валидных данных всех входных файлов пуст!
                    Сортировать нечего!
                    """);
        }
        else {
            System.out.printf("""
                    ==========================================================
                    Отобранные на сортировку слиянием данные входных файлов:
                    %s
                    """, dataToSort);
            // Есть что сортировать ? - Достаю режим сортировки и вых. файл
            SortMode sortMode = argsManager.findAndGetSortType();
            String outputFileName = argsManager.findAndGetOutputFileName();

            ValidDataManager validDataManager = new ValidDataManager();
            validDataManager.sortAndWrite(dataToSort, sortMode, dataType, outputFileName, comparator);
        }
        System.out.println("Программа завершила свою работу.");
    }

}
