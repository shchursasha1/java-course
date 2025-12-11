package com.quizmaker.service.file;

import com.quizmaker.model.Question;
import com.quizmaker.model.QuestionBank;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * XML implementation of QuestionFileReader.
 *
 * This is an example implementation showing how easily new formats
 * can be added without modifying existing code (Open/Closed Principle).
 *
 * NOTE: This is a simplified example. Production code should use
 * a proper XML library like DOM or SAX.
 *
 * @author Oleksandr Shchur
 * @version 2.0
 * @since 28.11.2025
 */
public class XmlQuestionFileReader implements QuestionFileReader {

  private static final String FORMAT = "XML";

  @Override
  public QuestionBank read(String filePath) throws IOException {
    // This is a placeholder implementation
    // In production, use javax.xml.parsers.DocumentBuilder
    throw new IOException("XML format not yet fully implemented. " +
        "This class demonstrates how easy it is to add new formats!");
    
    /* Production implementation would look like:
    
    QuestionBank bank = new QuestionBank();
    
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document document = builder.parse(new File(filePath));
      
      NodeList questionNodes = document.getElementsByTagName("question");
      for (int i = 0; i < questionNodes.getLength(); i++) {
        Element element = (Element) questionNodes.item(i);
        Question question = parseQuestionElement(element);
        bank.addQuestion(question);
      }
    } catch (Exception e) {
      throw new IOException("Failed to parse XML: " + filePath, e);
    }
    
    return bank;
    */
  }

  @Override
  public String getFormat() {
    return FORMAT;
  }
  
  /**
   * Example of how XML element would be parsed to Question.
   *
   * @param element XML element representing a question
   * @return parsed Question object
   */
  private Question parseQuestionElement(Object element) {
    // Example structure:
    // <question id="Q001" category="Science" difficulty="1">
    //   <text>What is the speed of light?</text>
    //   <answers>
    //     <answer correct="true">300,000 km/s</answer>
    //     <answer>150,000 km/s</answer>
    //     <answer>600,000 km/s</answer>
    //     <answer>50,000 km/s</answer>
    //   </answers>
    // </question>
    
    List<String> answers = new ArrayList<>();
    // ... parsing logic
    
    return new Question("id", "text", answers, 0, "category", 1);
  }
}

