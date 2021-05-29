package cn.xyz.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlatformIncomeRespDTO implements Serializable {

    private static final long serialVersionUID = 1100499105160261425L;


    /**
     * 平台别名
     */
    private String platformNickName;

    /*统计时间*/
    private List<String> statisDate;

    /*查询参数信息--[用户收入来源统计导出使用]*/
    private Map<String, PlatformStatisParamRespData> paramInfo;


    /*统计数据*/
    private List<List<BiPlatformStatisRespDTO>> statisData;
}

