package com.quizmaker.service.loader;

import com.quizmaker.model.Question;
import com.quizmaker.model.QuestionBank;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Loads questions from CSV files.
 *
 * CSV format: id,question_text,answer1,answer2,answer3,answer4,correct_index,category,difficulty
 *
 * This class implements the QuestionLoader interface, following the Open/Closed
 * and Single Responsibility principles.
 *
 * @author Developer Team
 * @version 1.0
 * @since 2025-12-10
 */
public class CsvQuestionLoader implements QuestionLoader {

  private static final String FORMAT = "CSV";
  private static final int EXPECTED_FIELDS = 9;

  /**
   * Loads questions from a CSV file.
   *
   * @param filePath path to the CSV file
   * @return a QuestionBank containing loaded questions
   * @throws IOException if the file cannot be read or is invalid
   */
  @Override
  public QuestionBank load(String filePath) throws IOException {
    QuestionBank bank = new QuestionBank();

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      boolean isHeader = true;

      while ((line = reader.readLine()) != null) {
        if (isHeader) {
          isHeader = false;
          continue;
        }

        Question question = parseLine(line);
        if (question != null) {
          bank.addQuestion(question);
        }
      }
    } catch (IOException e) {
      throw new IOException("Failed to load CSV file: " + filePath, e);
    }

    return bank;
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
   * Parses a single CSV line into a Question.
   *
   * @param line the CSV line to parse
   * @return a Question object, or null if the line is invalid
   */
  private Question parseLine(String line) {
    String[] parts = line.split(",");
    if (parts.length < EXPECTED_FIELDS) {
      System.err.println("Skipping invalid CSV line: " + line);
      return null;
    }

    try {
      String id = parts[0].trim();
      String questionText = parts[1].trim().replaceAll("^\"|\"$", "");

      List<String> answers = new ArrayList<>();
      for (int i = 2; i <= 5; i++) {
        answers.add(parts[i].trim().replaceAll("^\"|\"$", ""));
      }

      int correctIndex = Integer.parseInt(parts[6].trim());
      String category = parts[7].trim();
      int difficulty = Integer.parseInt(parts[8].trim());

      return new Question(id, questionText, answers, correctIndex, category, difficulty);
    } catch (NumberFormatException e) {
      System.err.println("Skipping line with invalid numbers: " + line);
      return null;
    }
  }
}

