package cn.xyz.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlatformStatisParamRespData implements Serializable {

    private static final long serialVersionUID = 4263523446154995471L;

    /**
     * 参数名称
     */
    private String dataName;

    /**
     * 参数key
     */
    private String dateKey;

    /**
     * 参数描述
     */
    private String dateDesc;

}
