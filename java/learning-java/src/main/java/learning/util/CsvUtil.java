package learning.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public final class CsvUtil {
    public static Iterable<CSVRecord> parse(File input, boolean skipHeaderRecord, String... header) {
        Iterable<CSVRecord> output;

        try (Reader input_file_reader = new FileReader(input)) {
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(header)
                .setSkipHeaderRecord(skipHeaderRecord)
                .build();

            output = csvFormat.parse(input_file_reader).getRecords();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        return output;
    }
}
