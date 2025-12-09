package com.kakao.actionbase.test;

import org.junit.jupiter.api.extension.ConditionEvaluationResult;
import org.junit.jupiter.api.extension.ExecutionCondition;
import org.junit.jupiter.api.extension.ExtensionContext;

public class IntelliJRunClickOnlyCondition implements ExecutionCondition {

  @Override
  public ConditionEvaluationResult evaluateExecutionCondition(ExtensionContext context) {
    String xpcService = System.getenv("XPC_SERVICE_NAME");
    String envEnabled = System.getenv("INTEGRATION_TESTS_ENABLED");

    boolean isRunFromIDE = xpcService != null && xpcService.contains("com.jetbrains.intellij");
    boolean isEnvEnabled = "true".equalsIgnoreCase(envEnabled);

    if (isRunFromIDE || isEnvEnabled) {
      return ConditionEvaluationResult.enabled("Run from IntelliJ (GUI click) or env override");
    }

    return ConditionEvaluationResult.disabled("Skipped: Not run from IntelliJ Run or env not set");
  }
}
