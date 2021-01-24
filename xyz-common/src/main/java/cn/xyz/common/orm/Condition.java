package cn.xyz.common.orm;

import cn.xyz.common.config.Config;
import cn.xyz.common.pojo.Basic;
import cn.xyz.common.tools.Tools;
import com.alibaba.fastjson.JSONObject;

public class Condition<T extends Condition<T>> extends Basic implements Config {
    StringBuffer sql = new StringBuffer();
    String cond = "";
    JSONObject columns = new JSONObject();
    public Condition(){}
    public Condition(String cond, JSONObject columns){
        this.cond = cond;
        this.columns = columns;
    }
    public T eq(String field, Object obj){ return op(field, obj, EQ, false); }
    public T ne(String field, Object obj){ return op(field, obj, NE, false); }
    public T lt(String field, Object obj){ return op(field, obj, LT, false); }
    public T gt(String field, Object obj){ return op(field, obj, GT, false); }
    public T le(String field, Object obj){ return op(field, obj, LE, false); }
    public T ge(String field, Object obj){ return op(field, obj, GE, false); }

    //大写N代表数字：有些数据库不支持数字类型使用字符串
    public T eqN(String field, Object obj){ return op(field, obj, EQ, true); }
    public T neN(String field, Object obj){ return op(field, obj, NE, true); }
    public T ltN(String field, Object obj){ return op(field, obj, LT, true); }
    public T gtN(String field, Object obj){ return op(field, obj, GT, true); }
    public T leN(String field, Object obj){ return op(field, obj, LE, true); }
    public T geN(String field, Object obj){ return op(field, obj, GE, true); }

    public T in(String key, Object obj){ return op(key, obj, IN, false); }
    public T nin(String key, Object obj){ return op(key, obj, NIN, false); }
    public T like(String key, Object obj){ return op(key, obj, LIKE, false); }
    public T nlike(String key, Object obj){ return op(key, obj, NLIKE, false); }
    public T isEmpty(String key, Object obj){ return op(key, obj, IS_EMPTY, false); }
    public T isNotEmpty(String key, Object obj){ return op(key, obj, IS_NOT_EMPTY, false); }
    public T date(String key, Object obj){ return op(key, obj, DATE, false); }
    public T sn(String key, Object obj){ return op(key, obj, SN, false); }
    @SuppressWarnings("unchecked")
    public T op(String field, Object obj, String judge, boolean isNumber) {
        if(!Tools.isEmpty(this.sql.toString())) this.sql.append(getKey(this));
        String[] arr = field.trim().split("\\.");
        String key = arr.length == 1? field: arr[1];
        String column = arr.length == 1? getTableKey(key): field;
        String value = getValue(key, obj);
        if(IS_EMPTY.equals(judge)){
            this.sql.append(LPAREN).append(column).append(IS_E).append(OR).append(column).append(IS_N).append(RPAREN);
        }else if(IS_NOT_EMPTY.equals(judge)){
            this.sql.append(column).append(ISN_E).append(AND).append(column).append(ISN_N);
        }else if(IN.equals(judge)){
            this.sql.append(column).append(IN).append(LPAREN).append(value).append(RPAREN);
        }else if(NIN.equals(judge)){
            this.sql.append(column).append(NIN).append(LPAREN).append(value).append(RPAREN);
        }else if(DATE.equals(judge)){
            String dateFrom = getValue(key + _FROM, obj);
            if(!Tools.isEmpty(dateFrom)) {
                String dateTo = getValue(key + _TO, obj);
                if(Tools.isEmpty(dateTo)) {
                    this.sql.append(column).append(GE).append(SQM).append(dateFrom).append(DATE_0).append(SQM).append(AND).append(column).append(LE).append(SQM).append(dateFrom).append(DATE_24).append(SQM);
                }else {
                    this.sql.append(column).append(GE).append(SQM).append(dateFrom).append(DATE_0).append(SQM).append(AND).append(column).append(LE).append(SQM).append(dateTo).append(DATE_24).append(SQM);
                }
            }
        }else if(SN.equals(judge)){
            String from = getValue(key + _FROM, obj);
            if(!Tools.isEmpty(from)) {
                String to = getValue(key + _TO, obj);
                if(Tools.isEmpty(to)) {
                    this.sql.append(column).append(LIKE).append(SQM).append(PCT).append(from).append(PCT).append(SQM);
                }else {
                    this.sql.append(column).append(GE).append(SQM).append(from).append(SQM).append(AND).append(column).append(LE).append(SQM).append(to).append(SQM);
                }
            }
        }else{
            if(!Tools.isEmpty(value)) {
                if(isNumber) {
                    this.sql.append(column).append(judge).append(value);
                }else {
                    if(LIKE.equals(judge)) {
                        this.sql.append(column).append(LIKE).append(SQM).append(PCT).append(value).append(PCT).append(SQM);
                    }else if(NLIKE.equals(judge)) {
                        this.sql.append(column).append(NLIKE).append(SQM).append(PCT).append(value).append(PCT).append(SQM);
                    }else {
                        this.sql.append(column).append(judge).append(SQM).append(value).append(SQM);
                    }
                }
            }
        }
        return (T)this;
    }
    public String getTableKey(String key) {
        if(!Tools.isEmpty(this.columns)) {
            String column = this.columns.getString(key);
            if(Tools.isEmpty(column)) {
                String _column = this.columns.getString("*");
                if(!Tools.isEmpty(_column)) {
                    return _column + key;
                }
            }else {
                return column;
            }
        }
        return key;
    }
    public String getValue(String key, Object obj) {
        if(Tools.isEmpty(obj)) {
            return null;
        }else if(obj instanceof JSONObject){
            return escape(((JSONObject)obj).getString(key).trim());
        }else if(obj.getClass().isArray()){
            Object[] objs = (Object[]) obj;
            boolean isString = (obj instanceof String[]);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < objs.length; i++) {
                if(i != 0){
                    sb.append(COMMA);
                }
                if(isString){
                    sb.append(SQM).append(escape(objs[i].toString())).append(SQM);
                }else{
                    sb.append(objs[i]);
                }
            }
            return sb.toString();
        }else {
            return escape(obj.toString().trim());
        }
    }
    public static String escape(String str) {
        if(str == null) return null;
        return str.replaceAll("\"", "&quot;").replaceAll("'", "&apos;").replaceAll(" ", "&nbsp;")
                .replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\n", "<br>");
    }
    public String getKey(Condition<T> t){
        if(t instanceof And){
            return AND;
        }else if(t instanceof Or){
            return OR;
        }else{
            return this.cond;
        }
    }
    public String getSql(){
        return this.sql.toString();
    }
}
