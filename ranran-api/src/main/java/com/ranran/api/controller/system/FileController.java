package com.ranran.api.controller.system;

import com.ranran.common.domain.Result;
import com.ranran.common.domain.dto.FileUploadDto;
import com.ranran.common.domain.entity.SysFile;
import com.ranran.common.domain.vo.FileVo;
import com.ranran.persistence.service.impl.SysFileService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

/**
 * @author ranran
 * 文件接口
 */
@RestController
@RequestMapping("/api/file")
public class FileController {

    @Resource
    private SysFileService sysFileService;

    @PostMapping("/upload")
    public Result<FileVo> upload(FileUploadDto dto) {
        SysFile sysFile = sysFileService.upload(dto.getFile());
        FileVo fileVo = new FileVo();
        fileVo.setFileId(sysFile.getFileId());
        fileVo.setFileUrl(sysFile.getFileUrl());
        return Result.success(fileVo, "上传成功");
    }

    @GetMapping("/download/{fileId}")
    public void download(@PathVariable Long fileId, HttpServletResponse response) {
        sysFileService.download(fileId, response);
    }
}
