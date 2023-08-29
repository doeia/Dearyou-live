package com.macro.mall.portal.controller;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.net.NetUtil;
import com.macro.mall.common.api.CommonPage;
import com.macro.mall.common.api.CommonResult;
import com.macro.mall.model.OmsCartItem;
import com.macro.mall.model.OmsOrderReturnApply;
import com.macro.mall.model.OmsOrderReturnReason;
import com.macro.mall.portal.config.BasicConfiguration;
import com.macro.mall.portal.domain.*;
import com.macro.mall.portal.service.OmsPortalOrderReturnApplyService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

/**
 * 申请退货管理Controller
 * Created by macro on 2018/10/17.
 */
@Controller
@Api(tags = "OmsPortalOrderReturnApplyController", description = "申请退货管理")
@RequestMapping("/returnApply")
public class OmsPortalOrderReturnApplyController {
    @Autowired
    private OmsPortalOrderReturnApplyService returnApplyService;
    @Autowired
    private BasicConfiguration basicConfig;

    @ApiOperation("申请退货")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult create(@RequestBody OmsOrderReturnApplyParam returnApply) {
        return returnApplyService.create(returnApply);
    }

    @ApiOperation("售后申请列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<CommonPage<OrderReturnApplyResult>> list(@RequestBody OmsOrderReturnApplyListParam applyListParam) {
        return CommonResult.success(returnApplyService.list(applyListParam));
    }

    @ApiOperation("售后原因列表")
    @RequestMapping(value = "/reasonList", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<List<OmsOrderReturnReason>> reasonList() {
        List<OmsOrderReturnReason> returnReasonList = returnApplyService.returnReasonList();
        return CommonResult.success(returnReasonList);
    }

    @ApiOperation("售后申请详情")
    @RequestMapping(value = "/detail/{id}", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<OrderReturnApplyResult> detail(@PathVariable Long id) {
        OrderReturnApplyResult returnApply = returnApplyService.detail(id);
        return CommonResult.success(returnApply);
    }

    @ApiOperation("上传凭证图片")
    @RequestMapping(value = "/uploadProofPic", method = RequestMethod.POST)
    @ResponseBody
    public CommonResult<String> uploadProofPic(@RequestParam MultipartFile file,@RequestParam Long id) throws UnknownHostException {
        StringBuffer fileName = new StringBuffer();
        fileName.append(UUID.randomUUID().toString().replaceAll("-", ""));
        String type = file.getContentType();
        if ("image/png".equals(type)) {
            fileName.append(".png");
        } else if ("image/jpeg".equals(type)) {
            fileName.append(".jpeg");
        } else if ("image/gif".equals(type)) {
            fileName.append(".gif");
        } else {
            return CommonResult.failed("请选择.png或.jpg格式的图片");
        }
        if (file.getSize() > 10240000L) {
            return CommonResult.failed("图片超过10Mb");
        }

        String uploadPath = "/upload/afterSale/" + id + "/";
        String imagePath = FileUtil.getWebRoot() + uploadPath + fileName.toString();
        File uploadFile = FileUtil.touch(imagePath);
        String host = basicConfig.getFileServerUrl();
        String imageHost = host + uploadPath + fileName.toString();
        try {
            file.transferTo(uploadFile);
            return CommonResult.success(imageHost);

        } catch (IOException e) {
            return CommonResult.failed("保存失败");
        }
    }
}
