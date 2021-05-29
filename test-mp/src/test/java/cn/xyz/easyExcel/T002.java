package cn.xyz.easyExcel;

import cn.xyz.mapper.UserMapper;
import cn.xyz.pojo.FillData;
import cn.xyz.pojo.FillData2;
import cn.xyz.pojo.Student;
import cn.xyz.tool.StudentListener;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest//测试的包名要与启动MainApplication一致，否则要加(classes = MainApplication.class)
public class T002 {
	@Resource
	private UserMapper userMapper;

	@Test
	public void test1() {
		String tempalte = "E:\\temp\\test1.xlsx";
		ExcelWriterBuilder write = EasyExcel.write("E:\\temp\\test.xlsx", FillData.class).withTemplate(tempalte);
		ExcelWriterSheetBuilder sheet1 = write.sheet();
		List<Student> list = init();
		sheet1.doFill(list);
	}

	@Test
	public void test2() {
		String tempalte = "E:\\temp\\test2.xlsx";
		ExcelWriter build = EasyExcel.write("E:\\temp\\test.xlsx", FillData.class)
				.withTemplate(tempalte).build();
		WriteSheet sheet1 = EasyExcel.writerSheet().build();
		FillConfig fc = FillConfig.builder().forceNewRow(true).build();

		List<FillData> list = init1();
		build.fill(list,fc, sheet1);
		Map<String, Object> map = new HashMap<>();
		map.put("aa","xxx");
		build.fill(map, sheet1);
		build.fill(list,fc, sheet1);
		build.fill(map, sheet1);

		build.finish();
	}

	@Test
	public void test3() {
		String tempalte = "E:\\temp\\test2.xlsx";
		ExcelWriter build = EasyExcel.write("E:\\temp\\test.xlsx", FillData.class)
				.withTemplate(tempalte).build();
		WriteSheet sheet1 = EasyExcel.writerSheet().build();
		FillConfig fc = FillConfig.builder().forceNewRow(true).build();

		List<FillData> list = init1();
		build.fill(list,fc, sheet1);
		Map<String, Object> map = new HashMap<>();


		List<FillData2> list2 = init2();
		build.fill(list2,fc, sheet1);
		Map<String, Object> map2 = new HashMap<>();
		map2.put("aa2","xxx2");
		//build.fill(map2, sheet1);

		//map.put("aa","xxx");
		map.put("aa2","xxx2");
		//build.fill(map, sheet1);

		build.finish();
	}
	private static List<FillData> init1(){
		List<FillData> students = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			FillData s = new FillData();
			s.setName("姓名"+i);
			s.setAge(i);
			students.add(s);
		}
		return students;
	}
	private static List<FillData2> init2(){
		List<FillData2> students = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			FillData2 s = new FillData2();
			s.setName2("姓名"+i);
			s.setAge2(i);
			students.add(s);
		}
		return students;
	}
	private static List<Student> init(){
		List<Student> students = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			Student s = new Student();
			s.setName("姓名"+i);
			s.setGender("班级"+i);
			s.setBirthday("2000-01-01");
			s.setId(i/2.0);
			students.add(s);
		}
		return students;
	}
}
