package cn.xyz.test.test;

import cn.xyz.common.tools.ToolsDouble;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class T017 {
    public static void main(String[] args) {
        String[] ss = {"新PC/ABS混合胶粒(dddd)/dsagasdga/adgads",
                "电缐(麦克风)/耳机音箱麦克风用/无接头，不是同轴导体/DAEYU等/",
                "电(麦克)缐(麦克风)/耳机音箱麦克风用/无接头，不是同轴导体/DAEYU等/,",
                        "耳机音箱麦克风用(sfas)",
                "喇叭外殼(墊圈/面網/後蓋)/適用於mot>",
        "无对应关系"};
        for (int i = 0; i < ss.length; i++) {
            String s = ss[i];
            String t = "";
            if(s.startsWith("新PC") && s.indexOf("/", 4) > 0){
                t = s.substring(0, s.indexOf("/", 4));
            }else if(s.indexOf("/") > 0){
                t = s.substring(0, s.indexOf("/"));
            }else{
                t = s;
            }
            if(t.matches(".*\\(.*\\)")){
                t = t.substring(0,t.lastIndexOf("("));//t..replaceAll("\\(.*\\)","")
            }
            System.out.println(t);
        }

        /*String s = "D王仲偉申請PDB170005-1392,PDB170005";
        Pattern p = Pattern.compile("PDB\\d{6}-\\d+");
        Matcher m = p.matcher(s);
        while (m.find()) {
            System.out.println(m.group(0));
        }*/
        /*double a = 12.5200;
        double b = 12.12345;
        if(Pattern.matches("^\\d+\\.?\\d{0,3}$", Double.toString(a))){
            System.out.println(ToolsDouble.round(a,3));
        }else{
            System.out.println(a);
        }*/
    }
}
