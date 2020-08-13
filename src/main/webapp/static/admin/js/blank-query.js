var dataList = null;
$(function() {
	/* 初始化入库单列表数据 */
	dataList = new $.DSTable({
		"url" : 'blank/blank-list',
		"ct" : "#result",
		"cm" : [{
                    "header" : "编码",
                    "dataIndex" : "id"
                },{
					"header" : "类型",
					"dataIndex" : "typeText"
				},{
					"header" : "内容",
					"dataIndex" : "content"
				},{
					"header" : "操作",
					"dataIndex" : "id",
					"renderer":function(v,record){
						var node = [];
						if(p_delete) {
							node.push('<a class="btn btn-success btn-xs delete" href="javascript:void(0);">删除</a>')
                        }
                        $operate = $("<div>"+$.trim(node.join(""),'--')+"</div>");

                        $operate.find(".delete").click(function () {
                            if (confirm("确认删除？")) {
                                $.post("blank/delete-blank", {id: v}, function (data) {
                                    dataList.reload();
                                    alert(data.data);
                                }, "json");
                            }
                        });
						return $operate;
					}
				}],
		"pm" : {
			"limit" : 15,
			"start" : 0
		},
		"getParam" : function() {
			var obj={};
			$(".query input,.query select").each(function(index,v2){
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

});


