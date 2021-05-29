package cn.xyz.tool;

import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.write.handler.CellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;

import java.util.List;

public class CustomCellWriteHandler implements CellWriteHandler {


    @Override
    public void beforeCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Row row, Head head, Integer integer, Integer integer1, Boolean aBoolean) {

    }

    @Override
    public void afterCellCreate(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterCellDataConverted(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, CellData cellData, Cell cell, Head head, Integer integer, Boolean aBoolean) {

    }

    @Override
    public void afterCellDispose(WriteSheetHolder writeSheetHolder, WriteTableHolder writeTableHolder, List<CellData> list, Cell cell, Head head, Integer integer, Boolean aBoolean) {
        // 这里可以对cell进行任何操作
        System.out.println("第{}行，第{}列写入完成。"+cell.getRowIndex()+ cell.getColumnIndex());
        if (aBoolean && cell.getColumnIndex() == 0) {
            //CreationHelper createHelper = writeSheetHolder.getSheet().getWorkbook().getCreationHelper();
            //Hyperlink hyperlink = createHelper.createHyperlink(HyperlinkType.URL);
            //hyperlink.setAddress("https://github.com/alibaba/easyexcel");
            //cell.setHyperlink(hyperlink);
            //CellStyle cellStyle = writeSheetHolder.getSheet().getc
            //cell.setCellStyle();
        }
    }
}
