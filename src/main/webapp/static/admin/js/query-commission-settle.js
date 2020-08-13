var dataList = null;
var start;
$(function() {

	/* 初始化入库单列表数据 */
	dataList = new $.DSTable({
		"url" : 'report/commission-settle-list',
		"ct" : "#result",
		"cm" : [{
					"header" : "合伙人姓名",
					"dataIndex" : "name"
				},{
					"header" : "手机号",
					"dataIndex" : "phone"
				},{
					"header" : "身份证",
					"dataIndex" : "idcard"
				},{
					"header" : "可提现",
					"dataIndex" : "partner_check"
				},{
					"header" : "订单号",
					"dataIndex" : "order_id"
				},{
					"header" : "号码",
					"dataIndex" : "num"
				},{
                    "header" : "地市",
                    "dataIndex" : "city_name"
                },{
					"header" : "京东号",
					"dataIndex" : "if_jd"
				},{
					"header" : "订单原价",
					"dataIndex" : "price"
				},{
					"header" : "订单成交价",
					"dataIndex" : "total"
				},{
					"header" : "销售方式",
					"dataIndex" : "sell_mode"
				},{
					"header" : "活动订单",
					"dataIndex" : "is_adjust_price"
				},{
					"header" : "调价金额",
					"dataIndex" : "adjust_price"
				},{
					"header" : "基础佣金",
					"dataIndex" : "settle_amt1"
				},{
					"header" : "商家佣金",
					"dataIndex" : "settle_amt2"
				},{
					"header" : "一级10户佣金",
					"dataIndex" : "settle_amt3"
				},{
					"header" : "一级100户佣金",
					"dataIndex" : "settle_amt4"
				},{
					"header" : "订单时间",
					"dataIndex" : "add_date"
				},{
					"header" : "改价人",
					"dataIndex" : "gjr"
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
        window.open("report/export-commission-settle-list?"+array.join("&"));
    });
});
