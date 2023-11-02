package learning.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Objects;

public final class JsonUtil {

    //region Mappers
    static final XmlMapper xmlMapper = new XmlMapper();
    static final ObjectMapper jsonMapper = new ObjectMapper();
    //endregion

    public static void convertXML(File inFile, File outFile) throws IOException {
        //region Input Validation
        Objects.requireNonNull(inFile, "Input file missing");
        Objects.requireNonNull(outFile, "Output file missing");

        if (!inFile.exists()) {
            throw new FileNotFoundException();
        }
        //endregion

        JsonNode xmlData = xmlMapper.readTree(inFile);
        jsonMapper.writeValue(outFile, xmlData);
    }

    public static String toString(Object input) throws JsonProcessingException {
        return jsonMapper.writeValueAsString(input);
    }

    public static void toString(Object input, File output) throws IOException {
        jsonMapper.writerWithDefaultPrettyPrinter().writeValue(output, input);
    }

    public static void printObject(Object input) throws JsonProcessingException {
        System.out.println(toString(input));
    }

    //region Utility Executor
    public static void main(String[] args) throws IOException {
        //region Converting XML to JSON
        File inFile = new File("src/main/resources/datasets/wikipedia-simple.xml");
        File outFile = new File("src/main/resources/datasets/wikipedia-simple.json");
        convertXML(inFile, outFile);
        //endregion
    }
    //endregion
}
