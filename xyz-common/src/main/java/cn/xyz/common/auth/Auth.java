package cn.xyz.common.auth;

import cn.xyz.common.orm.DbTool;
import cn.xyz.common.tools.Tools;
import cn.xyz.common.tools.ToolsSys;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpSession;

public class Auth {
    public static boolean havePermission(String username) throws Exception {
        return havePermission(username, null);
    }
    public static boolean havePermission(String username, String key) throws Exception {
        String[] roles = null;
        if(!Tools.isEmpty(key)) {
            JSONObject permission = DbTool.getInstance().createSelectSql("permission").where().andString("key", key).selectOne();
            if(!Tools.isEmpty(permission) && !Tools.isEmpty(permission.getString("value"))) {
                roles = permission.getString("value").split(",");
            }
        }
        return haveRole(username, roles);
    }
    public static boolean haveRole(String username, String role) throws Exception {
        return haveRole(username, new String[] {role});
    }
    public static boolean haveRole(String username, String[] roles) throws Exception {//有没有roles中的一个
        JSONArray ug = DbTool.getInstance().createSelectSql("groupusers").where().andString("username", username).select();
        if(!Tools.isEmpty(ug)) {
            for (int i = 0; i < ug.size(); i++) {
                if("ADMIN".equals(ug.getJSONObject(i).getString("groupid"))) {
                    return true;
                }else {
                    if(roles != null) {
                        for (String role : roles) {
                            if (ug.getJSONObject(i).getString("groupid").equals(role.trim())) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public JSONObject getPermission(JSONObject row, HttpSession session) throws Exception {
        JSONObject obj = new JSONObject();
        obj.put("ADMIN", havePermission(ToolsSys.getUsercode(session)));
        String key = row.getString("key");
        if(!Tools.isEmpty(key)) {
            String[] keys = key.split(",");
            for (String s : keys) {
                obj.put(s, havePermission(ToolsSys.getUsercode(session), s));
            }
        }
        return obj;
    }
}
