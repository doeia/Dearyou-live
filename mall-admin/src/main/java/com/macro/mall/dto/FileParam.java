package com.macro.mall.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileParam {
    private String policy;
    private String signature;
    private String key;
    private String ossaccessKeyId;
    private String dir;
    private String host;
    private MultipartFile file;
}
