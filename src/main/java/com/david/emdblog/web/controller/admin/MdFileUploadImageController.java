package com.david.emdblog.web.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.david.emdblog.constant.Constants;
import com.david.emdblog.utils.DateUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/admin/mdFileUploadImage")
@Controller
public class MdFileUploadImageController {

    /**
     * 上传uploadMarkdownImage图片
     * success : 0 | 1,           // 0 表示上传失败，1 表示上传成功
     * message : "提示的信息，上传成功或上传失败及错误信息等。",
     * url     : "图片地址"        // 上传成功时才返回
     */
    @RequestMapping("uploadMarkdownImage")
    @ResponseBody
    public Map<String, Object> uploadMarkdownImage(@RequestParam(value = "editormd-image-file", required = false) MultipartFile file, HttpServletRequest request) {
        Map<String, Object> resultMap = new HashMap<>();
        String fileName = file.getOriginalFilename();
        fileName = DateUtil.formatDate(new Date(), "yyyyMMdd")+fileName;
        File targetFile = new File(Constants.MARKDOWN_UPLOAD_IMAGEPATH, fileName);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        try {
            //  保存图片
            file.transferTo(targetFile);
            // 返回符合editormd的格式数据
            resultMap.put("success", 1);
            resultMap.put("message", "上传成功！");
            System.out.println(request.getContextPath());
            resultMap.put("url", request.getContextPath() + "/static/userImages/markdownuploadimages/" + fileName);
        } catch (Exception e) {
            resultMap.put("success", 0);
            resultMap.put("message", "上传失败！");
            e.printStackTrace();
        }
        return resultMap;
    }
}
