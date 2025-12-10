package com.quizmaker.service;

import com.quizmaker.model.QuestionBank;
import com.quizmaker.model.Quiz;
import java.io.IOException;

/**
 * Simplified facade for file operations.
 *
 * Delegates all work to QuestionFileService which follows SOLID principles.
 * This class exists for convenience and maintains a simple API.
 *
 * @author Developer Team
 * @version 2.0
 * @since 2025-12-10
 */
public class FileManager {

  private static final QuestionFileService fileService = new QuestionFileService();

  /**
   * Loads questions from a CSV file.
   *
   * @param filePath path to the CSV file
   * @return a QuestionBank containing loaded questions
   * @throws IOException if file cannot be read
   */
  public static QuestionBank loadFromCSV(String filePath) throws IOException {
    return fileService.loadQuestions(filePath, "CSV");
  }

  /**
   * Saves questions to a CSV file.
   *
   * @param filePath path where to save the CSV file
   * @param bank the QuestionBank to save
   * @throws IOException if file cannot be written
   */
  public static void saveToCSV(String filePath, QuestionBank bank) throws IOException {
    fileService.saveQuestions(filePath, bank, "CSV");
  }

  /**
   * Loads questions from a JSON file.
   *
   * @param filePath path to the JSON file
   * @return a QuestionBank containing loaded questions
   * @throws IOException if file cannot be read
   */
  public static QuestionBank loadFromJSON(String filePath) throws IOException {
    return fileService.loadQuestions(filePath, "JSON");
  }

  /**
   * Saves questions to a JSON file.
   *
   * @param filePath path where to save the JSON file
   * @param bank the QuestionBank to save
   * @throws IOException if file cannot be written
   */
  public static void saveToJSON(String filePath, QuestionBank bank) throws IOException {
    fileService.saveQuestions(filePath, bank, "JSON");
  }

  /**
   * Saves quiz results to a text file.
   *
   * @param filePath path where to save the results file
   * @param quiz the Quiz object to save
   * @throws IOException if file cannot be written
   */
  public static void saveQuizResults(String filePath, Quiz quiz) throws IOException {
    fileService.saveQuizResults(filePath, quiz);
  }

  /**
   * Gets the underlying file service for advanced usage.
   *
   * @return the QuestionFileService instance
   */
  public static QuestionFileService getFileService() {
    return fileService;
  }
}
