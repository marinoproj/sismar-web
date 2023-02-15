package br.com.marino.sismar.util;

import java.util.ArrayList;
import java.util.List;

public class RuleCondition {

    private String id;
    private String label;

    public static final List<RuleCondition> RULE_CONDITIONS;

    static {
        RULE_CONDITIONS = new ArrayList<>();
        RULE_CONDITIONS.add(new RuleCondition("maior", "Maior"));
        RULE_CONDITIONS.add(new RuleCondition("maior ou igual", "Maior ou igual"));
        RULE_CONDITIONS.add(new RuleCondition("menor", "Menor"));
        RULE_CONDITIONS.add(new RuleCondition("menor ou igual", "Menor ou igual"));
    }

    private RuleCondition(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

}
