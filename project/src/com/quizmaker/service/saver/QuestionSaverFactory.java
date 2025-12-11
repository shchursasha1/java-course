package com.quizmaker.service.saver;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory for creating appropriate QuestionSaver instances.
 *
 * This class implements the Factory pattern and follows the Open/Closed Principle.
 * New savers can be registered without modifying the factory's core logic.
 *
 * @author Oleksandr Shchur
 * @version 1.0
 * @since 28.11.2025
 */
public class QuestionSaverFactory {

  private static final Map<String, QuestionSaver> savers = new HashMap<>();

  static {
    // Register built-in savers
    registerSaver(new CsvQuestionSaver());
    registerSaver(new JsonQuestionSaver());
  }

  /**
   * Registers a new question saver.
   *
   * This method allows extending the factory with new formats without
   * modifying existing code (Open/Closed Principle).
   *
   * @param saver the QuestionSaver to register
   */
  public static void registerSaver(QuestionSaver saver) {
    savers.put(saver.getSupportedFormat().toUpperCase(), saver);
  }

  /**
   * Gets a saver for the specified format.
   *
   * @param format the file format (e.g., "CSV", "JSON")
   * @return the appropriate QuestionSaver
   * @throws UnsupportedFormatException if the format is not supported
   */
  public static QuestionSaver getSaver(String format) throws UnsupportedFormatException {
    QuestionSaver saver = savers.get(format.toUpperCase());
    if (saver == null) {
      throw new UnsupportedFormatException("Unsupported format: " + format);
    }
    return saver;
  }

  /**
   * Gets a saver based on file extension.
   *
   * @param filePath the file path
   * @return the appropriate QuestionSaver
   * @throws UnsupportedFormatException if the format cannot be determined
   */
  public static QuestionSaver getSaverByFilePath(String filePath)
      throws UnsupportedFormatException {
    String extension = getFileExtension(filePath);
    return getSaver(extension);
  }

  /**
   * Checks if a format is supported.
   *
   * @param format the file format to check
   * @return true if the format is supported
   */
  public static boolean isFormatSupported(String format) {
    return savers.containsKey(format.toUpperCase());
  }

  /**
   * Gets all supported formats.
   *
   * @return array of supported format names
   */
  public static String[] getSupportedFormats() {
    return savers.keySet().toArray(new String[0]);
  }

  /**
   * Extracts the file extension from a file path.
   *
   * @param filePath the file path
   * @return the file extension in uppercase
   */
  private static String getFileExtension(String filePath) {
    int lastDotIndex = filePath.lastIndexOf('.');
    if (lastDotIndex > 0 && lastDotIndex < filePath.length() - 1) {
      return filePath.substring(lastDotIndex + 1).toUpperCase();
    }
    return "";
  }

  /**
   * Exception thrown when an unsupported format is requested.
   */
  public static class UnsupportedFormatException extends Exception {

    public UnsupportedFormatException(String message) {
      super(message);
    }
  }
}

