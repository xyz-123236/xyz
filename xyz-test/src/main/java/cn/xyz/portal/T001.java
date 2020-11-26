package cn.xyz.portal;

import cn.xyz.common.orm.DbTool;
import cn.xyz.common.pojo.Email;
import cn.xyz.common.tools.Tools;
import cn.xyz.common.tools.ToolsEmail;
import com.alibaba.fastjson.JSONArray;

public class T001 {
    public static void main(String[] args) throws Exception {
        String[] emails = new String[]{
                "dongyan.wei@gdvdl.com,xi.liu@gdvdl.com",
                "hua@genius-win.com",
                "hanayang@sz.ese.com.hk",
                "jingyi899@126.com",
                "sales01@leagtech.com",
                "finac@siil.com.cn,li_wu@siil.com.cn,andyhsiao@siil.com.cn,lingling@siil.com.cn",
                "jesse-yu@xingshengda.net,yw-xuxiaohong@xingshengda.net,hujiamin@xingshengda.net"
        };
        String[] users = new String[]{
                "CV30001_AP",
                "DJ30002_AP",
                "ET30001_AP",
                "JS30001_AP",
                "LJ30001_AP",
                "SI41001_AP",
                "SX30006_AP"
        };
        for (int i = 0; i < users.length; i++) {
            Email email = new Email();
            email.setSubject("富士高中名Partner Portal系统开户信息");
            System.out.println(emails[i]);
            email.setEmail_to(emails[i]);
            email.setEmail_cc("tang.wu@fujikon.com");
            email.setContent("尊敬的合作伙伴("+users[i].substring(0,7)+"),您好：\n\n"
                    + "    您申请的对账账号已为您开通，用户名："+users[i]+",密码：fvqMI7bs       \n\n"
                    + "请尽快登录修改密码，谢谢！");
            ToolsEmail.SendEmail(email);
            Thread.sleep(2000);

        }


        String s = "dongyan.wei@gdvdl.com ";
        System.out.println(s.trim().replaceAll(" ","").replaceAll(" ","")+"ab");
        s = "hua@genius-win.com　";

//        JSONArray data = DbTool.getInstance().setSql("select * from `user` where userid = 'xxxxxxx_AP'").select();
//        if(Tools.isEmpty(data)){
//            System.out.println("kong");
//        }else{
//            System.out.println("no");
//        }
//        JSONArray data2 = DbTool.getInstance().setSql("select * from `user` where userid = 'DG30012_AP'").select();
//        if(Tools.isEmpty(data2)){
//            System.out.println("kong");
//        }else{
//            System.out.println("no");
//        }
    }
}
