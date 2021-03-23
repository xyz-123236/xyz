package cn.xyz.easyExcel;

import cn.xyz.mapper.UserMapper;
import cn.xyz.pojo.FillData;
import cn.xyz.pojo.Student;
import cn.xyz.tool.CustomCellWriteHandler;
import cn.xyz.tool.CustomSheetWriteHandler;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.merge.LoopMergeStrategy;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
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
public class T004 {
	@Resource
	private UserMapper userMapper;

	@Test
	public void test1() {
		ExcelWriterBuilder write = EasyExcel.write("E:\\temp\\test.xlsx");
		WriteCellStyle headWriteCellStyle = new WriteCellStyle();
		// 背景设置为红色
		headWriteCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
		headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.RIGHT);
		LoopMergeStrategy loopMergeStrategy = new LoopMergeStrategy(3, 0);

		HorizontalCellStyleStrategy horizontalCellStyleStrategy =
				new HorizontalCellStyleStrategy(new WriteCellStyle(),headWriteCellStyle);
		ExcelWriterSheetBuilder sheet1 = write.registerWriteHandler(horizontalCellStyleStrategy).registerWriteHandler(loopMergeStrategy).sheet();


		List<List<String>> list = new ArrayList<List<String>>(){{
			add(new ArrayList<String>(){{add("t1");add("t4");}});
			add(new ArrayList<String>(){{add("t2");}});
			add(new ArrayList<String>(){{add("t3");}});
		}};
		List<List<String>> list2 = new ArrayList<List<String>>(){{
			add(new ArrayList<String>(){{add("t11");add("t12");add("t13");}});
			add(new ArrayList<String>(){{add("t21");add("t22");add("t23");}});
			add(new ArrayList<String>(){{add("t31");add("t32");}});
			add(new ArrayList<String>(){{add("t41");add("t42");add("t43");}});
		}};
		sheet1.head(list);
		sheet1.doWrite(list2);
	}

	@Test
	public void customHandlerWrite() {
		ExcelWriterBuilder write = EasyExcel.write("E:\\temp\\test.xlsx");
		// 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
		//write.registerWriteHandler(new CustomSheetWriteHandler())
				//.registerWriteHandler(new CustomCellWriteHandler()).sheet("模板").doWrite(data());
	}
}
