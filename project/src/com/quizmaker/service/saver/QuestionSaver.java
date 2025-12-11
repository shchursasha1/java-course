package com.quizmaker.service.saver;

import com.quizmaker.model.QuestionBank;
import java.io.IOException;

/**
 * Interface for saving questions to various file formats.
 *
 * This interface follows the Open/Closed Principle - open for extension,
 * closed for modification. New file formats can be added by implementing
 * this interface without changing existing code.
 *
 * @author Oleksandr Shchur
 * @version 1.0
 * @since 28.11.2025
 */
public interface QuestionSaver {

  /**
   * Saves questions from a QuestionBank to a file.
   *
   * @param filePath path where to save the file
   * @param bank the QuestionBank to save
   * @throws IOException if the file cannot be written
   */
  void save(String filePath, QuestionBank bank) throws IOException;

  /**
   * Gets the supported file format for this saver.
   *
   * @return the file format (e.g., "CSV", "JSON", "XML")
   */
  String getSupportedFormat();
}

