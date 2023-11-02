package learning.pipeline.example.steps;

import learning.pipeline.example.models.SODataset;
import lombok.NoArgsConstructor;
import opennlp.tools.doccat.*;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.ObjectStreamUtils;
import opennlp.tools.util.TrainingParameters;
import opennlp.tools.util.model.ModelUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class NLPClassifier {
    final static Logger logger = LoggerFactory.getLogger(NLPClassifier.class);

    final static int CROSS_FOLDS = 10;
    final static int MODEL_THREADS = 10;
    final static int MODEL_CUTOFF_PARAM = 0;
    final static String MODEL_LANGUAGE_CODE = "en";

    public static List<DocumentSample> constructTrainingDocument(List<SODataset> datasets) {
        return datasets.stream()
            .map(NLPClassifier::convertToDocument)
            .collect(Collectors.toList());
    }

    public static double trainValidateModel(List<DocumentSample> datasets) {
        ObjectStream<DocumentSample> sampleStream = ObjectStreamUtils.createObjectStream(datasets);

        TrainingParameters params = ModelUtil.createDefaultTrainingParameters();
        params.put(TrainingParameters.CUTOFF_PARAM, MODEL_CUTOFF_PARAM);
        params.put(TrainingParameters.THREADS_PARAM, MODEL_THREADS);

        DoccatFactory factory = new DoccatFactory(new FeatureGenerator[] { new BagOfWordsFeatureGenerator() });

        DoccatCrossValidator model_validator = new DoccatCrossValidator(MODEL_LANGUAGE_CODE, params, factory);

        try {
            model_validator.evaluate(sampleStream, CROSS_FOLDS);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        return model_validator.getDocumentAccuracy();
    }

    //region Utilities
    static DocumentSample convertToDocument(SODataset dataset) {
        return new DocumentSample(
            dataset.getTag(),
            new String[] {dataset.getTitle(), dataset.getQuestion(), dataset.getAnswer()}
        );
    }
    //endregion
}
