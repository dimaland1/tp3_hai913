package org.example;

import org.example.processor.UserServiceProcessor;
import spoon.Launcher;
import org.example.processor.ProductServiceProcessor;
import java.io.File;

public class SpoonMain {
    public static void main(String[] args) {
        Launcher launcher = new Launcher();

        String inputPath = "src/main/java/org/example/service/ProductService.java";
        String outputPath = "target/generated-sources/spoon";

        System.out.println("Input file exists: " + new File(inputPath).exists());
        System.out.println("Input path: " + new File(inputPath).getAbsolutePath());

        launcher.addInputResource(inputPath);
        launcher.setSourceOutputDirectory(outputPath);

        launcher.getEnvironment().setAutoImports(true);
        launcher.getEnvironment().setCommentEnabled(true);
        launcher.getEnvironment().setComplianceLevel(17);
        launcher.getEnvironment().setLevel("DEBUG");

        launcher.addProcessor(new ProductServiceProcessor());

        System.out.println("Starting Spoon processing...");
        try {
            launcher.run();
            System.out.println("Spoon processing completed successfully");
            System.out.println("Output directory: " + new File(outputPath).getAbsolutePath());

            // Vérifier si le fichier de sortie existe
            File outputDir = new File(outputPath + "/org/example/service");
            if (outputDir.exists()) {
                System.out.println("Generated files:");
                for (File file : outputDir.listFiles()) {
                    System.out.println(" - " + file.getName());
                }
            }
        } catch (Exception e) {
            System.err.println("Error during Spoon processing: " + e.getMessage());
            e.printStackTrace();
        }


        launcher = new Launcher();

        inputPath = "src/main/java/org/example/service/UserService.java";
        outputPath = "target/generated-sources/spoon";

        System.out.println("Input file exists: " + new File(inputPath).exists());
        System.out.println("Input path: " + new File(inputPath).getAbsolutePath());

        launcher.addInputResource(inputPath);
        launcher.setSourceOutputDirectory(outputPath);

        launcher.getEnvironment().setAutoImports(true);
        launcher.getEnvironment().setCommentEnabled(true);
        launcher.getEnvironment().setComplianceLevel(17);
        launcher.getEnvironment().setLevel("DEBUG");

        launcher.addProcessor(new UserServiceProcessor());

        System.out.println("Starting Spoon processing...");
        try {
            launcher.run();
            System.out.println("Spoon processing completed successfully");
            System.out.println("Output directory: " + new File(outputPath).getAbsolutePath());

            // Vérifier si le fichier de sortie existe
            File outputDir = new File(outputPath + "/org/example/service");
            if (outputDir.exists()) {
                System.out.println("Generated files:");
                for (File file : outputDir.listFiles()) {
                    System.out.println(" - " + file.getName());
                }
            }
        } catch (Exception e) {
            System.err.println("Error during Spoon processing: " + e.getMessage());
            e.printStackTrace();
        }
    }
}