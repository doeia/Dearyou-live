package com.macro.mall.service;

import com.macro.mall.dto.ExpressResult;

public interface ExpressService {
    ExpressResult queryLogistics(String com, String num);
}
