package com.quizmaker.service;

import com.quizmaker.model.QuestionBank;
import com.quizmaker.model.Quiz;
import com.quizmaker.service.file.CsvQuestionFileReader;
import com.quizmaker.service.file.CsvQuestionFileWriter;
import com.quizmaker.service.file.JsonQuestionFileReader;
import com.quizmaker.service.file.JsonQuestionFileWriter;
import com.quizmaker.service.file.QuestionFileReader;
import com.quizmaker.service.file.QuestionFileWriter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for managing question file operations.
 *
 * This class follows SOLID principles:
 * - Single Responsibility: Manages file operations coordination
 * - Open/Closed: New formats can be added by registering new readers/writers
 * - Liskov Substitution: All readers/writers are interchangeable via interfaces
 * - Interface Segregation: Separate interfaces for reading and writing
 * - Dependency Inversion: Depends on abstractions (interfaces), not concrete classes
 *
 * @author Oleksandr Shchur
 * @version 2.0
 * @since 28.11.2025
 */
public class QuestionFileService {

  private final Map<String, QuestionFileReader> readers;
  private final Map<String, QuestionFileWriter> writers;

  /**
   * Constructs a QuestionFileService with default readers and writers.
   */
  public QuestionFileService() {
    this.readers = new HashMap<>();
    this.writers = new HashMap<>();
    
    // Register default implementations
    registerReader(new CsvQuestionFileReader());
    registerReader(new JsonQuestionFileReader());
    registerWriter(new CsvQuestionFileWriter());
    registerWriter(new JsonQuestionFileWriter());
  }

  /**
   * Registers a new file reader.
   *
   * This allows adding new file formats without modifying this class (Open/Closed Principle).
   *
   * @param reader the QuestionFileReader to register
   */
  public void registerReader(QuestionFileReader reader) {
    readers.put(reader.getFormat().toUpperCase(), reader);
  }

  /**
   * Registers a new file writer.
   *
   * This allows adding new file formats without modifying this class (Open/Closed Principle).
   *
   * @param writer the QuestionFileWriter to register
   */
  public void registerWriter(QuestionFileWriter writer) {
    writers.put(writer.getFormat().toUpperCase(), writer);
  }

  /**
   * Loads questions from a file based on format.
   *
   * @param filePath path to the file
   * @param format file format (CSV, JSON, etc.)
   * @return QuestionBank containing loaded questions
   * @throws IOException if file cannot be read or format is not supported
   */
  public QuestionBank loadQuestions(String filePath, String format) throws IOException {
    QuestionFileReader reader = readers.get(format.toUpperCase());
    
    if (reader == null) {
      throw new IOException("Unsupported file format: " + format + 
          ". Supported formats: " + readers.keySet());
    }
    
    return reader.read(filePath);
  }

  /**
   * Saves questions to a file based on format.
   *
   * @param filePath path where to save the file
   * @param questionBank the QuestionBank to save
   * @param format file format (CSV, JSON, etc.)
   * @throws IOException if file cannot be written or format is not supported
   */
  public void saveQuestions(String filePath, QuestionBank questionBank, String format) 
      throws IOException {
    QuestionFileWriter writer = writers.get(format.toUpperCase());
    
    if (writer == null) {
      throw new IOException("Unsupported file format: " + format + 
          ". Supported formats: " + writers.keySet());
    }
    
    writer.write(filePath, questionBank);
  }

  /**
   * Auto-detects file format from file extension and loads questions.
   *
   * @param filePath path to the file
   * @return QuestionBank containing loaded questions
   * @throws IOException if file cannot be read or format cannot be detected
   */
  public QuestionBank loadQuestions(String filePath) throws IOException {
    String format = detectFormat(filePath);
    return loadQuestions(filePath, format);
  }

  /**
   * Auto-detects file format from file extension and saves questions.
   *
   * @param filePath path where to save the file
   * @param questionBank the QuestionBank to save
   * @throws IOException if file cannot be written or format cannot be detected
   */
  public void saveQuestions(String filePath, QuestionBank questionBank) throws IOException {
    String format = detectFormat(filePath);
    saveQuestions(filePath, questionBank, format);
  }

  /**
   * Saves quiz results to a text file.
   *
   * @param filePath path where to save the results file
   * @param quiz the Quiz object to save
   * @throws IOException if file cannot be written
   */
  public void saveQuizResults(String filePath, Quiz quiz) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
      writer.write(quiz.toString());
      writer.write("\n");
      writer.write("Answers:\n");

      List<com.quizmaker.model.Question> questions = quiz.getQuestions();
      List<Integer> answers = quiz.getStudentAnswers();

      for (int i = 0; i < questions.size(); i++) {
        com.quizmaker.model.Question question = questions.get(i);
        writer.write("Q" + (i + 1) + ": " + question.getQuestionText() + "\n");

        if (i < answers.size()) {
          int studentAnswerIndex = answers.get(i);
          if (studentAnswerIndex >= 0 && studentAnswerIndex < question.getAnswers().size()) {
            String studentAnswer = question.getAnswers().get(studentAnswerIndex);
            writer.write("  Student Answer: " + studentAnswer + "\n");
          } else {
            writer.write("  Student Answer: NOT ANSWERED\n");
          }
        }

        writer.write("  Correct Answer: " + question.getCorrectAnswer() + "\n\n");
      }

      writer.write("=====================================\n\n");
    } catch (IOException e) {
      throw new IOException("Failed to save quiz results: " + filePath, e);
    }
  }

  /**
   * Detects file format from file extension.
   *
   * @param filePath the file path
   * @return the detected format
   * @throws IOException if format cannot be detected
   */
  private String detectFormat(String filePath) throws IOException {
    String lowerPath = filePath.toLowerCase();
    
    if (lowerPath.endsWith(".csv")) {
      return "CSV";
    } else if (lowerPath.endsWith(".json")) {
      return "JSON";
    }
    
    throw new IOException("Cannot detect file format from extension: " + filePath);
  }

  /**
   * Gets all supported read formats.
   *
   * @return list of supported formats for reading
   */
  public List<String> getSupportedReadFormats() {
    return new java.util.ArrayList<>(readers.keySet());
  }

  /**
   * Gets all supported write formats.
   *
   * @return list of supported formats for writing
   */
  public List<String> getSupportedWriteFormats() {
    return new java.util.ArrayList<>(writers.keySet());
  }
}
