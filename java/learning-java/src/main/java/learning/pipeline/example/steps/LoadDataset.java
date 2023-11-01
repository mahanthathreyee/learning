package learning.pipeline.example.steps;

import com.fasterxml.jackson.core.JsonProcessingException;
import learning.constants.AppConstants;
import learning.pipeline.example.constants.SOConstants;
import learning.pipeline.example.models.SODataset;
import learning.util.JsonUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Component
@NoArgsConstructor
public class LoadDataset {

    static Map<Integer, SODataset> readQuestions(Map<Integer, SODataset> datasets) {
        try (Reader questions_file = new FileReader(SOConstants.QUESTIONS_FILE)) {
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader()
                .setSkipHeaderRecord(false)
                .build();

            Iterable<CSVRecord> records = csvFormat.parse(questions_file);

            for (CSVRecord record : records) {
                int question_id = Integer.parseInt(record.get("Id"));
                datasets.put(question_id, new SODataset(
                    record.get("Title"),
                    record.get("Body"),
                    null,
                    null
                ));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return datasets;
    }

    public static void main(String[] args) throws IOException {
        Map<Integer, SODataset> datasets = new HashMap<>();
        datasets = readQuestions(datasets);
        File questions_output = new File(AppConstants.RESOURCE_DIR + "datasets/stackoverflow/questions.json");
        JsonUtil.toString(datasets, questions_output);
    }
}
