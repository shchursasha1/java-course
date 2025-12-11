package com.quizmaker.service.loader;

import com.quizmaker.model.QuestionBank;
import java.io.IOException;

/**
 * Interface for loading questions from various file formats.
 *
 * This interface follows the Open/Closed Principle - open for extension,
 * closed for modification. New file formats can be added by implementing
 * this interface without changing existing code.
 *
 * @author Oleksandr Shchur
 * @version 1.0
 * @since 28.11.2025
 */
public interface QuestionLoader {

  /**
   * Loads questions from a file into a QuestionBank.
   *
   * @param filePath path to the file to load
   * @return a QuestionBank containing loaded questions
   * @throws IOException if the file cannot be read or parsed
   */
  QuestionBank load(String filePath) throws IOException;

  /**
   * Gets the supported file format for this loader.
   *
   * @return the file format (e.g., "CSV", "JSON", "XML")
   */
  String getSupportedFormat();
}

