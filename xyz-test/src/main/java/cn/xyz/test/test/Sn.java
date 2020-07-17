package cn.xyz.test.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.tools.Tools;

public class Sn {
	public static String RANGE_DEFAULT = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static Integer JUMP_DEFAULT = 1;
	public static Integer LIMIT_DEFAULT = 50000;
	/**
	 * 创建序列号
	 * @param sn 序列号
	 * @param range 进制范围
	 * @param jump 跳号
	 * @param endIndex 结束位置
	 * @param beginIndex 开始位置
	 * @return String
	 * @throws Exception 
	 */
	public static String createSn(String sn, String range, Integer jump, Integer endIndex, Integer beginIndex) throws Exception{
		if(Tools.isEmpty(sn)) return null;
		if(jump == null) jump = Sn.JUMP_DEFAULT;
		if(jump == 0) return sn;
		if(range == null) range = Sn.RANGE_DEFAULT;
		Integer radix = range.length();
		if(jump > radix || jump < -radix) return null;
		if(endIndex == null) endIndex = sn.length()-1;
		if(beginIndex == null) beginIndex = 0;
		if(endIndex >= sn.length() || endIndex < 0) return null;
		if(beginIndex >= sn.length() || beginIndex < 0) return null;
		if(beginIndex > endIndex) return null;
		for (int i = endIndex; i >= 0; i--) {
			String ch = sn.substring(i, i+1);
			int index = range.indexOf(ch);
			sn = sn.substring(0, i) + range.substring((radix+index+jump)%radix, (radix+1+index+jump)%radix==0?radix:(radix+1+index+jump)%radix) + sn.substring(i+1, sn.length());
			if(((range.indexOf(ch) + jump) > -1 && (range.indexOf(ch) + jump) < radix) || i == beginIndex) {
				break;
			}
			if(jump > 0) {
				jump = 1;
			}else{
				jump = -1;
			}
		}
		return sn;
	}
	/**
	 * 创建序列号集合
	 * @param snFrom 开始编号
	 * @param snTo 结束编号
	 * @param number 需要数量
	 * @param range 进制范围
	 * @param position 改变位置
	 * @param jump 跳号
	 * @param limit 限制数量
	 * @return JSONObject: {msg:"error"} || {rows:[JSONArray]}
	 * @throws Exception 
	 * @throws NumberFormatException 
	 */
	public static JSONObject createSnList(String snFrom, String snTo, Integer number, String range, String position, Integer jump, Integer limit,String field) throws NumberFormatException, Exception {
		JSONObject result = new JSONObject();
		Integer endIndex = null;//流水号的最后一个位置
		Integer beginIndex = null;
		if(Tools.isEmpty(range)) range = Sn.RANGE_DEFAULT;
		if(limit == null) limit = Sn.LIMIT_DEFAULT;
		if(jump == null) jump = Sn.JUMP_DEFAULT;
		if(Tools.isEmpty(snFrom)) {
			result.put("status", false);
			result.put("msg", "许可证开始编号不能为空");
			return result;
		}
		
		if(!Tools.isEmpty(position)) {
			String[] positions = position.split("-");
			if(positions.length == 2) {
				endIndex = Integer.parseInt(positions[1])-1;
				beginIndex = Integer.parseInt(positions[0])-1;
			}else if(positions.length == 1){
				endIndex = snFrom.length()-1;
				beginIndex = snFrom.length() - Integer.parseInt(positions[0]);
			}else {
				result.put("status", false);
				result.put("msg", "变量位置不合法（合法的参数为：数字-数字）");
				return result;
			}
		}else {
			beginIndex = 0;
			endIndex = snFrom.length()-1;
		}
		if(!snFrom.substring(beginIndex, endIndex+1).matches("["+range+"]{"+(endIndex-beginIndex+1)+"}")) {
			result.put("status", false);
			result.put("msg", "许可证开始编号不合法");
			return result;
		}
		JSONArray rows = new JSONArray();
		JSONObject item2 = new JSONObject();
		item2.put(field, snFrom);
		rows.add(item2);
		String sn_temp = snFrom;
		if(number != null) {
			if(number > limit) {
				result.put("status", false);
				result.put("msg", "所需数量不能超过" + limit);
				return result;
			}else {
				for (int i = 0; i < number-1; i++) {
					sn_temp = createSn(sn_temp, range, jump, endIndex, beginIndex);
					if(sn_temp.substring(beginIndex, endIndex+1).compareTo(snFrom.substring(beginIndex, endIndex+1))<=0) {
						break;
					}
					JSONObject item = new JSONObject();
					item.put(field, sn_temp);
					rows.add(item);
					//改变位置达到最大时跳出
					/*if((sn_temp.substring(beginIndex, endIndex+1)).matches("["+range.substring(range.length()-1, range.length())+"]{"+(endIndex+1-beginIndex)+"}")) {
						break;
					}*/
				}
			}
		}else if(!Tools.isEmpty(snTo)) {
			if(!snTo.substring(beginIndex, endIndex+1).matches("["+range+"]{"+(endIndex-beginIndex+1)+"}") || snFrom.length() != snTo.length()) {
				result.put("status", false);
				result.put("msg", "许可证编号不合法（许可证不在规则范围内或开始结束编号长度不一致）");
				return result;
			}else if(!snFrom.substring(0, beginIndex).equals(snTo.substring(0, beginIndex)) ||
						!snFrom.substring(endIndex+1, snFrom.length()).equals(snTo.substring(endIndex+1, snFrom.length()))) {
				result.put("status", false);
				result.put("msg", "变量位置不合法（非变量位置不同）");
				return result;
			}else {
				for (int i = 0; true; i++) {
					sn_temp = createSn(sn_temp, range, jump, endIndex, beginIndex);
					if(sn_temp.substring(beginIndex, endIndex+1).compareTo(snFrom.substring(beginIndex, endIndex+1))<=0) {
						break;
					}
					JSONObject item = new JSONObject();
					item.put(field, sn_temp);
					rows.add(item);
					if(snTo.equals(sn_temp)) {
						break;
					}else {
						if(i == limit-2) {
							result.put("status", false);
							result.put("msg", "生成数量已超过" + limit);
							return result;
						}
					}
				}
			}
		}
		result.put("status", true);
		result.put("rows", rows);
		return result;
	}
	
	/**
	 * 增加大数后的某个号
	 * @param sn 序列号
	 * @param range 进制范围
	 * @param number 大数
	 * @return
	 * @throws Exception 
	 */
	public static String addSn(String sn, String range, Integer number) throws Exception {
		if(Tools.isEmpty(sn)) return null;
		if(Tools.isEmpty(range)) {
			range = Sn.RANGE_DEFAULT;
		}
		Integer radix = range.length();
		if(number == null) number = 1;
		for (int i = 0; i < sn.length(); i++) {
			if(number == 0) break;
			int jump = number % radix;
			sn = createSn(sn, range.substring(0, radix), jump, sn.length()-1-i, null);
			number = number / radix;
		}
		return sn;
	}
	public static String addSn(String sn,  Integer radix, Integer number) throws Exception {
		return addSn(sn, Sn.RANGE_DEFAULT.substring(0, radix), number);
	}
	public static String createSn(String sn, Integer radix, Integer num) throws Exception{
		return createSn(sn, Sn.RANGE_DEFAULT.substring(0, radix), num, null, null);
	}
	public static JSONObject createSnList(JSONObject row) throws NumberFormatException, Exception {
		return createSnList(row.getString("snFrom"), row.getString("snTo"), row.getInteger("number"), row.getString("range"), row.getString("position"), row.getInteger("jump"), row.getInteger("limit"), row.getString("field"));
	}
	
}
