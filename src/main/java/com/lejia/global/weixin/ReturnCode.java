package com.lejia.global.weixin;

import java.util.HashMap;
import java.util.Map;

public class ReturnCode {
    private static final Map<Integer, String> errCodeToErrMsg = new HashMap<Integer, String>() {
        {
            this.put(-1, "系统繁忙");
            this.put(0, "请求成功");
            this.put(40001,	"获取access_token时 AppSecret错误，或者access_token无效");
            this.put(40002,	"不合法的凭证类型");
            this.put(40003,	"不合法的OpenID,请开发者确认OpenID(该用户)是否已关注公众号，或是否是其他公众号的OpenID");
            this.put(40004,	"不合法的媒体文件类型");
            this.put(40005,	"不合法的文件类型");
            this.put(40006,	"不合法的文件大小");
            this.put(40007,	"不合法的媒体文件 id");
            this.put(40008,	"不合法的消息类型");
            this.put(40009,	"不合法的图片文件大小");
            this.put(40010,	"不合法的语音文件大小");
            this.put(40011,	"不合法的视频文件大小");
            this.put(40012,	"不合法的缩略图文件大小");
            this.put(40013,	"不合法的 AppID ，请开发者检查 AppID 的正确性，避免异常字符，注意大小写");
            this.put(40014,	"不合法的 access_token");
            this.put(40015,	"不合法的菜单类型");
            this.put(40016,	"不合法的按钮个数");
            this.put(40017,	"不合法的按钮个数");
            this.put(40018,	"不合法的按钮名字长度");
            this.put(40019,	"不合法的按钮KEY长度");
            this.put(40020,	"不合法的按钮URL长度");
            this.put(40021,	"不合法的菜单版本号");
            this.put(40022,	"不合法的子菜单级数");
            this.put(40023,	"不合法的子菜单按钮个数");
            this.put(40024,	"不合法的子菜单按钮类型");
            this.put(40025,	"不合法的子菜单按钮名字长度");
            this.put(40026,	"不合法的子菜单按钮 KEY 长度");
            this.put(40027,	"不合法的子菜单按钮 URL 长度");
            this.put(40028,	"不合法的自定义菜单使用用户");
            this.put(40029,	"不合法的 oauth_code");
            this.put(40030,	"不合法的 refresh_token");
            this.put(40031,	"不合法的 openid 列表");
            this.put(40032,	"不合法的 openid 列表长度");
            this.put(40033,	"不合法的请求字符,不能包含\\uxxxx格式的字符");
            this.put(40035,	"不合法的参数");
            this.put(40038,	"不合法的请求格式");
            this.put(40039,	"不合法的 URL 长度");
            this.put(40050,	"不合法的分组 id");
            this.put(40051,	"分组名字不合法");
            this.put(40060,	"删除单篇图文时，指定的 article_idx 不合法");
            this.put(40117,	"分组名字不合法");
            this.put(40118,	"media_id 大小不合法");
            this.put(40119,	"button 类型错误");
            this.put(40120,	"button 类型错误");
            this.put(40121,	"不合法的 media_id 类型");
            this.put(40132,	"微信号不合法");
            this.put(40137,	"不支持的图片格式");
            this.put(40155,	"请勿添加其他公众号的主页链接");
            this.put(41001,	"缺少access_token参数");
            this.put(41002,	"缺少appid参数");
            this.put(41003,	"缺少refresh_token参数");
            this.put(41004,	"缺少secret 参数");
            this.put(41005,	"缺少多媒体文件数据");
            this.put(41006,	"缺少media_id参数");
            this.put(41007,	"缺少子菜单数据");
            this.put(41008,	"缺少oauth code");
            this.put(41009,	"缺少openid");
            this.put(42001,	"access_token超时，请检查access_token的有效期");
            this.put(42002,	"refresh_token超时");
            this.put(42003,	"oauth_code超时");
            this.put(42007,	"用户修改微信密码,accesstoken和refreshtoken失效，需要重新授权");
            this.put(43001,	"需要 GET 请求");
            this.put(43002,	"需要 POST 请求");
            this.put(43003,	"需要 HTTPS 请求");
            this.put(43004,	"需要接收者关注");
            this.put(43005,	"需要好友关系");
            this.put(43019,	"需要将接收者从黑名单中移除");
            this.put(44001,	"多媒体文件为空");
            this.put(44002,	"POST 的数据包为空");
            this.put(44003,	"图文消息内容为空");
            this.put(44004,	"文本消息内容为空");
            this.put(45001,	"多媒体文件大小超过限制");
            this.put(45002,	"消息内容超过限制");
            this.put(45003,	"标题字段超过限制");
            this.put(45004,	"描述字段超过限制");
            this.put(45005,	"链接字段超过限制");
            this.put(45006,	"图片链接字段超过限制");
            this.put(45007,	"语音播放时间超过限制");
            this.put(45008,	"图文消息超过限制");
            this.put(45009,	"接口调用超过限制");
            this.put(45010,	"创建菜单个数超过限制");
            this.put(45011,	"API 调用太频繁，请稍候再试");
            this.put(45015,	"回复时间超过限制");
            this.put(45016,	"系统分组，不允许修改");
            this.put(45017,	"分组名字过长");
            this.put(45018,	"分组数量超过上限");
            this.put(45047,	"客服接口下行条数超过上限");
            this.put(46001,	"不存在媒体数据");
            this.put(46002,	"不存在的菜单版本");
            this.put(46003,	"不存在的菜单数据");
            this.put(46004,	"不存在的用户");
            this.put(47001,	"解析JSON/XML内容错误");
            this.put(48001,	"api功能未授权，请确认公众号已获得该接口");
            this.put(48002,	"粉丝拒收消息(粉丝在公众号选项中，关闭了'接收消息')");
            this.put(48004,	"api 接口被封禁，请登录mp.weixin.qq.com查看详情");
            this.put(48005,	"api 禁止删除被自动回复和自定义菜单引用的素材");
            this.put(48006,	"api 禁止清零调用次数，因为清零次数达到上限");
            this.put(48008,	"没有该类型消息的发送权限");
            this.put(50001,	"用户未授权该 api");
            this.put(50002,	"用户受限，可能是违规后接口被封禁");
            this.put(50005,	"用户未关注公众号");
            this.put(61451,	"参数错误 (invalid parameter)");
            this.put(61452,	"无效客服账号 (invalid kf_account)");
            this.put(61453,	"客服帐号已存在 (kf_account exsited)");
            this.put(61454,	"客服帐号名长度超过限制(仅允许10个英文字符，不包括@及@后的公众号的微信号)");
            this.put(61455,	"客服帐号名包含非法字符 (仅允许英文+数字)");
            this.put(61456,	"客服帐号个数超过限制 (10个客服账号)");
            this.put(61457,	"无效头像文件类型");
            this.put(61450,	"系统错误");
            this.put(61500,	"日期格式错误");
            this.put(65301,	"不存在此 menuid 对应的个性化菜单");
            this.put(65302,	"没有相应的用户");
            this.put(65303,	"没有默认菜单，不能创建个性化菜单");
            this.put(65304,	"MatchRule 信息为空");
            this.put(65305,	"个性化菜单数量受限");
            this.put(65306,	"不支持个性化菜单的帐号");
            this.put(65307,	"个性化菜单信息为空");
            this.put(65308,	"包含没有响应类型的 button");
            this.put(65309,	"个性化菜单开关处于关闭状态");
            this.put(65310,	"填写了省份或城市信息，国家信息不能为空");
            this.put(65311,	"填写了城市信息，省份信息不能为空");
            this.put(65312,	"不合法的国家信息");
            this.put(65313,	"不合法的省份信息");
            this.put(65314,	"不合法的城市信息");
            this.put(65316,	"该公众号的菜单设置了过多的域名外跳(最多跳转到3个域名的链接)");
            this.put(65317,	"不合法的 URL");


        }
    };

    public ReturnCode() {
    }

    public static String get(int errCode) {
        String result = (String)errCodeToErrMsg.get(errCode);
        return result != null ? result : "未知返回码：" + errCode;
    }
}

