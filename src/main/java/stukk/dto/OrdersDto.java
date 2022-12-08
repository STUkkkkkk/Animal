package stukk.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import stukk.entity.OrderDetail;
import stukk.entity.Orders;

import java.util.List;

@Data
@ApiModel(value = "订单信息辅助实体（OrdersDto）")
public class OrdersDto extends Orders {
    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "收货人")
    private String consignee;

    @ApiModelProperty(value = "订单详细信息列表")
    private List<OrderDetail> orderDetails;

}
