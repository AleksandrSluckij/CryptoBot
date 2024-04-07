package com.skillbox.cryptobot.configuration.properties;

import com.skillbox.cryptobot.exception.DelayPropertyException;
import org.springframework.boot.diagnostics.AbstractFailureAnalyzer;
import org.springframework.boot.diagnostics.FailureAnalysis;

import java.text.MessageFormat;

public class DelayPropertiesAnalyzer extends AbstractFailureAnalyzer<DelayPropertyException> {

    @Override
    protected FailureAnalysis analyze(Throwable rootFailure, DelayPropertyException cause) {
        return new FailureAnalysis(MessageFormat.format("Exception when try to set property: {0}", cause.getMessage()),
                "set-application-properties", cause);
    }
}
