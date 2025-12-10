package com.quizmaker.service.file;

import com.quizmaker.model.QuestionBank;
import java.io.IOException;

/**
 * Interface for reading questions from files.
 *
 * Implementations can support different file formats (CSV, JSON, XML, etc.).
 * This follows the Open/Closed Principle - new formats can be added
 * without modifying existing code.
 *
 * @author Developer Team
 * @version 2.0
 * @since 2025-12-10
 */
public interface QuestionFileReader {

  /**
   * Reads questions from a file.
   *
   * @param filePath path to the file to read
   * @return QuestionBank containing loaded questions
   * @throws IOException if file cannot be read or parsed
   */
  QuestionBank read(String filePath) throws IOException;

  /**
   * Returns the supported file format.
   *
   * @return format name (e.g., "CSV", "JSON")
   */
  String getFormat();
}

