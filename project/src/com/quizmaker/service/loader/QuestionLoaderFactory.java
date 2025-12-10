package com.quizmaker.service.loader;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory for creating appropriate QuestionLoader instances.
 *
 * This class implements the Factory pattern and follows the Open/Closed Principle.
 * New loaders can be registered without modifying the factory's core logic.
 *
 * @author Developer Team
 * @version 1.0
 * @since 2025-12-10
 */
public class QuestionLoaderFactory {

  private static final Map<String, QuestionLoader> loaders = new HashMap<>();

  static {
    // Register built-in loaders
    registerLoader(new CsvQuestionLoader());
    registerLoader(new JsonQuestionLoader());
  }

  /**
   * Registers a new question loader.
   *
   * This method allows extending the factory with new formats without
   * modifying existing code (Open/Closed Principle).
   *
   * @param loader the QuestionLoader to register
   */
  public static void registerLoader(QuestionLoader loader) {
    loaders.put(loader.getSupportedFormat().toUpperCase(), loader);
  }

  /**
   * Gets a loader for the specified format.
   *
   * @param format the file format (e.g., "CSV", "JSON")
   * @return the appropriate QuestionLoader
   * @throws UnsupportedFormatException if the format is not supported
   */
  public static QuestionLoader getLoader(String format) throws UnsupportedFormatException {
    QuestionLoader loader = loaders.get(format.toUpperCase());
    if (loader == null) {
      throw new UnsupportedFormatException("Unsupported format: " + format);
    }
    return loader;
  }

  /**
   * Gets a loader based on file extension.
   *
   * @param filePath the file path
   * @return the appropriate QuestionLoader
   * @throws UnsupportedFormatException if the format cannot be determined
   */
  public static QuestionLoader getLoaderByFilePath(String filePath)
      throws UnsupportedFormatException {
    String extension = getFileExtension(filePath);
    return getLoader(extension);
  }

  /**
   * Checks if a format is supported.
   *
   * @param format the file format to check
   * @return true if the format is supported
   */
  public static boolean isFormatSupported(String format) {
    return loaders.containsKey(format.toUpperCase());
  }

  /**
   * Gets all supported formats.
   *
   * @return array of supported format names
   */
  public static String[] getSupportedFormats() {
    return loaders.keySet().toArray(new String[0]);
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

