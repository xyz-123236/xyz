package cn.xyz.common.tools;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class ToolsXml {
    public static void createXML(String filePath, String fileName, Document doc) {
        try (FileWriter out = new FileWriter(filePath + fileName)){//写入文件
        	File newFile = new File(filePath);
			if (!newFile.exists()) {
				newFile.mkdirs();
			}
            //createDocument().write(out);
            OutputFormat format = OutputFormat.createPrettyPrint();  //转换成字符串
            format.setEncoding("UTF-8");
            //format.setSuppressDeclaration(true);//是否显示头
            format.setIndent(true); //设置是否缩进
            //format.setIndent(" "); //以空格方式实现缩进
            format.setNewlines(true); //设置是否换行
            //XMLWriter writer = new XMLWriter( System.out, format );//控制台输出
            XMLWriter writer = new XMLWriter(out, format );
            writer.write(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void print(Element root){
        Iterator iterator = root.elementIterator();
        while (iterator.hasNext()) {
            Element node = (Element) iterator.next();
            Iterator iterator2 = node.elementIterator();
            if (iterator2.hasNext()) {
                System.out.println("父节点名：" + node.getName());
                print(node);
            }else{
                System.out.println("子节点名：" + node.getName() + "---节点值：" + node.getStringValue());
            }
        }
    }
    public static void main(String[] args) {
        try {
            //FileUtilities.downloadFile("ftpas.flextronics.com","ftpfjtsf","w7y6mX%G","/test_out","E:\\temp");
            //1.创建Reader对象
            SAXReader reader = new SAXReader();
            //2.加载xml
            Document document = reader.read(new File("E:\\temp\\FLEX-FK-FUT-RECASN-100000374-202101221657.xml"));
            //3.获取根节点
            Element root = document.getRootElement();

            //查找某个节点
            Element element1 = (Element) root.selectNodes("app/data/rec/pack/del_date").get(0);
            System.out.println("节点名：" + element1.getName() + "---节点值：" + element1.getStringValue());
            //循环输出全部
            print(root);

        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
