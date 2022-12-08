package stukk.common;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回结果类，服务端响应的数据最终都会封装成此对象
 *
 * @param <T>
 */
@ApiModel(value = "通用返回结果实体（R<T>）")
@Data
public class R<T> implements Serializable {

    @ApiModelProperty(value = "编码：1成功，0和其它数字为失败")
    private Integer code; // 编码：1成功，0和其它数字为失败

    @ApiModelProperty(value = "错误信息")
    private String msg; // 错误信息

    @ApiModelProperty(value = "数据")
    private T data; // 数据

    @ApiModelProperty(value = "动态数据")
    private Map map = new HashMap(); // 动态数据

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
