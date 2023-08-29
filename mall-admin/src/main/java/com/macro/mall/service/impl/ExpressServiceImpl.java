package com.macro.mall.service.impl;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.macro.mall.dto.ExpressResult;
import com.macro.mall.service.ExpressService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;

@Service
public class ExpressServiceImpl implements ExpressService {

    @Value("${kuaidi100-config.key}")
    private String kuaidi100Key;
    @Value("${kuaidi100-config.customer}")
    private String kuaidi100Customer;

    @Override
    public ExpressResult queryLogistics(String com, String num){
        String param ="{\"com\":\""+com+"\",\"num\":\""+num+"\"}";
        String customer = kuaidi100Customer;
        String key = kuaidi100Key;
        String sign = SecureUtil.md5(param+key+customer);
        HashMap params = new HashMap();
        params.put("param",param);
        params.put("sign",sign);
        params.put("customer",customer);
        try {
            String result = HttpUtil.post("http://poll.kuaidi100.com/poll/query.do", params);
            if(StringUtils.isEmpty(result)){
                return null;
            }
            JSONObject obj = JSONUtil.parseObj(result);
            if(obj.containsKey("message") && obj.getStr("message").equals("ok")){
                ExpressResult expressResult = JSONUtil.toBean(obj, ExpressResult.class);
                return expressResult;
            }else{
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
