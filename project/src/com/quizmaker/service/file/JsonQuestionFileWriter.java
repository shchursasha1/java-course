package com.quizmaker.service.file;

import com.quizmaker.model.Question;
import com.quizmaker.model.QuestionBank;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * JSON implementation of QuestionFileWriter.
 *
 * Writes questions to JSON files as an array of objects.
 *
 * @author Developer Team
 * @version 2.0
 * @since 2025-12-10
 */
public class JsonQuestionFileWriter implements QuestionFileWriter {

  private static final String FORMAT = "JSON";

  @Override
  public void write(String filePath, QuestionBank questionBank) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      writer.write("[\n");

      List<Question> questions = questionBank.getAllQuestions();
      for (int i = 0; i < questions.size(); i++) {
        Question question = questions.get(i);
        writer.write(formatQuestionAsJson(question, i == questions.size() - 1));
      }

      writer.write("]");
    } catch (IOException e) {
      throw new IOException("Failed to write JSON file: " + filePath, e);
    }
  }

  @Override
  public String getFormat() {
    return FORMAT;
  }

  /**
   * Formats a Question object as JSON.
   *
   * @param question the Question to format
   * @param isLast whether this is the last question in the array
   * @return JSON formatted string
   */
  private String formatQuestionAsJson(Question question, boolean isLast) {
    StringBuilder json = new StringBuilder();
    
    json.append("  {\n");
    json.append("    \"id\": \"").append(question.getId()).append("\",\n");
    json.append("    \"questionText\": \"").append(escapeJson(question.getQuestionText()))
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

