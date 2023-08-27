package cft.petrov;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * В классе осуществляется запись отсортированного массива валидных данных в выходной файл.
 */
public class OutputFileWriter {

    public <T> void writeArrayToFile(T[] arrayToWrite, String outputFileName) {
        Path path = Path.of(outputFileName).toAbsolutePath();

        try (BufferedWriter out = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
            for (T i : arrayToWrite) {
                out.write(i.toString());
                out.newLine();
            }

            System.out.printf("""
                    Элементы массива записаны в файл %s

                    """, path);
        }
        catch (AccessDeniedException e) {
            System.out.printf("""
                    Увы, у Вашей учетной записи нет права на запись в файл %s !
                    Необходимо изменить или разрешения на файл, или путь к файлу на директорию,
                    в которой у Вас есть право на запись.
                    Вы также можете попробовать запустить программу от имени администратора.
                    """, outputFileName);
        }
        catch (IOException e) {
            System.out.printf("Непредвиденная ошибка записи в файл " + outputFileName, e);
        }
    }

}
