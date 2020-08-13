package com.lejia.global.weixin.kit;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlHelper {
    private final XPath path;
    private final Document doc;

    private XmlHelper(InputSource inputSource) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory dbf = getDocumentBuilderFactory();
        DocumentBuilder db = dbf.newDocumentBuilder();
        this.doc = db.parse(inputSource);
        this.path = getXPathFactory().newXPath();
    }

    private static XmlHelper create(InputSource inputSource) {
        try {
            return new XmlHelper(inputSource);
        } catch (ParserConfigurationException var2) {
            throw new RuntimeException(var2);
        } catch (SAXException var3) {
            throw new RuntimeException(var3);
        } catch (IOException var4) {
            throw new RuntimeException(var4);
        }
    }

    public static XmlHelper of(InputStream is) {
        InputSource inputSource = new InputSource(is);
        return create(inputSource);
    }

    public static XmlHelper of(String xmlStr) {
        StringReader sr = new StringReader(xmlStr.trim());
        InputSource inputSource = new InputSource(sr);
        XmlHelper xmlHelper = create(inputSource);
        IOUtils.closeQuietly(sr);
        return xmlHelper;
    }

    private Object evalXPath(String expression, Object item, QName returnType) {
        item = null == item ? this.doc : item;

        try {
            return this.path.evaluate(expression, item, returnType);
        } catch (XPathExpressionException var5) {
            throw new RuntimeException(var5);
        }
    }

    public String getString(String expression) {
        return (String)this.evalXPath(expression, (Object)null, XPathConstants.STRING);
    }

    public Boolean getBoolean(String expression) {
        return (Boolean)this.evalXPath(expression, (Object)null, XPathConstants.BOOLEAN);
    }

    public Number getNumber(String expression) {
        return (Number)this.evalXPath(expression, (Object)null, XPathConstants.NUMBER);
    }

    public Node getNode(String expression) {
        return (Node)this.evalXPath(expression, (Object)null, XPathConstants.NODE);
    }

    public NodeList getNodeList(String expression) {
        return (NodeList)this.evalXPath(expression, (Object)null, XPathConstants.NODESET);
    }

    public String getString(Object node, String expression) {
        return (String)this.evalXPath(expression, node, XPathConstants.STRING);
    }

    public Boolean getBoolean(Object node, String expression) {
        return (Boolean)this.evalXPath(expression, node, XPathConstants.BOOLEAN);
    }

    public Number getNumber(Object node, String expression) {
        return (Number)this.evalXPath(expression, node, XPathConstants.NUMBER);
    }

    public Node getNode(Object node, String expression) {
        return (Node)this.evalXPath(expression, node, XPathConstants.NODE);
    }

    public NodeList getNodeList(Object node, String expression) {
        return (NodeList)this.evalXPath(expression, node, XPathConstants.NODESET);
    }

    public Map<String, String> toMap() {
        Element root = this.doc.getDocumentElement();
        Map<String, String> params = new HashMap();
        NodeList list = root.getChildNodes();

        for(int i = 0; i < list.getLength(); ++i) {
            Node node = list.item(i);
            if (node instanceof Element) {
                params.put(node.getNodeName(), node.getTextContent());
            }
        }

        return params;
    }

    private static DocumentBuilderFactory getDocumentBuilderFactory() {
        return XmlHelper.XmlHelperHolder.documentBuilderFactory;
    }

    private static XPathFactory getXPathFactory() {
        return XmlHelper.XmlHelperHolder.xPathFactory;
    }

    private static class XmlHelperHolder {
        private static DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        private static XPathFactory xPathFactory = XPathFactory.newInstance();

        private XmlHelperHolder() {
        }
    }
}

