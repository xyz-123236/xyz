package cn.xyz.portal;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.itextpdf.text.pdf.parser.RegionTextRenderFilter;
import com.itextpdf.text.pdf.parser.RenderFilter;

import java.net.MalformedURLException;

public class T002 {
    public static void main(String[] args) throws MalformedURLException {
        //URL url=new DocFlavor.URL("C:\\Users\\Administrator.SC-201811201406\\Desktop\\开发资料\\声明.pdf");
        String pageContent = "";
        try {
            PdfReader reader = new PdfReader("E:\\test\\2.pdf");
            Rectangle rect = new Rectangle(90, 0, 450, 40);
            RenderFilter filter = new RegionTextRenderFilter(rect);
            int pageNum = reader.getNumberOfPages();
            System.out.println(PdfTextExtractor.getTextFromPage(reader, 1).trim().startsWith("檢測報告"));//读取第i页的文档内容
            /*for(int i=1;i<=1;i++){
                System.out.println(PdfTextExtractor.getTextFromPage(reader, i));//读取第i页的文档内容
                TextExtractionStrategy strategy = new FilteredTextRenderListener(new LocationTextExtractionStrategy(), filter);
                //System.out.println(PdfTextExtractor.getTextFromPage(reader, i, strategy));
            }*/
            System.out.println("pageContent");
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
        	//
        }
    }
}
