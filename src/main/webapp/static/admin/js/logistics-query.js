var dataList = null;
$(function() {
	/* 初始化入库单列表数据 */
	dataList = new $.DSTable({
		"url" : 'logistics/logistics-list',
		"ct" : "#result",
		"cm" : [{
					"header" : "编号",
					"dataIndex" : "id"
				},{
					"header" : "物流公司",
					"dataIndex" : "keyValue"
				},{
					"header" : "运费类型",
					"dataIndex" : "freightTypeStr"
				},{
					"header" : "运费",
					"dataIndex" : "freight"
				},{
					"header" : "操作",
					"dataIndex" : "operate",
					"renderer":function(v,record){
						var node = [];
						if(add_p){
							node.push('<a class="btn btn-success btn-xs update" href="JavaScript:void(0);">修改</a>')
							node.push('<a class="btn btn-success btn-xs reset-pwd" href="JavaScript:void(0);">删除</a>')
						}
						$operate = $("<div>"+$.trim(node.join(""),'--')+"</div>");
                        $operate.find(".update").click(function (){
                            $.post("logistics/logistics-detail",{id:record.id},function(data){
                                var _data=data.data;
                                updateFormInit($("#myModal form"), _data);
                                if(_data.freightType==3){
                                    $(".freight").show();
                                }else {
                                    $(".freight").hide();
								}
                                $('#myModal').modal('show');


                            },"json");
                        })

                        $operate.find(".reset-pwd").click(function (){
                            if(confirm("确认删除")) {
                                $.post("logistics/logistics-delete/"+record.id,function(data){
                                    if(data.code == 200) {
                                        alert(data.data);
                                        dataList.reload();
                                    }else {
                                        alert(data.data);
                                    }
                                },"json");
                            }
                        })
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

    $(document).on("click","#myModal .modal-footer .btn-primary",function() {
        // if(!validate_check($("#myModal form"))) return;
        var keyid=$("select[name=keyId]").val()
		if(!keyid||keyid==-1){
        	alert("请选择物流公司");
        	return
		}
        var freightType=$("select[name=freightType]").val();
        if(!freightType||freightType==-1){
            alert("请选择运费类型");
            return
        }
        if(freightType==3){
            var freight = $("#freight").val();
            if(!freight){
            	alert("请输入运费")
            	return
			}
		}
        $("input[name=keyValue]").val($("select[name=keyId]").find("option:selected").text())
        $.post("logistics/edit-logistics",$("#myModal form").serialize(),function(data){
            alert(data.data);
            dataList.reload();
            $('#myModal').modal('hide');
        },"json");
    });

    function init(){
        $("#myModal").data("html",$("#myModal form").html());
        $('#myModal').on('hide.bs.modal', function () {
            $("#myModal form").html($("#myModal").data("html"));
        });
    }
    init();


});

    $(document).on("change","#myModal select[name=freightType]",function () {   //选择物流类型
        if($(this).val()==3){
        	$(".freight").show();
		}else {
            $(".freight").hide();
            $("#freight").val("");
		}
    })
