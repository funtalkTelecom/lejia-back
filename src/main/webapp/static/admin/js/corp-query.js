var dataList = null;

$(function() {
	/* 初始化入库单列表数据 */
	dataList = new $.DSTable({
		"url" : 'corp/corp-list',
		"ct" : "#result",
                "cm" : [{
                    "header" : "编号",
                    "dataIndex" : "id"
                },{
                    "header" : "企业名称",
                    "dataIndex" : "name"
                },{
                    "header" : "法人",
                    "dataIndex" : "principal"
                },{
                    "header" : "电话",
                    "dataIndex" : "phone"
                },{
                    "header" : "邮箱",
                    "dataIndex" : "email"
                },{
                    "header" : "地址",
                    "dataIndex" : "address"
                },{
                    "header" : "状态",
                    "dataIndex" : "status",
                    "renderer" : function(v, record) {
                        if(v == '1') return "正常";
                        else  return "禁用";
                    }
                },{
					"header" : "操作",
					"dataIndex" : "id",
					"renderer":function(v,record){

						var node = [];

						if(p_edit) {
							node.push('<a class="btn btn-success btn-xs update" href="javascript:void(0);">修改</a>');
                        }

                        $operate = $("<div>"+$.trim(node.join(""),'--')+"</div>");

                        $operate.find(".update").click(function () {

                            $("#corpInfo .modal-title").html("商家详情");
                            $("#actionType").val("1");

                            $.post("corp/corp-info",{id:v},function(data){
                                var _data=data.data;
                                formInit($("#corpInfo form"), _data);
                                $('#corpInfo').modal('show');
                            },"json");

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

    $("#createCorp").click(function() {
        $("#actionType").val("2");
        $('#corpInfo').modal('show');
    });

	window.reload = function(){
		dataList.reload();
	}


    $('#corpInfo').on('hidden.bs.modal', function (event) {

        $("#name").val("");
        $("#principal").val("");
        $("#phone").val("");
        $("#email").val("");
        $("#address").val("");
        $("#status").val("");

    });
});


$(document).on("click","#corpInfo .modal-footer .btn-success",function() {

    var name = $("#name").val().replace(/\s+/g,"");
    var principal = $("#principal").val().replace(/\s+/g,"");
    var phone = $("#phone").val().replace(/\s+/g,"");
    var email = $("#email").val().replace(/\s+/g,"");
    var address = $("#address").val().replace(/\s+/g,"");
    var status = $("#status").val();

    if(name==""||principal==""||phone==""||email==""||address==""||status== null){
        alert("所有设置项不能为空,请检查!");
        return false;
    }

    var actionType=$("#actionType").val();

    if (actionType==1){

        $.post("corp/corp-edit",$("#corpInfo form").serialize(),function(data){
            dataList.reload();
            $('#corpInfo').modal('hide');
            alert(data.data);
        },"json");

    }

    if (actionType==2){

        $.post("corp/corp-create",$("#corpInfo form").serialize(),function(data){
            dataList.reload();
            $('#corpInfo').modal('hide');
            alert(data.data);
        },"json");

    }


/*    $.post("corp/corp-edit",$("#corpInfo form").serialize(),function(data){
        dataList.reload();
        $('#corpInfo').modal('hide');
        alert(data.data);
    },"json");*/
});


// 用法dateFtt("yyyy-MM-dd hh:mm:ss",new Date(1528271207000));
function dateFtt(fmt,date) {
    try {
        var o = {
            "M+": date.getMonth() + 1,                 //月份
            "d+": date.getDate(),                    //日
            "h+": date.getHours(),                   //小时
            "m+": date.getMinutes(),                 //分
            "s+": date.getSeconds(),                 //秒
            "q+": Math.floor((date.getMonth() + 3) / 3), //季度
            "S": date.getMilliseconds()             //毫秒
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (var k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
    } catch (e) {
        return "";
    }
    return fmt;
} ;



