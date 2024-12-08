package org.example.processor;

import spoon.processing.AbstractProcessor;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.code.CtBlock;
import spoon.reflect.code.CtCodeSnippetStatement;

public class ProductServiceProcessor extends AbstractProcessor<CtMethod<?>> {

    @Override
    public boolean isToBeProcessed(CtMethod<?> method) {
        System.out.println("Checking method: " + method.getSimpleName());
        boolean shouldProcess = method.getDeclaringType().getSimpleName().equals("ProductService");
        System.out.println("Should process: " + shouldProcess);
        return shouldProcess;
    }

    @Override
    public void process(CtMethod<?> method) {
        System.out.println("Processing method: " + method.getSimpleName());

        String methodName = method.getSimpleName();
        String operationType = getOperationType(methodName);

        String logCode = String.format(
                "try {\n" +
                        "    org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(\"UserProfile\");\n" +
                        "    String userId = java.util.Optional.ofNullable(org.example.context.UserContext.getCurrentUser())\n" +
                        "        .map(u -> u.getId())\n" +
                        "        .orElse(\"unknown\");\n" +
                        "    com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();\n" +
                        "    com.fasterxml.jackson.databind.node.ObjectNode logData = mapper.createObjectNode()\n" +
                        "        .put(\"method\", \"%s\")\n" +
                        "        .put(\"type\", \"%s\")\n" +
                        "        .put(\"userId\", userId)\n" +
                        "        .put(\"timestamp\", System.currentTimeMillis());\n" +
                        "    logger.info(mapper.writeValueAsString(logData));\n" +
                        "} catch (Exception e) {\n" +
                        "    System.err.println(\"Error logging operation: \" + e.getMessage());\n" +
                        "}\n",
                methodName,
                operationType
        );

        // Création et insertion du snippet
        CtCodeSnippetStatement snippet = getFactory().Code().createCodeSnippetStatement(logCode);
        CtBlock<?> body = method.getBody();
        if (body != null) {
            body.insertBegin(snippet);
            System.out.println("Added logging to method: " + methodName);
        }
    }

    private String getOperationType(String methodName) {
        if (methodName.startsWith("get") || methodName.startsWith("find")) {
            return "READ";
        } else if (methodName.startsWith("create")) {
            return "CREATE";
        } else if (methodName.startsWith("update")) {
            return "UPDATE";
        } else if (methodName.startsWith("delete")) {
            return "DELETE";
        }
        return "UNKNOWN";
    }
}