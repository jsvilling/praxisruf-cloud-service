package ch.fhnw.ip5.praxiscloudservice.domain;

/**
 * The {@link RuleType} of a {@link RuleConfig} determines how said RuleConfig is evaluated in the RulesEngine.
 */
public enum RuleType {
    SENDER,
    TYPE,
    NOTIFICATION
}
