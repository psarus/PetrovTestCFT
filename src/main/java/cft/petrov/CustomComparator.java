package cft.petrov;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Кастомный класс-компаратор для сравнения элементов в списке validByDataTypeFileLines.
 * Элемент может иметь "числовую часть" (н-р, "abc2": "abc" - строковая часть, "2" - числовая).
 * Идея в том, чтобы элемент-строку преобразовать в список подстрок,
 * где каждая подстрока представляет только строковую или числовую часть и сравнить списки подстрок,
 * проверяя, является ли i-я подстрока числом или строкой.
 * Это нужно, чтобы метод checkNaturalOrdering сортировал строки файла в "естественном порядке".
 * Так, н-р, при сравнении строк "abc2" > "abc10" (Alphabetical sorting),
 * я же хочу добиться, чтобы "abc2" < "abc10" (Natural sorting, привычный для человека порядок).
 */
public class CustomComparator implements Comparator<String> {
    // Мапа <строка, списокПодстрокСтроки>
    Map<String, List<String>> map = new HashMap<>();

    @Override
    public int compare(String current, String previous) {
        // Проверяется наличие в map ключа current (previous).
        // Если ключа нет или значение по ключу == null - считается значение, применяя к ключу метод parse.
        // Так ключ-строка преобразуется в список подстрок.
        // Если итоговое значение != null - пара ключ (строка) - значение (список подстрок) кладется в мапу.
        List<String> val1 = map.computeIfAbsent(current, CustomComparator::parse);
        List<String> val2 = map.computeIfAbsent(previous, CustomComparator::parse);

        int minList = Math.min(val1.size(), val2.size());
        for (int i = 0; i < minList; i++) {
            // извлечение части значения (одной из подстрок)
            String str1 = val1.get(i);
            String str2 = val2.get(i);

            int compareResult;  // результат сравнения подстрок

            // если в подстроках можно извлечь целочисленное значение
            if (str1.matches("\\d+") && str2.matches("\\d+")) {
                // идет сравнение подстрок как чисел
                compareResult = Integer.valueOf(str1).compareTo(Integer.valueOf(str2));
            }
            else { // сравнить как строковые значения
                compareResult = str1.compareTo(str2);
            }
            if (compareResult != 0) return compareResult;
        }
        return Integer.compare(val1.size(), val2.size());
    }

    /**
     * Метод преобразует каждую строку в список подстрок согласно шаблону,
     * "выделяя" у строки строковую часть и числовую.
     *
     * @param str входная строка
     * @return список подстрок
     */
    private static List<String> parse(String str) {
        final Pattern PATTERN = Pattern.compile("(\\D*)(\\d*)");
        // строка str сопоставляется с шаблоном "(не цифра)(цифра)"
        return PATTERN.matcher(str)
                .results()
                .filter(m -> !m.group(0).isEmpty()) // пропуск пустых совпадений с шаблоном
                .flatMap(m -> Stream.of(m.group(1), m.group(2))) // отображение строки на подстроки
                .collect(Collectors.toList()); // получение списка подстрок
    }

}
