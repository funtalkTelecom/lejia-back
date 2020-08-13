var dataList = null;
var start;
$(function() {

	/* 初始化入库单列表数据 */
	dataList = new $.DSTable({
		"url" : 'report/order-list',
		"ct" : "#result",
		"cm" : [{
					"header" : "订单号",
					"dataIndex" : "order_id"
				},{
					"header" : "号码",
					"dataIndex" : "num"
				},{
					"header" : "客户编码",
					"dataIndex" : "id"
				},{
					"header" : "客户姓名",
					"dataIndex" : "name"
				},{
					"header" : "客户昵称",
					"dataIndex" : "nick_name"
				},{
					"header" : "状态",
					"dataIndex" : "key_value"
				},{
					"header" : "支付方式",
					"dataIndex" : "pay_menthod"
				},{
                    "header" : "原价",
                    "dataIndex" : "sub_total"
                },{
					"header" : "物流费",
					"dataIndex" : "shipping_total"
				},{
                    "header" : "优惠",
                    "dataIndex" : "commission"
                },{
                    "header" : "调价",
                    "dataIndex" : "adjust_price"
                },{
                    "header" : "结算价",
                    "dataIndex" : "total"
                },{
                    "header" : "下单时间",
                    "dataIndex" : "add_date"
                },{
                    "header" : "供应商",
                    "dataIndex" : "corp"
                },{
                    "header" : "合伙人",
                    "dataIndex" : "partner_name"
                },{
                    "header" : "奖励金额",
                    "dataIndex" : "settle_amt"
                },{
                    "header" : "收件人",
                    "dataIndex" : "person_name"
                },{
                    "header" : "收货电话",
                    "dataIndex" : "person_tel"
                },{
                    "header" : "收货地址",
                    "dataIndex" : "address"
                },{
                    "header" : "发货时间",
                    "dataIndex" : "pickup_date"
                },{
                    "header" : "物流公司",
                    "dataIndex" : "express_name"
                },{
                    "header" : "运单号",
                    "dataIndex" : "express_number"
                },{
                    "header" : "取消原因",
                    "dataIndex" : "reason"
                }],
		"pm" : {
			"limit" : 15,
			"start" : 0
		},
		"getParam" : function() {
			var obj={};
            $(".query input[type!=checkbox],.query select").each(function(index,v2){
                var name=$(v2).attr("name");
                obj[name]=$(v2).val();
            });
			return obj;
		}
	});
	dataList.load();

	$("#query").click(function() {
		dataList.load();
	});
	
	window.reload = function(){
		dataList.reload();
	}

    //添加标签确定按钮
    $(document).on("click","#export",function() {
        var array = [];
        $(".query input[type!=checkbox],.query select").each(function(index,v2){
            var name=$(v2).attr("name");
            array.push(name+"="+$(v2).val());
        });
        window.open("report/export-order-list?"+array.join("&"));
    });
});
