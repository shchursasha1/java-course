package com.quizmaker.service.file;

import com.quizmaker.model.Question;
import com.quizmaker.model.QuestionBank;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * CSV implementation of QuestionFileWriter.
 *
 * Writes questions to CSV files with format:
 * id,question_text,answer1,answer2,answer3,answer4,correct_index,category,difficulty
 *
 * @author Oleksandr Shchur
 * @version 2.0
 * @since 28.11.2025
 */
public class CsvQuestionFileWriter implements QuestionFileWriter {

  private static final String FORMAT = "CSV";
  private static final String HEADER =
      "id,question_text,answer1,answer2,answer3,answer4,correct_index,category,difficulty";

  @Override
  public void write(String filePath, QuestionBank questionBank) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      writer.write(HEADER);
      writer.newLine();

      for (Question question : questionBank.getAllQuestions()) {
        writer.write(formatQuestionAsCsv(question));
        writer.newLine();
      }
    } catch (IOException e) {
      throw new IOException("Failed to write CSV file: " + filePath, e);
    }
  }

  @Override
  public String getFormat() {
    return FORMAT;
  }

  /**
   * Formats a Question object as a CSV line.
   *
   * @param question the Question to format
   * @return CSV formatted string
   */
  private String formatQuestionAsCsv(Question question) {
    StringBuilder line = new StringBuilder();
    
    line.append(question.getId()).append(",");
    line.append("\"").append(escapeQuotes(question.getQuestionText())).append("\",");
    
    List<String> answers = question.getAnswers();
    for (String answer : answers) {
      line.append("\"").append(escapeQuotes(answer)).append("\",");
    }
    
    line.append(question.getCorrectAnswerIndex()).append(",");
    line.append(question.getCategory()).append(",");
    line.append(question.getDifficulty());
    
    return line.toString();
  }

  /**
   * Escapes quotes in text for CSV format.
   *
   * @param text the text to escape
   * @return escaped text
   */
  private String escapeQuotes(String text) {
    return text.replace("\"", "\"\"");
  }
}

