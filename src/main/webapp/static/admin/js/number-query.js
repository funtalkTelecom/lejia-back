var dataList = null;


var with4s, numStatus, tags;
var start;

// $.post("dict-to-map", {group: "with4"},function(data){
//     with4s = data;
// },"json");
//
// $.post("dict-to-map", {group: "num_status"},function(data){
//     numStatus = data;
// },"json");
//
// $.post("dict-to-map", {group: "num_tags"},function(data){
//     tags = data;
// },"json");

$.ajax({
    type: "post",
    url: "dict-to-map",
    data:{group: "with4"},
    async:false,
    success: function(data){
        with4s = data;
    }
})
$.ajax({
    type: "post",
    url: "dict-to-map",
    data:{group: "num_status"},
    async:false,
    success: function(data){
        numStatus = data;
    }
})
$.ajax({
    type: "post",
    url: "dict-to-map",
    data:{group: "num_tags"},
    async:false,
    success: function(data){
        tags = data;
    }
})

$(function() {

	/* 初始化入库单列表数据 */
	dataList = new $.DSTable({
		"url" : 'number/number-list',
		"ct" : "#result",
		"cm" : [{
					"header" : "地市名称",
					"dataIndex" : "cityName"
				},{
					"header" : "网络类型",
					"dataIndex" : "netType"
				},{
					"header" : "号码",
					"dataIndex" : "numResource"
				},{
					"header" : "靓号类型",
					"dataIndex" : "numType"
				},{
					"header" : "级别",
					"dataIndex" : "numLevel"
				},{
					"header" : "最低消费",
					"dataIndex" : "lowConsume"
				},{
					"header" : "是否带4",
					"dataIndex" : "with4",
            		"renderer":function(v,record){
                        start = dataList.pm.start;
                        return with4s[v];
					}
				},/*{
					"header" : "标签",
					"dataIndex" : "tags",
                    "renderer":function(v,record){
                        var t = "";
                        if(v){
                            v = v.split(",");
                            for(var i=0; i<v.length; i++) {
                                t += tags[v[i]] + "<br>";
                            }
                        }
                        t = t ? t.substring(0, t.length - 1) : "";
                        $operate = t;
                        if(t) {
                            $operate = $("<span data-html=\"true\" class=\"btn-sm tooltip-info\" data-toggle=\"tooltip\" data-placement=\"bottom\" data-original-title=\"" +
                                t + "\">" +
                                $.trim(t.split("<br>")[0], '--') +
                                "</span>");

                            $operate.tooltip();
                        }
                        return $operate;
                    }
				},*/{
					"header" : "号段",
					"dataIndex" : "sectionNo"
				},/*{
					"header" : "此号码最多的数字",
					"dataIndex" : "moreDigit"
				},*/{
					"header" : "供应商",
					"dataIndex" : "seller"
				},{
					"header" : "买家",
					"dataIndex" : "buyer"
				},{
					"header" : "iccid",
					"dataIndex" : "iccid"
				},{
					"header" : "状态",
					"dataIndex" : "status",
					"renderer":function(v,record){
						return numStatus[v];
					}
				},{
                    "header" : "受理结果",
                    "dataIndex" : "sl_reason"
                },{
					"header" : "运营商类型",
					"dataIndex" : "teleType"
				},{
                    "header" : "操作",
                    "dataIndex" : "id",
                    "renderer":function(v,record){
                        var node = [];
                        if(p_sl && record.status == 7) {
                            node.push('<a class="btn btn-success btn-xs sl" href="javascript:void(0);">重受理</a>');
                            node.push('<a class="btn btn-success btn-xs over" href="javascript:void(0);">结束</a>');
                        }
                        $operate = $("<div>"+$.trim(node.join(""),'--')+"</div>");
                        //重受理
                        $operate.find(".sl").click(function () {
                            if(confirm("确认重新受理?")) {
                                $.post("number/again-sl", {id: v, noRepeat : 1}, function (data) {
                                    dataList.reload();
                                    alert(data.data);
                                }, "json");
                            }
                        });
                        //结束
                        $operate.find(".over").click(function () {
                            if(confirm("确认结束?")) {
                                $.post("number/over", {id: v, noRepeat : 1}, function (data) {
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
            $(".query input[type!=checkbox],.query select").each(function(index,v2){
                var name=$(v2).attr("name");
                obj[name]=$(v2).val();
            });
            var c = $(".query input[type=checkbox]:checked").length;
            $(".query input[type=checkbox]:checked").each(function(index,v2){
                var name=$(v2).attr("name");
                obj[name] = obj[name]==undefined?"":obj[name];
                obj[name]=obj[name]+$(v2).val()+",";
                if(c<=(index+1)) obj[name] = obj[name].substring(0, obj[name].length - 1);
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

    var option = {
        url:"",
        key:"keyId",
        value:"keyValue",
        onchange:"",
        onclick:"",
        labelClass:"col-xs-2",
        param:{t:new Date().getTime()}
    };
    dictCheckBoxDefault($("#numberTags"), "num_tags", option);
    var qoption = {
        url:"",
        key:"keyId",
        value:"keyValue",
        onchange:"",
        onclick:"",
        labelClass:"col-xs-1",
        param:{t:new Date().getTime()}
    };
    dictCheckBoxDefault($("#qnumberTags"), "num_tags", qoption);

    var soption = {
        url:"",
        key:"keyId",
        value:"keyValue",
        onchange:"",
        onclick:"",
        param:{t:new Date().getTime()}
    };
    dictSelect($("#qstatus"), "num_status", soption, false);

    //添加标签确定按钮
    $(document).on("click","#editTags .modal-footer .btn-success",function() {
        $.post("number/numRule-addTags",$("#editTags form").serialize(),function(data){
            $("#editTags .modal-footer .btn-success").attr("disabled", "disabled");
            // dataList.reload();
            $('#editTags').modal('hide');
            dataList.reload();
            alert(data.data);
        },"json");
	});

    //清空标签按钮
    $(document).on("click","#editTags .modal-footer .btn-danger",function() {
        $.post("number/numRule-clearTags",$("#editTags form").serialize(),function(data){
            $("#editTags .modal-footer .btn-success").attr("disabled", "disabled");
            // dataList.reload();
            $('#editTags').modal('hide');
            dataList.reload();
            alert(data.data);
        },"json");
	});

    $(document).on("click","#stop-sale .modal-footer .btn-success",function() {
        $.post("number/begin-sale",$("#stop-sale form").serialize(),function(data){
            dataList.reload();
            data = data.data;
            if(data.length > 0) {
                $("#failnum").val(data.join("\n"));
                $('#stop-sale-result').modal('show');
            }else {
                $('#stop-sale').modal('hide');
                alert("提交成功");
            }

        },"json");
    });

    $(document).on("click","#stop-sale .modal-footer .btn-danger",function() {
        $.post("number/stop-sale",$("#stop-sale form").serialize(),function(data){
            dataList.reload();
            data = data.data;
            if(data.length > 0) {
                $("#failnum").val(data.join("\n"));
                $('#stop-sale-result').modal('show');
            }else {
                $('#stop-sale').modal('hide');
                alert("提交成功");
            }
        },"json");
    });

    //失效
    $(document).on("click","#lose-sale .modal-footer .btn-danger",function() {
        if(confirm("请确认是否失效以上号码？")){
            $.post("number/lose-sale",$("#lose-sale form").serialize(),function(data){
                dataList.reload();
                data = data.data;
                if(data.length > 0) {
                    $("#losefailnum").val(data.join("\n"));
                    $('#lose-sale-result').modal('show');
                }else {
                    $('#lose-sale').modal('hide');
                    alert("提交成功");
                }
            },"json");
        }
    });

    //点击设置标签,初始化
    $("button[data-target=#editTags]").bind("click", function () {
        $("#numberTags").html("");
        dictCheckBoxDefault($("#numberTags"), "num_tags", option);
	});

    var getpos=function(str){
        //获取元素绝对位置
        var Left=0,Top=0;
        do{Left+=str.offsetLeft,Top+=str.offsetTop;}
        while(str=str.offsetParent);
        var scr=document.getElementById('scrollDiv');//减去图层滚动条

        if(scr!=null && scr!=undefined){
            Left=Left-scr.scrollLeft;
        }
        return {"Left":Left,"Top":Top};
    };
    //地市下拉框
    function showMenu(){
        //处理地市下拉位置偏移问题
        var xy = getpos($("#gSaleCityStr").get(0));
        var option = {
            "menuContentCss":'{"left":"'+(Number(xy.Left)-Number($("#sidebar").css("height")=="1px"?0:$("#sidebar").css("width").replace("px", ""))-5)+'px"}'
        };
        showCityMenu(option);
    }
    var zNodes;
    $.post("query-city-ztree", {pid: 0, isopen:false}, function (data) {
        zNodes = data;
        $.fn.zTree.init($("#cityTree"), setting, zNodes);
        $("#gSaleCityStr").off("click").on("click", showMenu);
    }, "json");
    $("#reset").click(function(){
        $.fn.zTree.init($("#cityTree"), setting, zNodes);
	});

    $('.chosen-select').chosen({allow_single_deselect:true, search_contains:true});
    //地市下拉框end

    //添加标签确定按钮
    $(document).on("click","#export",function() {
        var obj={};
        $(".query input[type!=checkbox],.query select").each(function(index,v2){
            var name=$(v2).attr("name");
            obj[name]=$(v2).val();
        });
        var c = $(".query input[type=checkbox]:checked").length;
        $(".query input[type=checkbox]:checked").each(function(index,v2){
            var name=$(v2).attr("name");
            obj[name] = obj[name]==undefined?"":obj[name];
            obj[name]=obj[name]+$(v2).val()+",";
            if(c<=(index+1)) obj[name] = obj[name].substring(0, obj[name].length - 1);
        });

        bootbox.dialog({
            boxCss:{"width":"400px"},
            title: "<span class='bigger-110'>确认框</span>",
            message: "请选择导出哪些数据?",
            buttons:
                {
                    "success" :
                        {
                            "label" : "<i class='ace-icon fa fa-check'></i> 当前页",
                            "className" : "btn-sm btn-success",
                            "callback": function() {
                                $("#export").prop("disabled", true);
                                obj["isCurrentPage"] = "1";
                                var param = "start="+start+"&";
                                for (key in obj) {
                                    param += key+"="+obj[key]+"&";
                                }
                                downloadFile("number/number-export", param, '号码列表.xlsx','$("#export").prop("disabled", false)');
                            }
                        },
                    "danger" :
                        {
                            "label" : "全部",
                            "className" : "btn-sm btn-danger",
                            "callback": function() {
                                $("#export").prop("disabled", true);
                                obj["isCurrentPage"] = "0";
                                var param = "";
                                for (key in obj) {
                                    param += key+"="+obj[key]+"&";
                                }
                                downloadFile("number/number-export", param, '号码列表.xlsx','$("#export").prop("disabled", false)');
                            }
                        }
                }
        });
    });

    $(document).on("click","#saleNumInfo .modal-footer .btn-success",function() {
        if ( !$("#saleNumInfo input[type=file]").val()){
            alert("请选择文件");
            return
        }
        var options = {
            type : "post",
            url: "number/batch-add-num",
            success : function(data) {
                var date = data.data;
                if(confirm(date.data+",请确认")){
                    $.post("number/sure-batch-add-num",
                        function(data){
                            dataList.reload();
                            $('#saleNumInfo').modal('hide');
                        },"json");
                }

            }
        };
        // 将options传给ajaxForm
        $('#saleNumInfo form').ajaxSubmit(options);
    });
});
function downloadFile(url, param, fileName, functionStr){
    // url = 'number/number-export';
    var xhr = new XMLHttpRequest();
    xhr.open('POST', url+"?"+param, true);        // 也可以使用POST方式，根据接口
    xhr.responseType = "blob";    // 返回类型blob
    // 定义请求完成的处理函数，请求前也可以增加加载框/禁用下载按钮逻辑
    xhr.onload = function () {
        // 请求完成
        if (this.status === 200) {
            // 返回200
            var blob = this.response;
            var reader = new FileReader();
            reader.readAsDataURL(blob);    // 转换为base64，可以直接放入a表情href
            reader.onload = function (e) {
                // 转换完成，创建一个a标签用于下载
                var a = document.createElement('a');
                a.download = fileName;
                a.href = e.target.result;
                $("body").append(a);    // 修复firefox中无法触发click
                a.click();
                $(a).remove();
                eval(functionStr);
            }
        }
    };
    xhr.send(JSON.stringify(param));
}
