package com.macro.mall.service.impl;

import cn.hutool.json.JSONUtil;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.BinaryUtil;
import com.aliyun.oss.model.MatchMode;
import com.aliyun.oss.model.PolicyConditions;
import com.macro.mall.dto.OssCallbackParam;
import com.macro.mall.dto.OssPolicyResult;
import com.macro.mall.service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class FileServiceImpl implements FileService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OssServiceImpl.class);
    @Value("${upload-config.oss.policy.expire}")
    private int UPLOAD_OSS_EXPIRE;
    @Value("${upload-config.oss.maxSize}")
    private int UPLOAD_OSS_MAX_SIZE;
    @Value("${upload-config.oss.callback}")
    private String UPLOAD_OSS_CALLBACK;
    @Value("${upload-config.oss.bucketName}")
    private String UPLOAD_OSS_BUCKET_NAME;
    @Value("${upload-config.oss.endpoint}")
    private String UPLOAD_OSS_ENDPOINT;
    @Value("${upload-config.oss.dir.prefix}")
    private String UPLOAD_OSS_DIR_PREFIX;

    @Autowired
    private OSSClient ossClient;

    @Override
    public OssPolicyResult policy() {
        OssPolicyResult result = new OssPolicyResult();
        // 存储目录
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String dir = UPLOAD_OSS_DIR_PREFIX+sdf.format(new Date());
        // 签名有效期
        long expireEndTime = System.currentTimeMillis() + UPLOAD_OSS_EXPIRE * 1000;
        Date expiration = new Date(expireEndTime);
        // 文件大小
        long maxSize = UPLOAD_OSS_MAX_SIZE * 1024 * 1024;
        // 回调
        OssCallbackParam callback = new OssCallbackParam();
        callback.setCallbackUrl(UPLOAD_OSS_CALLBACK);
        callback.setCallbackBody("filename=${object}&size=${size}&mimeType=${mimeType}&height=${imageInfo.height}&width=${imageInfo.width}");
        callback.setCallbackBodyType("application/x-www-form-urlencoded");
        // 提交节点
        String action = "http://" + UPLOAD_OSS_BUCKET_NAME + "." + UPLOAD_OSS_ENDPOINT;
        if(StringUtils.isEmpty(UPLOAD_OSS_BUCKET_NAME)){
            action = "http://" + UPLOAD_OSS_ENDPOINT;
        }
        try {
            PolicyConditions policyConds = new PolicyConditions();
            policyConds.addConditionItem(PolicyConditions.COND_CONTENT_LENGTH_RANGE, 0, maxSize);
            policyConds.addConditionItem(MatchMode.StartWith, PolicyConditions.COND_KEY, dir);
            String postPolicy = ossClient.generatePostPolicy(expiration, policyConds);
            byte[] binaryData = postPolicy.getBytes("utf-8");
            String policy = BinaryUtil.toBase64String(binaryData);
            String signature = ossClient.calculatePostSignature(postPolicy);
            String callbackData = BinaryUtil.toBase64String(JSONUtil.parse(callback).toString().getBytes("utf-8"));
            // 返回结果
            result.setAccessKeyId(ossClient.getCredentialsProvider().getCredentials().getAccessKeyId());
            result.setPolicy(policy);
            result.setSignature(signature);
            result.setDir(dir);
            result.setCallback(callbackData);
            result.setHost(action);
        } catch (Exception e) {
            LOGGER.error("签名生成失败", e);
        }
        return result;
    }
}
