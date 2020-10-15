package cn.xyz.common.tools;

import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.exception.CustomException;
import cn.xyz.common.pojo.Sn;
import cn.xyz.common.tools.Tools;

public class ToolsSn {
	public static String RANGE_DEFAULT = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static Integer JUMP_DEFAULT = 1;
	public static Integer LIMIT_DEFAULT = 50000;
	public static JSONObject createSn(Sn sn) throws Exception {
		check(sn);
		return createSn(sn, ToolsSn.RANGE_DEFAULT.substring(0, radix), number);
	}
	public static Sn check(Sn sn) throws CustomException {
		String position = sn.getPosition();
		String snFrom = sn.getSnFrom();
		if(Tools.isEmpty(snFrom)) throw new CustomException("编号不能为空");
		if(!Tools.isEmpty(position)) {
			String[] positions = position.split("-");
			if(positions.length == 2) {
				sn.setEndIndex(Integer.parseInt(positions[1])-1);
				sn.setBeginIndex(Integer.parseInt(positions[0])-1);
			}else if(positions.length == 1){//一个数表示后几位
				sn.setEndIndex(snFrom.length()-1);
				sn.setBeginIndex(snFrom.length() - Integer.parseInt(positions[0]));
			}else {
				throw new CustomException("流水号位置不合法");
			}
		}else {
			sn.setBeginIndex(0);
			sn.setEndIndex(snFrom.length()-1);
		}



		if(number == 0) return Tools.success(sn);

		if(beginIndex >= sn.length() || beginIndex < 0) return Tools.error("开始位置超出范围");
		if(endIndex >= sn.length() || endIndex < 0) return Tools.error("结束位置超出范围");
		if(beginIndex > endIndex) return Tools.error("开始位置不能大于结束位置");
		String regex = "["+range+"]{"+(endIndex-beginIndex+1)+"}";
		if(!Pattern.matches(regex, sn.substring(beginIndex, endIndex+1))) return Tools.error("编号流水号不合法");
		//if(!sn.substring(beginIndex, endIndex+1).matches("["+range+"]{"+(endIndex-beginIndex+1)+"}")) return null;
		Integer radix = range.length();
		return sn;
	}
	public static JSONObject createSn(String sn, Integer radix, Integer number) throws Exception {
		return createSn(sn, ToolsSn.RANGE_DEFAULT.substring(0, radix), number);
	}
	public static JSONObject createSn(String sn, Integer radix, Integer number, String position) throws Exception {
		return createSn(sn, ToolsSn.RANGE_DEFAULT.substring(0, radix), number, position);
	}
	public static JSONObject createSn(String sn, Integer radix, Integer number, Integer beginIndex, Integer endIndex) throws Exception {
		return createSn(sn, ToolsSn.RANGE_DEFAULT.substring(0, radix), number, beginIndex, endIndex);
	}
	public static JSONObject createSn(String sn, String range, Integer number) throws Exception {
		return createSn(sn, range, number, null);
	}
	public static JSONObject createSn(String sn, String range, Integer number, String position) throws Exception {
		JSONObject obj = position(sn, position);
		if(!obj.getBooleanValue("status")) {
			return obj;
		}
		return createSn(sn, range, number, obj.getJSONObject("data").getInteger("beginIndex"), obj.getJSONObject("data").getInteger("endIndex"));
	}
	/**
	 * 创建序列号
	 * @param sn 序列号
	 * @param range 进制范围
	 * @param number 跳号数量
	 * @param beginIndex 开始位置
	 * @param endIndex 结束位置
	 * @return String
	 * @throws Exception 
	 */
	public static JSONObject createSn(String sn, String range, Integer number, Integer beginIndex, Integer endIndex) throws Exception {
		if(Tools.isEmpty(sn)) return Tools.error("编号不能为空");
		if(Tools.isEmpty(range)) range = ToolsSn.RANGE_DEFAULT;
		if(number == null) number = JUMP_DEFAULT;
		if(number == 0) return Tools.success(sn);
		if(endIndex == null) endIndex = sn.length()-1;
		if(beginIndex == null) beginIndex = 0;
		if(beginIndex >= sn.length() || beginIndex < 0) return Tools.error("开始位置超出范围");
		if(endIndex >= sn.length() || endIndex < 0) return Tools.error("结束位置超出范围");
		if(beginIndex > endIndex) return Tools.error("开始位置不能大于结束位置");
		String regex = "["+range+"]{"+(endIndex-beginIndex+1)+"}";
		if(!Pattern.matches(regex, sn.substring(beginIndex, endIndex+1))) return Tools.error("编号流水号不合法");
		//if(!sn.substring(beginIndex, endIndex+1).matches("["+range+"]{"+(endIndex-beginIndex+1)+"}")) return null;
		Integer radix = range.length();
		int carry = 0;//进位
		for (int i = endIndex; i >= beginIndex; i--) {
			int jump = number % radix + carry;
			String ch = sn.substring(i, i+1);
			int index = range.indexOf(ch);
			sn = sn.substring(0, i) + range.charAt((radix+index+jump)%radix) + sn.substring(i+1, sn.length());
			carry = 0;
			if(((range.indexOf(ch) + jump) < 0 || (range.indexOf(ch) + jump) >= radix)) {
				if(i == beginIndex){
					return Tools.error("生成的编号超出了最大范围");
				}
				if(jump > 0) {
					carry = 1;
				}else{
					carry = -1;
				}
			}
			number = number / radix;
			if(number == 0 && carry == 0) {
				return Tools.success(sn);
			}
		}
		return Tools.success(sn);
	}
	
	public static JSONObject createSnList(JSONObject row) throws NumberFormatException, Exception {
		return createSnList(row.getString("snFrom"), row.getString("snTo"), row.getInteger("number"), row.getString("range"), row.getString("position"), row.getInteger("jump"), row.getInteger("limit"), row.getString("field"));
	}
	public static JSONObject createSnList(String snFrom, String snTo, Integer number, Integer radix, String position, Integer jump, Integer limit,String field) throws NumberFormatException, Exception {
		return createSnList(snFrom, snTo, number, ToolsSn.RANGE_DEFAULT.substring(0, radix), position, jump, limit, field);
	}
	public static JSONObject createSnList(String snFrom, String snTo, Integer number, Integer radix, Integer beginIndex, Integer endIndex, Integer jump, Integer limit,String field) throws NumberFormatException, Exception {
		return createSnList(snFrom, snTo, number, ToolsSn.RANGE_DEFAULT.substring(0, radix), beginIndex, endIndex, jump, limit, field);
	}
	public static JSONObject createSnList(String snFrom, String snTo, Integer number, String range, String position, Integer jump, Integer limit,String field) throws NumberFormatException, Exception {
		JSONObject obj = position(snFrom, position);
		if(!obj.getBooleanValue("status")) {
			return obj;
		}
		return createSnList(snFrom, snTo, number, range, obj.getJSONObject("data").getInteger("beginIndex"), obj.getJSONObject("data").getInteger("endIndex"), jump, limit, field);
	}

	/**
	 * 创建序列号集合
	 * @param snFrom 开始编号
	 * @param snTo 结束编号
	 * @param number 需要数量
	 * @param range 进制范围
	 * @param beginIndex 流水号开始位置
	 * @param endIndex 流水号结束位置
	 * @param jump 跳号
	 * @param limit 限制数量
	 * @param field 有则生成键值对，没有直接添加到数组
	 * @return JSONObject: {msg:"error"} || {data:[JSONArray]}
	 * @throws NumberFormatException
	 * @throws Exception
	 */
	public static JSONObject createSnList(String snFrom, String snTo, Integer number, String range, Integer beginIndex, Integer endIndex, Integer jump, Integer limit,String field) throws NumberFormatException, Exception {
		if(Tools.isEmpty(snFrom)) return Tools.error("开始编号不能为空");
		if(number == null) number = JUMP_DEFAULT;
		if(!snFrom.substring(beginIndex, endIndex+1).matches("["+range+"]{"+(endIndex-beginIndex+1)+"}")) return Tools.error("开始编号流水号不合法");
		if(!Tools.isEmpty(number)) {
			//
		}else if(!Tools.isEmpty(snTo)){
			if(!snTo.substring(beginIndex, endIndex+1).matches("["+range+"]{"+(endIndex-beginIndex+1)+"}")) return Tools.error("结束编号流水号不合法");
			if(snFrom.compareTo(snTo) > 0) return Tools.error("开始编号不能大于结束编号");
			if(!snFrom.substring(0, beginIndex).equals(snTo.substring(0, beginIndex)) 
					|| !snFrom.substring(endIndex+1, snFrom.length()).equals(snTo.substring(endIndex+1, snFrom.length()))) return Tools.error("开始编号和结束编号的非流水号位置不相同");
			number = diffSn(snFrom, snTo, range, beginIndex, endIndex)/jump;
		}else {
			return Tools.error("数量和结束编号不能都为空");
		}
		if(limit == null) limit = ToolsSn.LIMIT_DEFAULT;
		if(Tools.isEmpty(range)) range = ToolsSn.RANGE_DEFAULT;
		if(number > limit) return Tools.error("创建数量不能大于"+limit);
		
		JSONArray rows = new JSONArray();
		if(Tools.isEmpty(field)) {
			rows.add(snFrom);
		}else {
			JSONObject item = new JSONObject();
			item.put(field, snFrom);
			rows.add(item);
		}
		String sn_temp = snFrom;
		for (int i = 0; i < number-1; i++) {
			JSONObject obj = createSn(sn_temp, range, jump, beginIndex, endIndex);
			if(!obj.getBooleanValue("status")) {
				return obj;
			}
			sn_temp = obj.getString("data");
			if(Tools.isEmpty(field)) {
				rows.add(sn_temp);
			}else {
				JSONObject item = new JSONObject();
				item.put(field, sn_temp);
				rows.add(item);
			}
		}
		return Tools.success(rows);
	}
	
	public static Integer diffSn(String snFrom, String snTo, String range, Integer beginIndex, Integer endIndex) throws Exception {
		if(!snFrom.substring(beginIndex, endIndex+1).matches("["+range+"]{"+(endIndex-beginIndex+1)+"}")) return null;
		if(!snTo.substring(beginIndex, endIndex+1).matches("["+range+"]{"+(endIndex-beginIndex+1)+"}")) return null;
		if(snFrom.length() != snTo.length()) return null;
		int numFrom = 0;
		int numTo = 0;
		Integer radix = range.length();
		Integer radixMult = 1;
		for (int i = endIndex; i >= beginIndex; i--) {
			numFrom += range.indexOf(snFrom.charAt(i))*radixMult;
			numTo += range.indexOf(snTo.charAt(i))*radixMult;
			radixMult *= radix;
		}
		return numTo-numFrom+1;
	}
	public static JSONObject position(String sn, String position) throws Exception {
		if(Tools.isEmpty(sn)) return Tools.error("编号不能为空");
		JSONObject obj = new JSONObject();
		if(!Tools.isEmpty(position)) {
			String[] positions = position.split("-");
			if(positions.length == 2) {
				obj.put("endIndex", Integer.parseInt(positions[1])-1);
				obj.put("beginIndex", Integer.parseInt(positions[0])-1);
			}else if(positions.length == 1){//一个数表示后几位
				obj.put("endIndex", sn.length()-1);
				obj.put("beginIndex", sn.length() - Integer.parseInt(positions[0]));
			}else {
				return Tools.error("流水号位置不合法");
			}
		}else {
			obj.put("beginIndex", 0);
			obj.put("endIndex", sn.length()-1);
		}
		return Tools.success(obj);
	}
	public static void main(String[] args) {
		try {
			/*if(Pattern.matches("[0-9a-zA-Z]{6}", "123456")) {
				System.out.println("ok1");
			}else {
				System.out.println("fa1");
			}
			System.out.println(-15%6);*/
			
			//System.out.println(Sn.RANGE_DEFAULT.charAt(35));
			/*System.out.println(createSn("0186FK-0000000DK", "0123456789", 1, "8-14"));
			System.out.println(createSn("0186FK-0000000DK", "0123456789", 2500, "8-14"));
			System.out.println(createSn("0186FK-0999999DK", "0123456789", 1, "8-14"));
			System.out.println(createSn("0186FK-9999999DK", "0123456789", 5, "8-14"));
			System.out.println(createSn("0186FK-0999999DK", "0123456789", 2354, "8-14"));
			System.out.println(createSn("0186FK-0999999DK", "0123456789", -32, "8-14"));
			System.out.println(createSn("0186FK-0000000DK", "0123456789", -1, "8-14"));
			System.out.println(createSn("0186FK-0000888DK", "0123456789", 111, "8-14"));
			System.out.println(createSn("0186FK-0000889DK", "0123456789", 121, "8-14"));
			System.out.println(diffSn("0186FK-0003526DK", "0186FK-0004253DK", "0123456789", 7, 13));*/
			JSONObject obj = createSnList("0186FK-0000000DK", null, 100, 10, 7, 13, 1, 50000, null);
			JSONArray data = obj.getJSONArray("data");
			for (int i = 0; i < data.size(); i++) {
				System.out.println(data.get(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
