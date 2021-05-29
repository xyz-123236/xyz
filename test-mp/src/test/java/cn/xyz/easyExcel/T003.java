package cn.xyz.easyExcel;

import cn.xyz.pojo.PlatformStatisParamRespData;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class T003 {
    /**
     * @Description: 拼接表头
     * @Param: paramInfo :表头信息
     * @return: java.util.List<cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity>
     * @Author: peikunkun
     * @Date: 2019/12/26 0026 上午 10:42
     *//*
    private static List<ExcelExportEntity> dynamicNewAddExcel(Map<String, PlatformStatisParamRespData> paramInfo) {
        //表头的集合，用于添加表头
        List<ExcelExportEntity> entityList = new ArrayList<>();

        //ExcelExportEntity构造参数【第一个是列名头的统计字段,第二个是需要指定的一个key在填充数据的时候是需要根据这个key进行填充值,第三个参数是列宽】
        ExcelExportEntity platformXh = new ExcelExportEntity("统计字段1", "statisKey1", 30);
        //列的合并(纵向列的同名称会进行合并,效果见上图的平台名称的变化)
        platformXh.setMergeVertical(true);
        entityList.add(platformXh);

        ExcelExportEntity statisDateXh = new ExcelExportEntity("统计字段2", "statisKey2", 30);
        entityList.add(statisDateXh);

        //参数信息--[用于动态拼接列头]
        final Iterator<String> iterator = paramInfo.keySet().iterator();
        while (iterator.hasNext()) {
            final String paramKeyStr = iterator.next();
            final String paramNameStr = paramInfo.get(paramKeyStr).getDataName();
            //列头由参数汉字名称,参数key为列key
            entityList.add(new ExcelExportEntity(paramNameStr, paramKeyStr, 30));
        }
        return entityList;
    }


    *//**
     * @Description: 拼接数据
     * @Param: statisData :拼接数据
     * @Author: peikunkun
     * @Date: 2019/12/26 0026 上午 10:42
     *//*
    private static List<Map<String, Object>> dynamicListDataByKey(List<PlatformIncomeRespDTO> statisData) {
        //参数类型
        final Set<String> statisParamKey = statisData.get(0).getParamInfo().keySet();
        final List<String> statisDate = statisData.get(0).getStatisDate();
        final int platformNum = statisData.size();

        //最终的数据
        List<Map<String, Object>> datas = new ArrayList<>();
        for (int i = 0; i < platformNum; i++) {
            for (int j = 0; j < statisDate.size(); j++) {
                Map<String, Object> hashMap = new LinkedHashMap<>(10);
                //这个是依据key进行数据的填充,(根据前面填写的statisKey1进行填充数据)
                hashMap.put("statisKey1", statisData.get(i).getPlatformNickName());
                String statisDateStr = statisDate.get(j);
                //这个是依据key进行数据的填充,(根据前面填写的statisKey2进行填充  数据)
                hashMap.put("statisKey2", statisDateStr);
                //参数的验证
                for (String paramKey : statisParamKey) {
                    for (BiPlatformStatisRespDTO paramData : statisData.get(i).getStatisData().get(j)) {
                        if (paramKey.equals(paramData.getParamKey())) {
                            hashMap.put(paramData.getParamKey(), paramData.getValue() + "(" + paramData.getRateValue() + ")");
                        }
                    }
                }
                datas.add(hashMap);
            }
        }
        return datas;
    }


    @Test
    public void Administrator_84_20191226095523() throws IOException {
        System.out.println("欢迎使用单元测试方法【Administrator_84()_20191226095523】");
        System.out.println("此方法测试描述：【】");
        //拼装第一个数据---------------------------------------------------------------------
        final PlatformIncomeRespDTO platformIncomeRespDTO1 = new PlatformIncomeRespDTO();
        platformIncomeRespDTO1.setPlatformNickName("aaa");
        //拼装时间维度
        platformIncomeRespDTO1.setStatisDate(Lists.newArrayList("2019-12-26","2019-12-27"));
        //拼装头信息
        Map<String, PlatformStatisParamRespData> paramInfo1=new HashMap<>();
        paramInfo1.put("userCount", new PlatformStatisParamRespData("用户数","userCount","用户信息"));
        paramInfo1.put("friendsCount", new PlatformStatisParamRespData("好友数","friendsCount","好友信息"));
        platformIncomeRespDTO1.setParamInfo(paramInfo1);
        //拼装数据
        final ArrayList<List<BiPlatformStatisRespDTO>> data1 = Lists.newArrayList();
        data1.add(Lists.newArrayList(new BiPlatformStatisRespDTO("2019-12-26","userCount","100"),new BiPlatformStatisRespDTO("2019-12-26","friendsCount","200")));
        data1.add(Lists.newArrayList(new BiPlatformStatisRespDTO("2019-12-27","userCount","300"),new BiPlatformStatisRespDTO("2019-12-27","friendsCount","400")));
        platformIncomeRespDTO1.setStatisData(data1);


        //拼装第二个数据---------------------------------------------------------------------
        final PlatformIncomeRespDTO platformIncomeRespDTO2 = new PlatformIncomeRespDTO();
        platformIncomeRespDTO2.setPlatformNickName("bbb");
        //拼装时间维度
        platformIncomeRespDTO2.setStatisDate(Lists.newArrayList("2019-12-26","2019-12-27"));
        //拼装头信息
        Map<String, PlatformStatisParamRespData> paramInfo2=new HashMap<>();
        paramInfo2.put("userCount", new PlatformStatisParamRespData("用户数","userCount","用户信息"));
        paramInfo2.put("friendsCount", new PlatformStatisParamRespData("好友数","friendsCount","好友信息"));
        platformIncomeRespDTO2.setParamInfo(paramInfo2);

        //拼装数据
        final ArrayList<List<BiPlatformStatisRespDTO>> data2 = Lists.newArrayList();
        data2.add(Lists.newArrayList(new BiPlatformStatisRespDTO("2019-12-26","userCount","500"),new BiPlatformStatisRespDTO("2019-12-26","friendsCount","600")));
        data2.add(Lists.newArrayList(new BiPlatformStatisRespDTO("2019-12-27","userCount","700"),new BiPlatformStatisRespDTO("2019-12-27","friendsCount","800")));
        platformIncomeRespDTO2.setStatisData(data2);

        final ArrayList<PlatformIncomeRespDTO> platformIncomeRespDTOS = Lists.newArrayList(platformIncomeRespDTO1, platformIncomeRespDTO2);
        System.out.println(JSONObject.toJSONString(platformIncomeRespDTOS));

        //拼装列头
        List<ExcelExportEntity> colList = dynamicNewAddExcel(paramInfo2);

        //数据拼装
        List<Map<String, Object>> list = dynamicListDataByKey(platformIncomeRespDTOS);

        //文件名称
        final Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), colList, list);

        //此功能与【拼装列头】中的 platformXh.setMergeVertical(true);功能效果一样,可直接使用 platformXh.setMergeVertical(true);进行纵向合并
        //动态合并纵列[mergeMap key列索引(从0开始),value依赖的列,没有传空,startRow 开始行(从零开始)]
        //Map<Integer, int[]> mer = new HashMap<>();
        //mer.put(0, new int[]{});
        //PoiMergeCellUtil.mergeCells(workbook.getSheetAt(0), mer, 1);

        final FileOutputStream fileOutputStream = new FileOutputStream("C:\\Users\\Administrator\\Desktop\\1.xls");
        //导出excel
        downLoadExcel(null, fileOutputStream, workbook);
    }




    *//**
     * @Description: 下载文件
     * @Param: fileName
     * @Param outputStream
     * @Param workbook
     * @return: void
     * @Author: peikunkun
     * @Date: 2019/12/26 0026 上午 10:44
     *//*
    public static void downLoadExcel(String fileName, FileOutputStream outputStream, Workbook workbook)
            throws IOException {
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            outputStream.close();
        }
    }*/
}
