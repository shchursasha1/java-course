package com.quizmaker.service.file;

import com.quizmaker.model.QuestionBank;
import java.io.IOException;

/**
 * Interface for writing questions to files.
 *
 * Implementations can support different file formats (CSV, JSON, XML, etc.).
 * This follows the Open/Closed Principle - new formats can be added
 * without modifying existing code.
 *
 * @author Oleksandr Shchur
 * @version 2.0
 * @since 28.11.2025
 */
public interface QuestionFileWriter {

  /**
   * Writes questions to a file.
   *
   * @param filePath path where to save the file
   * @param questionBank the QuestionBank to save
   * @throws IOException if file cannot be written
   */
  void write(String filePath, QuestionBank questionBank) throws IOException;

  /**
   * Returns the supported file format.
   *
   * @return format name (e.g., "CSV", "JSON")
   */
  String getFormat();
}

