package learning.pipeline.example.steps;

import learning.pipeline.example.constants.SOConstants;
import learning.pipeline.example.models.SODataset;
import learning.util.CsvUtil;
import lombok.NoArgsConstructor;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

@Component
@NoArgsConstructor
public class LoadDataset {
    final static Logger logger = LoggerFactory.getLogger(LoadDataset.class);

    //region Steps
    public static Map<Integer, SODataset> readQuestions(Map<Integer, SODataset> datasets) {
        var questions_file = new File(SOConstants.QUESTIONS_FILE);
        readDataset(datasets, questions_file, "Id", true, (dataset, record) -> {
            dataset.setTitle(record.get("Title"));
            dataset.setQuestion(record.get("Body"));
        });

        return datasets;
    }

    public static Map<Integer, SODataset> readAnswers(Map<Integer, SODataset> datasets) {
        var answers_file = new File(SOConstants.ANSWERS_FILE);
        readDataset(datasets, answers_file, "ParentId", false, (dataset, record) -> {
            dataset.setAnswer(record.get("Body"));
        });

        return datasets;
    }

    public static List<SODataset> readTags(Map<Integer, SODataset> datasets) {
        var tags_file = new File(SOConstants.TAGS_FILE);
        readDataset(datasets, tags_file, "Id", false, (dataset, record) -> {
            dataset.setTag(record.get("Tag"));
        });

        return datasets.values().stream().toList();
    }
    //endregion

    //region Utilities
    static void readDataset(Map<Integer, SODataset> datasets, File dataset_file, String id_field,
                            boolean create_dataset, BiConsumer<SODataset, CSVRecord> set_data) {
        var records = CsvUtil.parse(dataset_file, false);

        for (var record: records) {
            var record_id = Integer.parseInt(record.get(id_field));

            if (create_dataset) {
                datasets.put(record_id, new SODataset());
            }

            set_data.accept(datasets.get(record_id), record);
        }
    }
    //endregion

}
