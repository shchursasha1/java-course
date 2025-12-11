package com.quizmaker.service.saver;

import com.quizmaker.model.Question;
import com.quizmaker.model.QuestionBank;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Saves questions to JSON files.
 *
 * This class implements the QuestionSaver interface, following the Open/Closed
 * and Single Responsibility principles.
 *
 * @author Oleksandr Shchur
 * @version 1.0
 * @since 28.11.2025
 */
public class JsonQuestionSaver implements QuestionSaver {

  private static final String FORMAT = "JSON";

  /**
   * Saves questions to a JSON file.
   *
   * @param filePath path where to save the JSON file
   * @param bank the QuestionBank to save
   * @throws IOException if the file cannot be written
   */
  @Override
  public void save(String filePath, QuestionBank bank) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      writer.write("[\n");

      List<Question> questions = bank.getAllQuestions();
      for (int i = 0; i < questions.size(); i++) {
        String questionJson = formatQuestionAsJson(questions.get(i), i == questions.size() - 1);
        writer.write(questionJson);
      }

      writer.write("]");
    } catch (IOException e) {
      throw new IOException("Failed to write JSON file: " + filePath, e);
    }
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
   * Formats a Question as a JSON object.
   *
   * @param question the Question to format
   * @param isLast whether this is the last question (affects comma)
   * @return a JSON formatted string
   */
  private String formatQuestionAsJson(Question question, boolean isLast) {
    StringBuilder json = new StringBuilder();
    json.append("  {\n");
    json.append("    \"id\": \"").append(question.getId()).append("\",\n");
    json.append("    \"questionText\": \"")
        .append(escapeJson(question.getQuestionText()))
        .append("\",\n");
    json.append("    \"answers\": [\n");

    List<String> answers = question.getAnswers();
    for (int j = 0; j < answers.size(); j++) {
      json.append("      \"").append(escapeJson(answers.get(j))).append("\"");
      if (j < answers.size() - 1) {
        json.append(",");
      }
      json.append("\n");
    }

    json.append("    ],\n");
    json.append("    \"correctAnswerIndex\": ").append(question.getCorrectAnswerIndex())
        .append(",\n");
    json.append("    \"category\": \"").append(question.getCategory()).append("\",\n");
    json.append("    \"difficulty\": ").append(question.getDifficulty()).append("\n");
    json.append("  }");

    if (!isLast) {
      json.append(",");
    }
    json.append("\n");

    return json.toString();
  }

  /**
   * Escapes special characters for JSON strings.
   *
   * @param text the text to escape
   * @return the escaped text
   */
  private String escapeJson(String text) {
    return text.replaceAll("\\\\", "\\\\\\\\")
        .replaceAll("\"", "\\\\\"")
        .replaceAll("\n", "\\\\n")
        .replaceAll("\r", "\\\\r");
  }
}

