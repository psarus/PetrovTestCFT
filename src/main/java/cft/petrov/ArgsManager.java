package cft.petrov;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс содержит методы получения из аргументов командной строки частей программы:
 * режима сортировки, типа данных, списка имен входных файлов, имени выходного файла.
 */
public class ArgsManager {

    private final String[] args;

    public ArgsManager(String[] args) {
        this.args = args;
    }

    /**
     * Метод определяет режим сортировки, исходя из значения первого аргумента командной строки,
     * и возвращает его.
     */
    public SortMode findAndGetSortType() {
        SortMode sortMode = SortMode.ASC;  // по умолчанию - сортировка по возрастанию
        switch (args[0]) {
            case "-a", "-A" -> System.out.println("Выбран режим сортировки по возрастанию.");
            case "-d", "-D" -> {
                sortMode = SortMode.DESC;
                System.out.println("Выбран режим сортировки по убыванию.");
            }
            // метод isValidArgs заботится, чтобы args[0].matches("^-[aAdDiIsS]$")
            default -> System.out.println("По умолчанию будет произведена сортировка по возрастанию.");
        }
        return sortMode;
    }

    /**
     * Метод определяет тип данных, исходя из значения первого (второго) аргумента командной строки,
     * и возвращает его.
     */
    public DataType findAndGetDataType() {
        DataType dataType;
        if (args[0].matches("-[iI]") || args[1].matches("-[iI]")) {
            dataType = DataType.INTEGER;
            System.out.println("Выбран режим работы с целыми числами.");
        }
        else {
            dataType = DataType.STRING;
            System.out.println("Выбран режим работы со строками.");
        }
        // метод isValidArgs заботится, чтобы тип данных matches("^-[iIsS]$")
        return dataType;
    }

    /**
     * Метод формирует и возвращает список имен входных файлов.
     */
    public List<String> findAndGetInputFileNames() {
        List<String> inputFileNames = new ArrayList<>();
        for (int i = args.length-1; !args[i-1].matches("^-[iIsS]$"); i--) {
            if (args[i].endsWith(".txt")) {
                inputFileNames.add(args[i]);
            }
        }
        return inputFileNames;
    }

    /**
     * Метод находит среди аргументов имя выходного файла и возвращает его.
     */
    public String findAndGetOutputFileName() {
        for (int i = 1; i < args.length; i++) {
            // почему с i = 1: [типСортировки] типДанных выхФайл вхФайл [вхФайл2 вхФайл3 ...]
            if (args[i].endsWith(".txt")) {
                return args[i];
            }
        }
        return "";
    }

}
