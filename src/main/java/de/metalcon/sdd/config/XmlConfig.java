package de.metalcon.sdd.config;

import java.io.IOException;
import java.nio.file.Paths;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import de.metalcon.sdd.api.exception.InvalidXmlConfigException;

public class XmlConfig extends Config {

    public XmlConfig(
            String configFile) {
        super();

        xmlLoad(configFile);
    }

    private void xmlLoad(String configFile) {
        Document domDoc = null;
        try {
            domDoc =
                    DocumentBuilderFactory.newInstance().newDocumentBuilder()
                            .parse(Paths.get(configFile).toFile());
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new InvalidXmlConfigException("Couldn't open XML file: \""
                    + configFile + "\".", e);
        }

        domDoc.normalize();

        Element domRoot = domDoc.getDocumentElement();
        xmlAssertNodeName(domRoot, "sdd-config");

        int i = 0;
        for (Node domIter = domRoot.getFirstChild(); domIter != null; domIter =
                domIter.getNextSibling()) {
            if (domIter.getNodeType() == Node.ELEMENT_NODE) {
                Element domElement = (Element) domIter;

                ++i;
                switch (i) {
                    case 1:
                        xmlLoadTransactions(domElement);
                        break;
                    case 2:
                        xmlLoadDetails(domElement);
                        break;
                    case 3:
                        xmlLoadNodes(domElement);
                        break;

                    default:
                        throw new InvalidXmlConfigException(
                                "To many elements in XML config.");
                }
            }
        }
    }

    private void xmlLoadTransactions(Element domTransactionMode) {
        xmlAssertNodeName(domTransactionMode, "transactions");
        xmlAssertHasAttribute(domTransactionMode, "mode");
        String mode = domTransactionMode.getAttribute("mode");
        switch (mode) {
            case "single":
                setTransactionMode(TransactionMode.SINGLE);
                break;
            case "block":
                setTransactionMode(TransactionMode.BLOCK);
                break;

            default:
                throw new InvalidXmlConfigException(
                        "Invalid mode for transations: \"" + mode
                                + "\". Valid modes are: \"single\", \"block\".");
        }
    }

    private void xmlLoadDetails(Element domDetails) {
        xmlAssertNodeName(domDetails, "details");
        for (Node domIter = domDetails.getFirstChild(); domIter != null; domIter =
                domIter.getNextSibling()) {
            if (domIter.getNodeType() == Node.ELEMENT_NODE) {
                Element domElement = (Element) domIter;
                xmlLoadDetail(domElement);
            }
        }
    }

    private void xmlLoadDetail(Element domDetail) {
        xmlAssertNodeName(domDetail, "detail");
        xmlAssertHasAttribute(domDetail, "name");
        addDetail(domDetail.getAttribute("name"));
    }

    private void xmlLoadNodes(Element domNodes) {
        xmlAssertNodeName(domNodes, "nodes");
        for (Node domIter = domNodes.getFirstChild(); domIter != null; domIter =
                domIter.getNextSibling()) {
            if (domIter.getNodeType() == Node.ELEMENT_NODE) {
                Element domElement = (Element) domIter;
                xmlLoadNode(domElement);
            }
        }
    }

    private void xmlLoadNode(Element domNode) {
        xmlAssertNodeName(domNode, "node");
        xmlAssertHasAttribute(domNode, "type");

        String nodeType = domNode.getAttribute("type");
        if (isNodeType(nodeType)) {
            throw new InvalidXmlConfigException(
                    "Duplicate definition of Node type: \"" + nodeType + "\".");
        }

        ConfigNode node = new ConfigNode();

        int i = 0;
        for (Node domIter = domNode.getFirstChild(); domIter != null; domIter =
                domIter.getNextSibling()) {
            if (domIter.getNodeType() == Node.ELEMENT_NODE) {
                Element domElement = (Element) domIter;

                if (i == 0) {
                    if (domElement.getNodeName().equals("property")) {
                        xmlLoadProperty(node, domElement);
                    } else {
                        ++i;
                    }
                }
                if (i == 1) {
                    if (domElement.getNodeName().equals("relation")) {
                        xmlLoadRelation(node, domElement);
                    } else {
                        ++i;
                    }
                }
                if (i == 2) {
                    if (domElement.getNodeName().equals("output")) {
                        xmlLoadOutput(node, domElement);
                    } else {
                        throw new InvalidXmlConfigException(
                                "Invalid XML child elements for Node: \""
                                        + nodeType + "\".");
                    }
                }
            }
        }

        addNode(nodeType, node);
    }

    private void xmlLoadProperty(ConfigNode node, Element domProperty) {
        xmlAssertNodeName(domProperty, "property");
        xmlAssertHasAttribute(domProperty, "name");
        xmlAssertHasAttribute(domProperty, "type");
        node.addProperty(domProperty.getAttribute("name"),
                domProperty.getAttribute("type"));
    }

    private void xmlLoadRelation(ConfigNode node, Element domRelation) {
        xmlAssertNodeName(domRelation, "relation");
        xmlAssertHasAttribute(domRelation, "name");
        xmlAssertHasAttribute(domRelation, "type");
        node.addRelation(domRelation.getAttribute("name"), new RelationType(
                domRelation.getAttribute("type")));
    }

    private void xmlLoadOutput(ConfigNode node, Element domOutput) {
        xmlAssertNodeName(domOutput, "output");
        xmlAssertHasAttribute(domOutput, "detail");

        String outputDetail = domOutput.getAttribute("detail");
        ConfigNodeOutput output = new ConfigNodeOutput();

        int i = 0;
        for (Node domIter = domOutput.getFirstChild(); domIter != null; domIter =
                domIter.getNextSibling()) {
            if (domIter.getNodeType() == Node.ELEMENT_NODE) {
                Element domElement = (Element) domIter;

                if (i == 0) {
                    if (domElement.getNodeName().equals("out-property")) {
                        xmlLoadOutProperty(output, domElement);
                    } else {
                        ++i;
                    }
                }
                if (i == 1) {
                    if (domElement.getNodeName().equals("out-relation")) {
                        xmlLoadOutRelation(output, domElement);
                    } else {
                        throw new InvalidXmlConfigException(
                                "Invalid XML child elements for Output + \""
                                        + outputDetail + "\".");
                    }
                }
            }
        }

        node.addOutput(outputDetail, output);
    }

    private void xmlLoadOutProperty(
            ConfigNodeOutput output,
            Element domOutProperty) {
        xmlAssertNodeName(domOutProperty, "out-property");
        xmlAssertHasAttribute(domOutProperty, "property");
        output.addOutPropery(domOutProperty.getAttribute("property"));
    }

    private void xmlLoadOutRelation(
            ConfigNodeOutput output,
            Element domOutRelation) {
        xmlAssertNodeName(domOutRelation, "out-relation");
        xmlAssertHasAttribute(domOutRelation, "relation");
        xmlAssertHasAttribute(domOutRelation, "detail");
        output.addOutRelation(domOutRelation.getAttribute("relation"),
                domOutRelation.getAttribute("detail"));
    }

    private void xmlAssertNodeName(Element domElement, String expectedNodeName) {
        if (!domElement.getNodeName().equals(expectedNodeName)) {
            throw new InvalidXmlConfigException("Invalid XML Element \""
                    + domElement.getNodeName() + "\". Expected Node Name: \""
                    + expectedNodeName + "\".");
        }
    }

    private void xmlAssertHasAttribute(
            Element domElement,
            String expectedAttribute) {
        if (!domElement.hasAttribute(expectedAttribute)) {
            throw new InvalidXmlConfigException("Expected Attribute \""
                    + expectedAttribute + "\" on XML Element: \""
                    + domElement.getNodeName() + "\".");
        }
    }
}
