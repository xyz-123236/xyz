package cn.xyz.portal;

import cn.xyz.common.orm.DbTool;
import cn.xyz.common.pojo.Email;
import cn.xyz.common.tools.Tools;
import cn.xyz.common.tools.ToolsEmail;
import cn.xyz.common.tools.ToolsString;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CreateUser {
    public static void main(String[] args) {

        try {
            JSONArray us = DbTool.getInstance().createSelectSql("sn_temp").select();
            JSONArray us2 = DbTool.getInstance().setSql("select * from (\n" +
                    "\tSELECT s.*,u.email,u.userid FROM fic_portal.sn_temp s LEFT JOIN `user` u on s.code = u.company_code\n" +
                    ") as a where a.sn1 = a.email or (a.sn2 is not null and a.sn2 != '' and a.sn2 = a.email) or (a.sn3 is not null and a.sn3 != '' and a.sn3 = a.email) or (a.sn4 is not null and a.sn4 != '' and a.sn4 = a.email)\n").select();
            Set<String> set = new HashSet<>();
            if(!Tools.isEmpty(us2)){
                for (int i = 0; i < us2.size(); i++) {
                    JSONObject item =  us2.getJSONObject(i);
                    set.add(item.getString("email"));
                    //增加权限
                    /*JSONObject obj2 = new JSONObject();
                    System.out.println(item.getString("userid"));
                    obj2.put("userid",item.getString("userid"));
                    obj2.put("group_id",15);//15
                    DbTool.getInstance().createInsertSql("user_group",obj2,"tang.wu").insert();

                    Email email = new Email();
                    email.setSubject("富士高中名Partner Portal系统开户信息");
                    email.setEmail_to(item.getString("email"));
                    email.setEmail_cc("tang.wu@fujikon.com");
                    email.setContent("尊敬的合作伙伴("+item.getString("code")+"),您好：\n"
                            + "      您的用户（"+item.getString("userid")+"）已开通对账模块       \n"
                            + "请知悉，谢谢！");
                    ToolsEmail.SendEmail(email);
                    Thread.sleep(2000);*/
                }
            }
            for (int i = 0; i < us.size(); i++) {
                //添加user
                JSONObject item = us.getJSONObject(i);
                JSONObject obj = new JSONObject();
                obj.put("userid",item.getString("code")+"_AP");
                JSONArray data = DbTool.getInstance().setSql("select * from `user` where userid = '"+item.getString("code")+"_AP"+"'").select();
                if(!Tools.isEmpty(data)){
                    continue;
                }
                String password = getRandomPassWord();
                obj.put("password",getMD5(password));
                obj.put("username",item.getString("username"));
                obj.put("status","P");
                obj.put("user_type","Vendor");
                String emai = "";
                if(!set.contains(item.getString("sn1"))){
                    emai += ","+item.getString("sn1").trim().replaceAll(" ","").replaceAll("　","");
                }
                if(!Tools.isEmpty(item.getString("sn2"))){
                    emai += ","+item.getString("sn2").trim().replaceAll(" ","").replaceAll("　","");
                }
                if(!Tools.isEmpty(item.getString("sn3"))){
                    emai += ","+item.getString("sn3").trim().replaceAll(" ","").replaceAll("　","");
                }
                if(!Tools.isEmpty(item.getString("sn4"))){
                    emai += ","+item.getString("sn4").trim().replaceAll(" ","").replaceAll("　","");
                }
                if(!Tools.isEmpty(emai)){
                    emai = emai.substring(1);
                    obj.put("email", emai);
                    //obj.put("phoneno",item.getString("phoneno"));
                    obj.put("company_code",item.getString("code"));
                    obj.put("company_name",item.getString("name"));
                    System.out.println(item.getString("code")+item.getString("name"));
                    DbTool.getInstance().createInsertSql("user",obj,"tang.wu").insert();
                    //添加user-group
                    JSONObject obj2 = new JSONObject();
                    obj2.put("userid",item.getString("code")+"_AP");
                    obj2.put("group_id",15);//15
                    DbTool.getInstance().createInsertSql("user_group",obj2,"tang.wu").insert();

                    Email email = new Email();
                    email.setSubject("富士高中名Partner Portal系统开户信息");
                    System.out.println(obj.getString("email"));
                    email.setEmail_to(obj.getString("email"));
                    email.setEmail_cc("tang.wu@fujikon.com");
                    email.setContent("尊敬的合作伙伴("+item.getString("code")+"),您好：\n\n"
                            + "    您申请的对账账号已为您开通，用户名："+obj.getString("userid")+",密码："+password+"       \n\n"
                            + "请尽快登录修改密码，谢谢！");
                    ToolsEmail.SendEmail(email);
                    Thread.sleep(2000);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static String getRandomPassWord() {
        String str = new String("123456789qwertyuiopasdfghjklzxcvbnmQWERTYUIPASDFGHJKLZXCVBNM");
        StringBuffer sb = new StringBuffer();
        Random r = new Random();
        int range = str.length();
        for (int i = 0; i < 8; i++) {
            sb.append(str.charAt(r.nextInt(range)));
        }
        return sb.toString();
    }
    public static String getMD5(String str) {

        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");

            md.update(str.getBytes());
            result = new BigInteger(1, md.digest()).toString(16);

        } catch (Exception e) {
            return null;
        }
        return result;
    }
}
