package de.metalcon.sdd.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.SortedSet;
import java.util.TreeSet;

public class ClassGenerator {

    public static final String DEFAULT_CONFIG_PATH =
            "/usr/share/sdd/config.xml";

    public static final String DEFAULT_OUTPUT_PATH = "/usr/share/sdd/classes";

    public static final String DEFAULT_PACAKGE_NAME = "sdd.generated.classes";

    private String packageName;

    private Config config;

    private Path outputDir;

    public ClassGenerator(
            String configPath,
            String outputPath,
            String packageName) {
        this.packageName = packageName;

        System.out.println("Loading config...");
        config = new XmlConfig(configPath);

        outputDir = Paths.get(outputPath);
        outputDir.toFile().mkdir();
    }

    private void run() throws IOException {
        writeSddOutputClass();

        for (String nodeType : config.getNodeTypes()) {
            ConfigNode node = config.getNode(nodeType);

            Path nodeDir = outputDir.resolve(nodeType.toLowerCase());
            nodeDir.toFile().mkdir();

            writeNode(nodeDir, node, nodeType);
        }
    }

    private void writeNode(Path nodeDir, ConfigNode node, String nodeType)
            throws IOException {
        for (String detail : node.getOutputDetails()) {
            ConfigNodeOutput nodeOutput = node.getOutput(detail);
            writeNodeOutput(nodeDir, node, nodeType, nodeOutput, detail);
        }
    }

    private void writeNodeOutput(
            Path nodeDir,
            ConfigNode node,
            String nodeType,
            ConfigNodeOutput nodeOutput,
            String detail) throws IOException {
        String className = buildClassName(nodeType, detail);
        System.out.println("Generating " + className + "...");

        SortedSet<String> imports = new TreeSet<String>();
        imports.add("SddOutput");

        for (String relation : nodeOutput.getOutRelations()) {
            RelationType type = node.getRelationType(relation);
            String relationDetail = nodeOutput.getOutRelationDetail(relation);
            imports.add(type.getType().toLowerCase() + "."
                    + getRelationType(type, relationDetail, false));
        }

        StringBuilder content = new StringBuilder();
        content.append("package " + packageName + "." + nodeType.toLowerCase()
                + ";\n\n");
        if (!imports.isEmpty()) {
            for (String imp : imports) {
                content.append("import " + packageName + "." + imp + ";\n");
            }
        }
        content.append("\n");
        content.append("public class " + className + " extends SddOutput {\n\n");

        for (String property : nodeOutput.getOutProperties()) {
            String type = node.getPropertyType(property);
            writeField(content, node, nodeOutput, property, type);
        }

        for (String relation : nodeOutput.getOutRelations()) {
            String type =
                    getRelationType(node.getRelationType(relation),
                            nodeOutput.getOutRelationDetail(relation), true);
            writeField(content, node, nodeOutput, relation, type);
        }

        for (String property : nodeOutput.getOutProperties()) {
            String type = node.getPropertyType(property);
            writeGetters(content, node, nodeOutput, property, type);
        }

        for (String relation : nodeOutput.getOutRelations()) {
            String type =
                    getRelationType(node.getRelationType(relation),
                            nodeOutput.getOutRelationDetail(relation), true);
            writeGetters(content, node, nodeOutput, relation, type);
        }

        content.append("}\n");

        Path nodeOutputPath = nodeDir.resolve(className + ".java");
        Files.write(nodeOutputPath, content.toString().getBytes(),
                StandardOpenOption.WRITE, StandardOpenOption.CREATE);
    }

    private void writeField(
            StringBuilder content,
            ConfigNode node,
            ConfigNodeOutput nodeOutput,
            String name,
            String type) {
        content.append("    private " + type + " " + name + ";\n\n");
    }

    private void writeGetters(
            StringBuilder content,
            ConfigNode node,
            ConfigNodeOutput nodeOutput,
            String name,
            String type) {
        content.append("    public " + type + " get" + capitalize(name)
                + "() {\n");
        content.append("        return " + name + ";\n");
        content.append("    }\n\n");
    }

    private static String getRelationType(
            RelationType relationType,
            String detail,
            boolean withList) {
        if (withList && relationType.isArray()) {
            return capitalize(relationType.getType()) + capitalize(detail)
                    + "[]";
        } else {
            return capitalize(relationType.getType()) + capitalize(detail);
        }
    }

    private static String buildClassName(String nodeType, String detail) {
        return capitalize(nodeType) + capitalize(detail);
    }

    private static String capitalize(String string) {
        return Character.toUpperCase(string.charAt(0)) + string.substring(1);
    }

    private void writeSddOutputClass() throws IOException {
        System.out.println("Generating SddOutput...");

        StringBuilder content = new StringBuilder();
        content.append("package " + packageName + ";\n\n");
        content.append("import de.metalcon.domain.Muid;\n\n");
        content.append("public abstract class SddOutput {\n\n");
        content.append("    private Muid muid;\n\n");
        content.append("    public Muid getMuid() {\n");
        content.append("        return muid;\n");
        content.append("    }\n\n");
        content.append("}\n");

        Path sddOutputPath = outputDir.resolve("SddOutput.java");
        Files.write(sddOutputPath, content.toString().getBytes(),
                StandardOpenOption.WRITE, StandardOpenOption.CREATE);
    }

    public static void main(String[] args) throws IOException {
        String configPath = DEFAULT_CONFIG_PATH;
        String outputPath = DEFAULT_OUTPUT_PATH;
        String packageName = DEFAULT_PACAKGE_NAME;
        if (args.length > 0) {
            configPath = args[0];
        }
        if (args.length > 1) {
            outputPath = args[1];
        }
        if (args.length > 2) {
            packageName = args[2];
        }
        System.out.println("Using configuration path: \"" + configPath + "\".");
        System.out.println("Using output path: \"" + outputPath + "\".");
        System.out.println("Using package name: \"" + packageName + "\".");

        ClassGenerator main =
                new ClassGenerator(configPath, outputPath, packageName);
        main.run();
    }

}
