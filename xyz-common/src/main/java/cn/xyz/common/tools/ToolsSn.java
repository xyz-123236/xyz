package cn.xyz.common.tools;

import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.xyz.common.exception.CustomException;
import cn.xyz.common.pojo.Result;
import cn.xyz.common.pojo.Sn;
import cn.xyz.common.tools.Tools;

public class ToolsSn {
	
	public static JSONArray createSn(Sn sn) throws Exception {
		check(sn);
		JSONArray rows = new JSONArray();
		if(sn.getNumber() == 1) {
			sn.setSnFrom(createSn2(sn));
			return put(rows, sn);
		}
		for (int i = 0; i < sn.getNumber(); i++) {
			put(rows, sn);
			sn.setSnFrom(createSn2(sn));
		}
		return rows;
	}
	public static String createSn2(Sn sn) throws Exception {
		check(sn);
		Integer radix = sn.getRange().length();
		String snFrom = sn.getSnFrom();
		Integer jump = sn.getJump();
		if(jump == 0) return snFrom;
		int carry = 0;//进位
		for (int i = sn.getEndIndex(); i >= sn.getBeginIndex(); i--) {
			int jump_t = jump % radix + carry;
			String ch = snFrom.substring(i, i+1);
			int index = sn.getRange().indexOf(ch);
			snFrom = snFrom.substring(0, i) + sn.getRange().charAt((radix+index+jump_t)%radix) + snFrom.substring(i+1, snFrom.length());
			carry = 0;
			if(((sn.getRange().indexOf(ch) + jump_t) < 0 || (sn.getRange().indexOf(ch) + jump_t) >= radix)) {
				if(i == sn.getBeginIndex()){
					throw new CustomException("生成的编号超出了最大范围");
				}
				if(jump_t > 0) {
					carry = 1;
				}else{
					carry = -1;
				}
			}
			jump = jump / radix;
			if(jump == 0 && carry == 0) {
				return snFrom;
			}
		}
		return snFrom;
	}
	public static Sn check(Sn sn) throws CustomException {
		String snFrom = sn.getSnFrom();
		if(Tools.isEmpty(snFrom)) throw new CustomException("编号不能为空");
		String position = sn.getPosition();
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
		}else if(Tools.isEmpty(sn.getBeginIndex())) {
			sn.setBeginIndex(0);
			sn.setEndIndex(snFrom.length()-1);
		}
		if(!Tools.isEmpty(sn.getRadix())) sn.setRange(sn.getRange().substring(0, sn.getRadix()));
		Integer beginIndex = sn.getBeginIndex();
		Integer endIndex = sn.getEndIndex();
		if(beginIndex >= snFrom.length() || beginIndex < 0 || 
				endIndex >= snFrom.length() || endIndex < 0 ||
				beginIndex > endIndex) throw new CustomException("流水号位置不合法");
		String regex = "["+sn.getRange()+"]{"+(endIndex-beginIndex+1)+"}";
		if(!Pattern.matches(regex, snFrom.substring(beginIndex, endIndex+1))) throw new CustomException("流水号范围不合法");
		//if(!sn.substring(beginIndex, endIndex+1).matches("["+range+"]{"+(endIndex-beginIndex+1)+"}")) return null;

		if(!Tools.isEmpty(sn.getNumber())) {
			//
		}else if(!Tools.isEmpty(sn.getSnTo())){
			if(!sn.getSnTo().substring(beginIndex, endIndex+1).matches("["+sn.getRange()+"]{"+(endIndex-beginIndex+1)+"}")) throw new CustomException("结束编号流水号不合法");
			if(snFrom.compareTo(sn.getSnTo()) > 0) throw new CustomException("开始编号不能大于结束编号");
			if(!snFrom.substring(0, beginIndex).equals(sn.getSnTo().substring(0, beginIndex)) 
					|| !snFrom.substring(endIndex+1, snFrom.length()).equals(sn.getSnTo().substring(endIndex+1, snFrom.length()))) throw new CustomException("开始编号和结束编号的非流水号位置不相同");
			sn.setNumber(diffSn(snFrom, sn.getSnTo(), sn.getRange(), beginIndex, endIndex)/sn.getJump());
		}else {
			throw new CustomException("数量和结束编号不能都为空");
		}
		if(sn.getNumber() > sn.getLimit()) throw new CustomException("创建数量不能大于"+sn.getLimit());
		
		return sn;
	}

	
	public static JSONArray put(JSONArray rows, Sn sn) {
		if(Tools.isEmpty(sn.getField())) {
			rows.add(sn.getSnFrom());
		}else {
			JSONObject item = new JSONObject();
			item.put(sn.getField(), sn.getSnFrom());
			rows.add(item);
		}
		return rows;
	}
	public static Integer diffSn(String snFrom, String snTo, String range, Integer beginIndex, Integer endIndex) {
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
			int a = 1/0;
			//System.out.println(Sn.RANGE_DEFAULT.charAt(35));
			Sn sn = new Sn("0186FK-0000000DK", "0123456789", 1, "8-14");
			System.out.println(createSn(sn));
			sn = new Sn("0186FK-0000000DK", "0123456789", 2500, "8-14");
			System.out.println(createSn(sn));
			sn = new Sn("0186FK-0999999DK", "0123456789", 1, "8-14");
			System.out.println(createSn(sn));
			sn = new Sn("0186FK-9999999DK", "0123456789", 5, "8-14");
			//System.out.println(createSn(sn));
			sn = new Sn("0186FK-0999999DK", "0123456789", 2354, "8-14");
			System.out.println(createSn(sn));
			sn = new Sn("0186FK-0999999DK", "0123456789", -32, "8-14");
			System.out.println(createSn(sn));
			sn = new Sn("0186FK-0000000DK", "0123456789", -1, "8-14");
			//System.out.println(createSn(sn));
			sn = new Sn("0186FK-0000888DK", "0123456789", 111, "8-14");
			System.out.println(createSn(sn));
			sn = new Sn("0186FK-0000889DK", "0123456789", 121, "8-14");
			System.out.println(createSn(sn));
			System.out.println(diffSn("0186FK-0003526DK", "0186FK-0004253DK", "0123456789", 7, 13));
			sn.setSnFrom("0186FK-0000000DK");
			sn.setNumber(100);
			sn.setJump(1);
			sn.setRange("0123456789");
			sn.setPosition("8-14");
			System.out.println(createSn(sn));
			//JSONObject obj = createSnList("0186FK-0000000DK", null, 100, 10, 7, 13, 1, 50000, null);
			//JSONArray data = obj.getJSONArray("data");
			/*for (int i = 0; i < data.size(); i++) {
				System.out.println(data.get(i));
			}*/
		} catch (Exception e) {
			Result.error(e);
		}
		
	}
}
