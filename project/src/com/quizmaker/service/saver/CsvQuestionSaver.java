package com.quizmaker.service.saver;

import com.quizmaker.model.Question;
import com.quizmaker.model.QuestionBank;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Saves questions to CSV files.
 *
 * This class implements the QuestionSaver interface, following the Open/Closed
 * and Single Responsibility principles.
 *
 * @author Oleksandr Shchur
 * @version 1.0
 * @since 28.11.2025
 */
public class CsvQuestionSaver implements QuestionSaver {

  private static final String FORMAT = "CSV";

  /**
   * Saves questions to a CSV file.
   *
   * @param filePath path where to save the CSV file
   * @param bank the QuestionBank to save
   * @throws IOException if the file cannot be written
   */
  @Override
  public void save(String filePath, QuestionBank bank) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      // Write header
      writer.write(
          "id,question_text,answer1,answer2,answer3,answer4,correct_index,category,difficulty\n");

      // Write questions
      for (Question question : bank.getAllQuestions()) {
        String line = formatQuestionAsCsv(question);
        writer.write(line);
        writer.newLine();
      }
    } catch (IOException e) {
      throw new IOException("Failed to write CSV file: " + filePath, e);
    }
  }

  /**
   * Gets the supported file format.
   *
   * @return "CSV"
   */
  @Override
  public String getSupportedFormat() {
    return FORMAT;
  }

  /**
   * Formats a Question as a CSV line.
   *
   * @param question the Question to format
   * @return a CSV formatted string
   */
  private String formatQuestionAsCsv(Question question) {
    List<String> answers = question.getAnswers();
    StringBuilder line = new StringBuilder();

    line.append(question.getId()).append(",");
    line.append("\"").append(escapeQuotes(question.getQuestionText())).append("\",");

    for (String answer : answers) {
      line.append("\"").append(escapeQuotes(answer)).append("\",");
    }

    line.append(question.getCorrectAnswerIndex()).append(",");
    line.append(question.getCategory()).append(",");
    line.append(question.getDifficulty());

    return line.toString();
  }

  /**
   * Escapes quotes in a string for CSV format.
   *
   * @param text the text to escape
   * @return the escaped text
   */
  private String escapeQuotes(String text) {
    return text.replaceAll("\"", "\"\"");
  }
}

