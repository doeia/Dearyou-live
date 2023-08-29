package com.macro.mall.controller;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.dto.FileParam;
import com.macro.mall.dto.OssPolicyResult;
import com.macro.mall.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Api(tags = "FileController", description = "文件管理")
@RestController
@RequestMapping("/file")
public class FileController {
    private final static Logger log = LoggerFactory.getLogger(FileController.class);
    @Value("${upload-config.uploadPath}")
    private String uploadPath;
    @Value("${upload-config.suffix}")
    private String suffix;
    @Value("${upload-config.oss.dir.prefix}")
    private String prefix;

    @Autowired
    private FileService fileService;

    @ApiOperation(value = "文件上传签名生成")
    @RequestMapping(value = "/policy", method = RequestMethod.GET)
    @ResponseBody
    public CommonResult<OssPolicyResult> policy() {
        OssPolicyResult result = fileService.policy();
        return CommonResult.success(result);
    }

    @PostMapping(value = "/upload")
    public CommonResult upload(FileParam param) {
        MultipartFile file = param.getFile();
        if (file.isEmpty()) {
            log.warn("文件为空空");
        }
        String fileName = file.getOriginalFilename();  // 文件名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));  // 后缀名

        if(!param.getKey().startsWith(prefix)){
            return CommonResult.failed();
        }

        if((","+suffix+",").indexOf(","+suffixName.substring(1)+",") < 0){
            return CommonResult.failed();
        }


        String filePath = uploadPath; // 上传后的路径
        fileName = UUID.randomUUID() + suffixName; // 新文件名
        fileName = param.getKey().replace("${filename}",fileName);
        File dest = new File(filePath + fileName);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdirs();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return CommonResult.success(fileName);
    }

}
