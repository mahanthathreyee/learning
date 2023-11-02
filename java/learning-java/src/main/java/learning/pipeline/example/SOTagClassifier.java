package learning.pipeline.example;

import learning.constants.AppConstants;
import learning.pipeline.Pipeline;
import learning.pipeline.example.models.SODataset;
import learning.pipeline.example.steps.LoadDataset;
import learning.pipeline.example.steps.NLPClassifier;
import learning.util.JsonUtil;
import opennlp.tools.doccat.DocumentSample;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SOTagClassifier {
    /*
    Steps:
    1. Load questions to memory
    2. Load answers to memory and map to question ID
    3. Load tags to memory and map to question ID
    4. Construct model training file
    5. Train OpenNLP model with 50% dataset
    6. Validate trained model with remaining dataset
     */

    final static Logger logger = LoggerFactory.getLogger(NLPClassifier.class);

    public static void main(String[] args) throws IOException {
        Pipeline<Map<Integer, SODataset>, Double> readDataset =
            new Pipeline<>(LoadDataset::readQuestions)
                .next(LoadDataset::readAnswers)
                .next(LoadDataset::readTags)
                .next(NLPClassifier::constructTrainingDocument)
                .next(NLPClassifier::trainValidateModel);

        logger.info(String.format("Accuracy: %f", readDataset.run(new HashMap<>())));
    }

}
