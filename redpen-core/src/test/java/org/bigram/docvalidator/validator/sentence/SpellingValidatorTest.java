package org.bigram.docvalidator.validator.sentence;

import org.bigram.docvalidator.DocumentValidator;
import org.bigram.docvalidator.DocumentValidatorException;
import org.bigram.docvalidator.ValidationError;
import org.bigram.docvalidator.config.Configuration;
import org.bigram.docvalidator.config.ValidatorConfiguration;
import org.bigram.docvalidator.distributor.FakeResultDistributor;
import org.bigram.docvalidator.model.DocumentCollection;
import org.bigram.docvalidator.model.Sentence;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class SpellingValidatorTest {

  @Test
  public void testValidate() throws Exception {
    SpellingValidator validator = new SpellingValidator();
    validator.addWord("this");
    validator.addWord("is");
    validator.addWord("a");
    validator.addWord("pen");
    List<ValidationError> errors = validator.validate(
        new Sentence("this iz a pen", 0));
    assertEquals(1, errors.size());
  }

  @Test
  public void testLoadDefaultDictionary() throws DocumentValidatorException {
    Configuration config = new Configuration.Builder()
        .addValidatorConfig(new ValidatorConfiguration("Spelling"))
        .setCharacterTable("en").build();

    DocumentCollection documents = new DocumentCollection.Builder()
        .addDocument("")
        .addSection(1, new ArrayList<>())
        .addParagraph()
        .addSentence(
            "this iz goody",
            1)
        .build();

    DocumentValidator validator = new DocumentValidator.Builder()
        .setConfiguration(config)
        .setResultDistributor(new FakeResultDistributor())
        .build();

    List<ValidationError> errors = validator.check(documents);
    assertEquals(1, errors.size());
  }

  @Test
  public void testUpperCase() throws DocumentValidatorException {
    Configuration config = new Configuration.Builder()
        .addValidatorConfig(new ValidatorConfiguration("Spelling"))
        .setCharacterTable("en").build();

    DocumentCollection documents = new DocumentCollection.Builder()
        .addDocument("")
        .addSection(1, new ArrayList<>())
        .addParagraph()
        .addSentence(
            "This iz goody",
            1)
        .build();

    DocumentValidator validator = new DocumentValidator.Builder()
        .setConfiguration(config)
        .setResultDistributor(new FakeResultDistributor())
        .build();

    List<ValidationError> errors = validator.check(documents);
    assertEquals(1, errors.size());
  }


  @Test
  public void testSkipCharacterCase() throws DocumentValidatorException {
    Configuration config = new Configuration.Builder()
        .addValidatorConfig(new ValidatorConfiguration("Spelling"))
        .setCharacterTable("en").build();

    DocumentCollection documents = new DocumentCollection.Builder()
        .addDocument("")
        .addSection(1, new ArrayList<>())
        .addParagraph()
        .addSentence(
            "That is true, but there is a condition",
            1)
        .build();

    DocumentValidator validator = new DocumentValidator.Builder()
        .setConfiguration(config)
        .setResultDistributor(new FakeResultDistributor())
        .build();

    List<ValidationError> errors = validator.check(documents);
    assertEquals(0, errors.size());
  }

  @Test
  public void testEndPeriod() throws DocumentValidatorException {
    Configuration config = new Configuration.Builder()
        .addValidatorConfig(new ValidatorConfiguration("Spelling"))
        .setCharacterTable("en").build();

    DocumentCollection documents = new DocumentCollection.Builder()
        .addDocument("")
        .addSection(1, new ArrayList<>())
        .addParagraph()
        .addSentence(
            "That is true.",
            1)
        .build();

    DocumentValidator validator = new DocumentValidator.Builder()
        .setConfiguration(config)
        .setResultDistributor(new FakeResultDistributor())
        .build();

    List<ValidationError> errors = validator.check(documents);
    assertEquals(0, errors.size());
  }

  @Test
  public void testVoid() throws DocumentValidatorException {
    Configuration config = new Configuration.Builder()
        .addValidatorConfig(new ValidatorConfiguration("Spelling"))
        .setCharacterTable("en").build();

    DocumentCollection documents = new DocumentCollection.Builder()
        .addDocument("")
        .addSection(1, new ArrayList<>())
        .addParagraph()
        .addSentence(
            "",
            1)
        .build();

    DocumentValidator validator = new DocumentValidator.Builder()
        .setConfiguration(config)
        .setResultDistributor(new FakeResultDistributor())
        .build();

    List<ValidationError> errors = validator.check(documents);
    assertEquals(0, errors.size());
  }
}