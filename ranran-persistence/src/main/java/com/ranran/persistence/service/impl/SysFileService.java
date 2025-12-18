package com.ranran.persistence.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ranran.common.constant.DateConstant;
import com.ranran.common.domain.entity.SysFile;
import com.ranran.common.domain.enums.ResultCode;
import com.ranran.common.exception.BusinessException;
import com.ranran.common.utils.*;
import com.ranran.persistence.mapper.SysFileMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDate;

/**
 * @author ranran
 * 文件Service
 */
@Service
public class SysFileService extends ServiceImpl<SysFileMapper, SysFile> {
    @Value("${file.upload.path}")
    private String uploadPath;

    @Value("${file.upload.access-path}")
    private String accessPath;

    public SysFile upload(MultipartFile file) {
        if (file.isEmpty()) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "文件不能为空");
        }
        try {
            String username = SecurityUtil.getAuthentication().getName();
            String originalFileName = file.getOriginalFilename();
            String fileSuffix = FileUtil.getFileExt(originalFileName);
            String uniqueFileName = IdUtil.fastSimpleUuid() + "." + fileSuffix;
            String today = DateUtil.format(LocalDate.now(), DateConstant.YYYY_MM_DD);
            File saveDir = Paths.get(uploadPath, today).toFile();
            if (!saveDir.exists()) {
                boolean mkdirs = saveDir.mkdirs();
                if (!mkdirs) {
                    throw new BusinessException("文件目录创建失败");
                }
            }
            File saveFile = Paths.get(saveDir.getAbsolutePath(), uniqueFileName).toFile();
            file.transferTo(saveFile);
            SysFile sysFile = new SysFile();
            sysFile.setFileName(originalFileName);
            sysFile.setFilePath(saveFile.getAbsolutePath());
            sysFile.setFileUrl(accessPath + today + "/" + uniqueFileName);
            sysFile.setFileType(file.getContentType());
            sysFile.setFileSize(file.getSize());
            sysFile.setCreateBy(username);
            baseMapper.insert(sysFile);
            return sysFile;
        } catch (IOException e) {
            throw new BusinessException("文件上传失败：" + e.getMessage());
        }
    }

    public void download(Long fileId, HttpServletResponse response) {
        SysFile sysFile = baseMapper.selectById(fileId);
        if (ObjUtil.isNull(sysFile)) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "文件不存在");
        }
        File file = new File(sysFile.getFilePath());
        if (!file.exists()) {
            throw new BusinessException("文件已被删除");
        }
        try (OutputStream os = response.getOutputStream()) {
            response.setContentType(sysFile.getFileType());
            String encodedFileName = URLEncoder.encode(sysFile.getFileName(),
                    StandardCharsets.UTF_8);
            response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"");
            os.write(FileUtil.readBytes(file));
            os.flush();
        } catch (IOException e) {
            throw new BusinessException("文件下载失败：" + e.getMessage());
        }
    }

}
