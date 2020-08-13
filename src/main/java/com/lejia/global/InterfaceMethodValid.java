package com.lejia.global;

import com.lejia.dto.InterfaceResult;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.dom4j.Element;

import java.util.Map;

public class InterfaceMethodValid {
    public InterfaceResult valid0102(Map<String, Object> mapParam) {
        Element platrequest = (Element) mapParam.get("data");
        String req_no = platrequest.elementText("req_no");
        if(StringUtils.isBlank(req_no)) return new InterfaceResult("F0001", "订单号未填写", null);
        return new InterfaceResult("00000", "success", null);
    }

    public InterfaceResult valid0103(Map<String, Object> mapParam) {
        Element platrequest = (Element) mapParam.get("data");
        String req_no = platrequest.elementText("req_no");
        if(StringUtils.isBlank(req_no)) return new InterfaceResult("F0001", "订单号未填写", null);
        return new InterfaceResult("00000", "success", null);
    }

    public InterfaceResult valid0101(Map<String, Object> mapParam) {
        Element platrequest = (Element) mapParam.get("data");
        String req_no = platrequest.elementText("req_no");
        if(StringUtils.isBlank(req_no)) return new InterfaceResult("F0001", "订单号未填写", null);
        int page_num = NumberUtils.toInt(platrequest.elementText("page_num"));
        if(page_num<=0) return new InterfaceResult("F0001", "page_num未填写", null);
        int operator = NumberUtils.toInt(platrequest.elementText("operator"));
        if(operator > 0 && !ArrayUtils.contains(new int[]{1,2,3}, operator))  return new InterfaceResult("F0001", "operator非法", null);
        String pattern = ObjectUtils.toString(platrequest.elementText("pattern"));
        if(StringUtils.isNotBlank(pattern) && !pattern.matches("^[0-9\\?]{11}$"))  return new InterfaceResult("F0001", "pattern非法", null);
        return new InterfaceResult("00000", "success", null);
    }

    public InterfaceResult valid0107(Map<String, Object> mapParam) {
        Element platrequest = (Element) mapParam.get("data");
        String mobile_number = platrequest.elementText("mobile_number");
        if(StringUtils.isBlank(mobile_number) || !mobile_number.matches(RegexConsts.REGEX_MOBILE_COMMON))  return new InterfaceResult("F0001", "mobile_number非法", null);
        return new InterfaceResult("00000", "success", null);
    }

    public InterfaceResult valid0104(Map<String, Object> mapParam) {
        Element platrequest = (Element) mapParam.get("data");
        String req_no = platrequest.elementText("req_no");
        String mobile_number = platrequest.elementText("mobile_number");
        int meal_id = NumberUtils.toInt(platrequest.elementText("meal_id"));
        String person_name = platrequest.elementText("person_name");
        String person_tel = platrequest.elementText("person_tel");
        String address = platrequest.elementText("address");
        String call_url = platrequest.elementText("call_url");
        if(StringUtils.isBlank(req_no) || req_no.length() > 50)  return new InterfaceResult("F0001", "req_no非法", null);
        if(StringUtils.isBlank(mobile_number) || !mobile_number.matches(RegexConsts.REGEX_MOBILE_COMMON))  return new InterfaceResult("F0001", "mobile_number非法", null);
        if(meal_id <= 0)  return new InterfaceResult("F0001", "mobile_number非法", null);
        if(StringUtils.isBlank(person_name) || person_name.length()>20)  return new InterfaceResult("F0001", "person_name非法", null);
        if(StringUtils.isBlank(person_tel) || !person_tel.matches(RegexConsts.REGEX_MOBILE_COMMON))  return new InterfaceResult("F0001", "person_tel非法", null);
        if(StringUtils.isBlank(address) || address.length() > 60)  return new InterfaceResult("F0001", "address非法", null);
        if(StringUtils.isBlank(call_url) || !person_tel.matches(RegexConsts.REGEX_URL))  return new InterfaceResult("F0001", "call_url非法", null);
        return new InterfaceResult("00000", "success", null);
    }

    public InterfaceResult valid0105(Map<String, Object> mapParam) {
        Element platrequest = (Element) mapParam.get("data");
        String pay_no = platrequest.elementText("pay_no");
        if(StringUtils.isBlank(pay_no) || pay_no.length() > 50)  return new InterfaceResult("F0001", "pay_no非法", null);
        return new InterfaceResult("00000", "success", null);
    }

    public InterfaceResult valid0108(Map<String, Object> mapParam) {
        Element platrequest = (Element) mapParam.get("data");
        String req_no = platrequest.elementText("req_no");
        if(StringUtils.isNotBlank(req_no) && req_no.length() > 50)  return new InterfaceResult("F0001", "req_no非法", null);
        int page_num = NumberUtils.toInt(platrequest.elementText("page_num"));
        if(page_num<=0) return new InterfaceResult("F0001", "page_num未填写", null);
        String begin_date = platrequest.elementText("begin_date");
        String end_date = platrequest.elementText("end_date");
        if(StringUtils.isNotBlank(begin_date) && !begin_date.matches(RegexConsts.DATE_YYYYMMDD))  return new InterfaceResult("F0001", "begin_date非法", null);
        if(StringUtils.isNotBlank(end_date) && !end_date.matches(RegexConsts.DATE_YYYYMMDD))  return new InterfaceResult("F0001", "end_date非法", null);
        return new InterfaceResult("00000", "success", null);
    }

    public InterfaceResult valid0109(Map<String, Object> mapParam) {
        Element platrequest = (Element) mapParam.get("data");
        String mobile_number = platrequest.elementText("mobile_number");
        if(StringUtils.isBlank(mobile_number) || !mobile_number.matches(RegexConsts.REGEX_MOBILE_COMMON))  return new InterfaceResult("F0001", "mobile_number非法", null);
        int act_type = NumberUtils.toInt(platrequest.elementText("act_type"),-1);
        if(act_type != 1 && act_type != 2) return new InterfaceResult("F0001", "act_type非法", null);
        return new InterfaceResult("00000", "success", null);
    }

    public InterfaceResult valid0110(Map<String, Object> mapParam) {
        Element platrequest = (Element) mapParam.get("data");
        String req_no = platrequest.elementText("req_no");
        if(StringUtils.isBlank(req_no) || req_no.length() > 50)  return new InterfaceResult("F0001", "req_no非法", null);
        String reason = platrequest.elementText("reason");
        if(StringUtils.isNotBlank(reason) && reason.length() > 50)  return new InterfaceResult("F0001", "reason超长", null);
        return new InterfaceResult("00000", "success", null);
    }

}
