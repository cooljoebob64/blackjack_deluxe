package com.jlu.blackjack.runner;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

/**
 * Entry point for running the Cucumber tests in JUnit
 */
@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty"},
        snippets = CucumberOptions.SnippetType.CAMELCASE,
        features = {"src/test/resources"},
        glue = {"com.jlu.blackjack.test", "src/test/resources"}
)
public class CucumberTest {
    // This class should be empty, step definitions are located in separate classes.
}
