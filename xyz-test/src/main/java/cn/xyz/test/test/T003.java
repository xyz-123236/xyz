package cn.xyz.test.test;

import java.util.List;

import com.alibaba.fastjson.JSONObject;

public class T003 {

	public static void main(String[] args) {
		//JSONObject obj = new JSONObject();
		//System.out.println(obj.getDoubleValue("aa"));
		String aa = "2020-06-10";
		int a = Integer.parseInt(aa.replaceAll("-", "").substring(0, 6));
		System.out.println(a);
		System.out.println(a/100);
		System.out.println(a%100);
		//System.out.println(aa.substring(-1));
		//print(52);
		String test ="INSERT INTO pass.dbo.employee\r\n" + 
				"(empno, revision, emp_name, sex, birthday, id_no, prod_ctr, asm_line, positionno, worktyno, turnno, basewage, wagetyno, rankno, is_married, spou_no, edu_sch, spec, eduno, in_date, salacaltyno, is_on_test, is_muti_cont, cont_end_dt, off_dt, offno, is_beded, is_insured, is_live_in, addr_home_p, addr_home_c, addr_home_t, addr_now_p, addr_now_c, addr_now_t, phoneno, cont_man, eft_date, rec_status, ent_by, ent_date, chk_by, chk_date, postby, pdate, old_rankno, dormno, roomno, bedno, is_dg, is_on_practice, [timestamp], id_eft_d, company, station, work_type, [fusion], allowance, bonus, reltype, telephone, relation_tel, edu_copy, nation, zip_code, english_name, emptype, if_rnd, cont_start_dt, cont_limit, prob_dt, cnt_formal, arbpl, arbpl_efdate)\r\n" + 
				"VALUES('0132117   ', 6, '             ', '0', '1989-05-11 00:00:00.000', '420881198905117147  ', 'P051  ', NULL, 'POS056', '      ', 'RR    ', 1720.0000, 'BST002', 'RAK103', '0', NULL, '華中科技大學        ', '會計                ', 'EDU005', '2017-11-01 00:00:00.000', 'SCT002', '0', '0', '2020-10-31 00:00:00.000', NULL, NULL, '1', '0', '1', '湖北省              ', '鐘祥市', '張集鎮牌坊村三組16號', NULL, NULL, NULL, NULL, NULL, '2019-05-01 00:00:00.000', 'P', 'HRD_LY    ', '2019-05-14 15:12:57.920', NULL, NULL, 'HRD_LY    ', '2019-05-14 15:15:57.856', 'CAR002', NULL, NULL, NULL, '0', '0', 0x000000016BAB29F0, '2018-01-23 00:00:00.000', 'A', NULL, 'A', 'N', 1000, 1483, 'P', '15921138375    ', '15019633247    ', 'Y    ', '漢族', '431900', NULL, ' ', NULL, '2017-11-01 00:00:00.000', '3                   ', NULL, NULL, NULL, NULL);\r\n" + 
				"";
		
	}
	public static void print(Object msg) {
		System.out.println("Object"+msg);
	}
	public static void print(String msg) {
		System.out.println("String"+msg);
	}
	
}
