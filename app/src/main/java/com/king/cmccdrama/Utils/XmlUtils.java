package com.king.cmccdrama.Utils;

import android.util.Log;

import com.king.cmccdrama.Const.Config;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

public class XmlUtils {

    private Document document;


    public XmlUtils() {
    }

    private HashMap<String, String> propertyMap = new HashMap<>(); // 所有属性
    private List<Element> nodeList = new ArrayList<>();

    public HashMap<String, String> GetPropertyMapMap() {
        return this.propertyMap;
    }

    public List<Element> getNodeList() {
        return this.nodeList;
    }

    public Document parse(InputStream xml) {
        SAXReader reader = new SAXReader();
        try {
            document = reader.read(xml);
        } catch (DocumentException ex) {
            ex.printStackTrace();
        }
        return document;
    }

    /*
    public String getNodeText(String nodeName) {
        Element root = document.getRootElement();
        for (Iterator i = root.elementIterator(nodeName); i.hasNext(); ) {
            Element node = (Element) i.next();
            Log.i("JuMeng", nodeName + "的值为:" + node.getText());
            return node.getText();
        }
        return "";
    }
    */

    /*
    // 取指定节点的某个属性
    public String getElementNode(String elementName, String propertyName) {
        Element element = (Element) document.selectSingleNode(elementName);
        return element.attribute(propertyName).getValue();
        //return element.valueOf(propertyName);
    }
     */

    /**
     * 查找所有属性,适合查找全局唯一属性
     * @param node
     */
    public void getAllProperty(Element node) {
        // 当前节点的名称、文本内容和属性
        // System.out.println("当前节点名称："+node.getName());//当前节点名称
        // System.out.println("当前节点的内容："+node.getTextTrim());//当前节点名称
        List<Attribute> listAttr = node.attributes();//当前节点的所有属性的list
        for(Attribute attr:listAttr){//遍历当前节点的所有属性
            String name = attr.getName();//属性名称
            String value = attr.getValue();//属性的值
            propertyMap.put(name,value);
        }
        //递归遍历当前节点所有的子节点
        List<Element> listElement = node.elements();//所有一级子节点的list
        for(Element e:listElement){//遍历所有一级子节点
            this.getAllProperty(e);//递归
        }
    }

    /**
     * 查找名为searchName的节点,结果通过"getNodeList"获取
     * @param node
     * @param searchName
     */
    public void getNode(Element node, String searchName) {
        if (searchName.equals(node.getName())) {
            this.nodeList.add(node);
        }
        // 递归
        List<Element> listElement = node.elements();
        for(Element e:listElement) {
            this.getNode(e, searchName);
        }
    }

    public void getAllProperty2(Element e) {
        List<Element> els = e.elements();
        for (Element el : els) {
            //判断是否复合节点
            if(!el.hasMixedContent()){
                propertyMap.put(el.getName(),el.getTextTrim());
            }else{
                System.out.println(el.getPath());
                getAllProperty2(el);
            }
        }
    }

    /**
     * 获取所有子节点
     * @param e
     */
    /*
    public void getAllChildNodes(Element e) {
        List<Element> els = e.elements();
        for (Element el : els) {
            //判断是否复合节点
            if(!el.hasMixedContent()){
                Log.i("JuMeng", el.getName()+":"+el.getText());
                nodeMap.put(el.getName(),el.getTextTrim());
            }else{
                System.out.println(el.getPath());
                getAllChildNodes(el);
            }
        }
    }
     */
}
