package xyz.mpdn.resource.processor.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features"},
        plugin = {"pretty"},
        glue = {"xyz.mpdn.resource.processor.cucumber.glue"})
public class CucumberTestRunner {
}
