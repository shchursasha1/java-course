package com.quizmaker.service.loader;

import com.quizmaker.model.Question;
import com.quizmaker.model.QuestionBank;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Loads questions from JSON files.
 *
 * JSON format: array of objects with fields: id, questionText, answers (array),
 * correctAnswerIndex, category, difficulty.
 *
 * This class implements the QuestionLoader interface, following the Open/Closed
 * and Single Responsibility principles.
 *
 * @author Developer Team
 * @version 1.0
 * @since 2025-12-10
 */
public class JsonQuestionLoader implements QuestionLoader {

  private static final String FORMAT = "JSON";

  /**
   * Loads questions from a JSON file.
   *
   * @param filePath path to the JSON file
   * @return a QuestionBank containing loaded questions
   * @throws IOException if the file cannot be read or parsed
   */
  @Override
  public QuestionBank load(String filePath) throws IOException {
    QuestionBank bank = new QuestionBank();

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      StringBuilder jsonContent = new StringBuilder();
      String line;

      while ((line = reader.readLine()) != null) {
        jsonContent.append(line);
      }

      String json = jsonContent.toString().trim();
      json = json.replaceAll("\\[\\s*", "").replaceAll("\\s*\\]", "").trim();

      if (!json.isEmpty()) {
        String[] objects = json.split("\\},\\s*\\{");

        for (String obj : objects) {
          obj = obj.replaceAll("[\\{\\}]", "").trim();
          Question question = parseJsonObject(obj);
          if (question != null) {
            bank.addQuestion(question);
          }
        }
      }
    } catch (IOException e) {
      throw new IOException("Failed to load JSON file: " + filePath, e);
    }

    return bank;
  }

  /**
   * Gets the supported file format.
   *
   * @return "JSON"
   */
  @Override
  public String getSupportedFormat() {
    return FORMAT;
  }

  /**
   * Parses a single JSON object string into a Question.
   *
   * @param jsonObject the JSON object string
   * @return a Question object, or null if parsing fails
   */
  private Question parseJsonObject(String jsonObject) {
    try {
      String id = extractJsonValue(jsonObject, "id");
      String questionText = extractJsonValue(jsonObject, "questionText");
      String category = extractJsonValue(jsonObject, "category");
      int correctIndex = Integer.parseInt(extractJsonValue(jsonObject, "correctAnswerIndex"));
      int difficulty = Integer.parseInt(extractJsonValue(jsonObject, "difficulty"));

      List<String> answers = extractJsonArray(jsonObject, "answers");

      return new Question(id, questionText, answers, correctIndex, category, difficulty);
    } catch (NumberFormatException e) {
      System.err.println("Error parsing JSON object: " + e.getMessage());
      return null;
    }
  }

  /**
   * Extracts a string value from a JSON object.
   *
   * @param json the JSON string
   * @param key the key to search for
   * @return the extracted value
   */
  private String extractJsonValue(String json, String key) {
    String pattern = "\"" + key + "\"\\s*:\\s*\"([^\"]*?)\"";
    Pattern p = Pattern.compile(pattern);
    Matcher m = p.matcher(json);
    if (m.find()) {
      return m.group(1);
    }
    return "";
  }

  /**
   * Extracts a JSON array from a JSON object.
   *
   * @param json the JSON string
   * @param key the array key
   * @return the list of values in the array
   */
  private List<String> extractJsonArray(String json, String key) {
    List<String> result = new ArrayList<>();
    String pattern = "\"" + key + "\"\\s*:\\s*\\[([^\\]]*?)\\]";
    Pattern p = Pattern.compile(pattern);
    Matcher m = p.matcher(json);

    if (m.find()) {
      String arrayContent = m.group(1);
      String[] items = arrayContent.split(",");
      for (String item : items) {
        item = item.replaceAll("[\"\\s]", "").trim();
        if (!item.isEmpty()) {
          result.add(item);
        }
      }
    }
    return result;
  }
}

