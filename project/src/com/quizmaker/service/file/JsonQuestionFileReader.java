package com.quizmaker.service.file;

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
 * JSON implementation of QuestionFileReader.
 *
 * Reads questions from JSON files using regex parsing.
 * Format: Array of objects with id, questionText, answers, correctAnswerIndex, category, difficulty
 *
 * @author Oleksandr Shchur
 * @version 2.0
 * @since 28.11.2025
 */
public class JsonQuestionFileReader implements QuestionFileReader {

  private static final String FORMAT = "JSON";

  @Override
  public QuestionBank read(String filePath) throws IOException {
    QuestionBank bank = new QuestionBank();

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      StringBuilder jsonContent = new StringBuilder();
      String line;

      while ((line = reader.readLine()) != null) {
        jsonContent.append(line);
      }

      String json = jsonContent.toString().trim();
      
      // Remove only the outermost array brackets
      if (json.startsWith("[")) {
        json = json.substring(1);
      }
      if (json.endsWith("]")) {
        json = json.substring(0, json.length() - 1);
      }
      json = json.trim();

      if (json.isEmpty()) {
        return bank;
      }

      String[] objects = json.split("\\},\\s*\\{");

      for (String obj : objects) {
        obj = obj.replaceAll("[\\{\\}]", "").trim();
        Question question = parseJsonObject(obj);
        if (question != null) {
          bank.addQuestion(question);
        }
      }
    } catch (IOException e) {
      throw new IOException("Failed to read JSON file: " + filePath, e);
    }

    return bank;
  }

  @Override
  public String getFormat() {
    return FORMAT;
  }

  /**
   * Parses a single JSON object string into a Question.
   *
   * @param jsonObject the JSON object string
   * @return the parsed Question or null if parsing fails
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
    } catch (Exception e) {
      System.err.println("Error parsing JSON object: " + e.getMessage());
      return null;
    }
  }

  /**
   * Extracts a string value from JSON.
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

    // Try numeric value
    pattern = "\"" + key + "\"\\s*:\\s*(\\d+)";
    p = Pattern.compile(pattern);
    m = p.matcher(json);
    if (m.find()) {
      return m.group(1);
    }

    return "";
  }

  /**
   * Extracts a JSON array from JSON.
   *
   * @param json the JSON string
   * @param key the array key
   * @return the list of values in the array
   */
  private List<String> extractJsonArray(String json, String key) {
    List<String> result = new ArrayList<>();
    String pattern = "\"" + key + "\"\\s*:\\s*\\[([^\\]]*?)\\]";
    Pattern p = Pattern.compile(pattern, Pattern.DOTALL);
    Matcher m = p.matcher(json);

    if (m.find()) {
      String arrayContent = m.group(1);
      // Split by comma that is followed by optional whitespace and a quote
      String[] items = arrayContent.split("\"\\s*,\\s*\"");
      for (String item : items) {
        item = item.replaceAll("\"", "").trim();
        if (!item.isEmpty()) {
          result.add(item);
        }
      }
    }
    return result;
  }
}

