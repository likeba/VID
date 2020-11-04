package com.nomad.data.agent.utils;

import org.springframework.transaction.support.DefaultTransactionDefinition;

public class DbUtils {
    public static DefaultTransactionDefinition getDefaultTransactionDefinition(String name) {
        DefaultTransactionDefinition definition = new DefaultTransactionDefinition();
        definition.setName(name);
        definition.getPropagationBehavior();
        return definition;
    }
}
