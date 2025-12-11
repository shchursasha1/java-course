package com.quizmaker.service.file;

import com.quizmaker.model.Question;
import com.quizmaker.model.QuestionBank;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV implementation of QuestionFileReader.
 *
 * Reads questions from CSV files with format:
 * id,question_text,answer1,answer2,answer3,answer4,correct_index,category,difficulty
 *
 * @author Oleksandr Shchur
 * @version 2.0
 * @since 28.11.2025
 */
public class CsvQuestionFileReader implements QuestionFileReader {

  private static final String FORMAT = "CSV";

  @Override
  public QuestionBank read(String filePath) throws IOException {
    QuestionBank bank = new QuestionBank();

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      boolean isHeader = true;

      while ((line = reader.readLine()) != null) {
        if (isHeader) {
          isHeader = false;
          continue;
        }

        Question question = parseCsvLine(line);
        if (question != null) {
          bank.addQuestion(question);
        }
      }
    } catch (IOException e) {
      throw new IOException("Failed to read CSV file: " + filePath, e);
    }

    return bank;
  }

  @Override
  public String getFormat() {
    return FORMAT;
  }

  /**
   * Parses a single CSV line into a Question object.
   *
   * @param line the CSV line to parse
   * @return Question object or null if parsing fails
   */
  private Question parseCsvLine(String line) {
    try {
      String[] parts = line.split(",");
      if (parts.length < 9) {
        System.err.println("Skipping invalid line: " + line);
        return null;
      }

      String id = parts[0].trim();
      String questionText = removeQuotes(parts[1].trim());
      
      List<String> answers = new ArrayList<>();
      for (int i = 2; i <= 5; i++) {
        answers.add(removeQuotes(parts[i].trim()));
      }

      int correctIndex = Integer.parseInt(parts[6].trim());
      String category = parts[7].trim();
      int difficulty = Integer.parseInt(parts[8].trim());

      return new Question(id, questionText, answers, correctIndex, category, difficulty);
    } catch (NumberFormatException e) {
      System.err.println("Skipping invalid line: " + line);
      return null;
    }
  }

  /**
   * Removes surrounding quotes from a string.
   *
   * @param text the text to process
   * @return text without surrounding quotes
   */
  private String removeQuotes(String text) {
    if (text.startsWith("\"") && text.endsWith("\"")) {
      return text.substring(1, text.length() - 1);
    }
    return text;
  }
}

