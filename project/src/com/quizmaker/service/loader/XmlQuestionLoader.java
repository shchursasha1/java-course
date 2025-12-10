package com.quizmaker.service.loader;

import com.quizmaker.model.Question;
import com.quizmaker.model.QuestionBank;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Loads questions from XML files.
 *
 * This class demonstrates the extensibility of the loader architecture.
 * It was added WITHOUT modifying any existing code, following the
 * Open/Closed Principle.
 *
 * Example XML format:
 * <pre>
 * &lt;questions&gt;
 *   &lt;question id="Q001"&gt;
 *     &lt;text&gt;What is the capital of France?&lt;/text&gt;
 *     &lt;answers&gt;
 *       &lt;answer&gt;Berlin&lt;/answer&gt;
 *       &lt;answer correct="true"&gt;Paris&lt;/answer&gt;
 *       &lt;answer&gt;Madrid&lt;/answer&gt;
 *       &lt;answer&gt;Rome&lt;/answer&gt;
 *     &lt;/answers&gt;
 *     &lt;category&gt;Geography&lt;/category&gt;
 *     &lt;difficulty&gt;1&lt;/difficulty&gt;
 *   &lt;/question&gt;
 * &lt;/questions&gt;
 * </pre>
 *
 * @author Developer Team
 * @version 1.0
 * @since 2025-12-10
 */
public class XmlQuestionLoader implements QuestionLoader {

  private static final String FORMAT = "XML";

  /**
   * Loads questions from an XML file.
   *
   * @param filePath path to the XML file
   * @return a QuestionBank containing loaded questions
   * @throws IOException if the file cannot be read or parsed
   */
  @Override
  public QuestionBank load(String filePath) throws IOException {
    QuestionBank bank = new QuestionBank();

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      StringBuilder xmlContent = new StringBuilder();
      String line;

      while ((line = reader.readLine()) != null) {
        xmlContent.append(line).append("\n");
      }

      String xml = xmlContent.toString();
      List<String> questionXmls = extractQuestions(xml);

      for (String questionXml : questionXmls) {
        Question question = parseQuestion(questionXml);
        if (question != null) {
          bank.addQuestion(question);
        }
      }
    } catch (IOException e) {
      throw new IOException("Failed to load XML file: " + filePath, e);
    }

    return bank;
  }

  /**
   * Gets the supported file format.
   *
   * @return "XML"
   */
  @Override
  public String getSupportedFormat() {
    return FORMAT;
  }

  /**
   * Extracts individual question XML blocks.
   *
   * @param xml the full XML content
   * @return list of question XML strings
   */
  private List<String> extractQuestions(String xml) {
    List<String> questions = new ArrayList<>();
    Pattern pattern = Pattern.compile("<question[^>]*>.*?</question>", Pattern.DOTALL);
    Matcher matcher = pattern.matcher(xml);

    while (matcher.find()) {
      questions.add(matcher.group());
    }

    return questions;
  }

  /**
   * Parses a single question XML block.
   *
   * @param questionXml the question XML string
   * @return a Question object, or null if parsing fails
   */
  private Question parseQuestion(String questionXml) {
    try {
      String id = extractAttribute(questionXml, "question", "id");
      String questionText = extractTagContent(questionXml, "text");
      String category = extractTagContent(questionXml, "category");
      int difficulty = Integer.parseInt(extractTagContent(questionXml, "difficulty"));

      List<String> answers = extractAnswers(questionXml);
      int correctIndex = findCorrectAnswerIndex(questionXml);

      return new Question(id, questionText, answers, correctIndex, category, difficulty);
    } catch (NumberFormatException e) {
      System.err.println("Error parsing XML question: " + e.getMessage());
      return null;
    }
  }

  /**
   * Extracts attribute value from an XML tag.
   *
   * @param xml the XML string
   * @param tag the tag name
   * @param attribute the attribute name
   * @return the attribute value
   */
  private String extractAttribute(String xml, String tag, String attribute) {
    Pattern pattern =
        Pattern.compile("<" + tag + "[^>]*" + attribute + "=\"([^\"]+)\"", Pattern.DOTALL);
    Matcher matcher = pattern.matcher(xml);
    return matcher.find() ? matcher.group(1) : "";
  }

  /**
   * Extracts content from an XML tag.
   *
   * @param xml the XML string
   * @param tag the tag name
   * @return the tag content
   */
  private String extractTagContent(String xml, String tag) {
    Pattern pattern = Pattern.compile("<" + tag + ">([^<]+)</" + tag + ">", Pattern.DOTALL);
    Matcher matcher = pattern.matcher(xml);
    return matcher.find() ? matcher.group(1).trim() : "";
  }

  /**
   * Extracts all answers from the answers section.
   *
   * @param questionXml the question XML string
   * @return list of answer texts
   */
  private List<String> extractAnswers(String questionXml) {
    List<String> answers = new ArrayList<>();
    Pattern pattern = Pattern.compile("<answer[^>]*>([^<]+)</answer>", Pattern.DOTALL);
    Matcher matcher = pattern.matcher(questionXml);

    while (matcher.find()) {
      answers.add(matcher.group(1).trim());
    }

    return answers;
  }

  /**
   * Finds the index of the correct answer.
   *
   * @param questionXml the question XML string
   * @return the index of the correct answer (0-based)
   */
  private int findCorrectAnswerIndex(String questionXml) {
    Pattern pattern = Pattern.compile("<answer[^>]*correct=\"true\"[^>]*>", Pattern.DOTALL);
    Matcher matcher = pattern.matcher(questionXml);

    int index = 0;
    int currentIndex = 0;

    Pattern answerPattern = Pattern.compile("<answer[^>]*>", Pattern.DOTALL);
    Matcher answerMatcher = answerPattern.matcher(questionXml);

    while (answerMatcher.find()) {
      if (answerMatcher.group().contains("correct=\"true\"")) {
        index = currentIndex;
        break;
      }
      currentIndex++;
    }

    return index;
  }
}

