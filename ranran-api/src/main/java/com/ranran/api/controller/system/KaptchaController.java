package com.ranran.api.controller.system;

import com.google.code.kaptcha.Producer;
import com.ranran.common.constant.Constant;
import com.ranran.common.domain.Result;
import com.ranran.common.utils.IdUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author ranran
 * 验证码接口
 */
@RestController
@RequestMapping("/captcha")
public class KaptchaController {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private Producer kaptchaProducer;

    @GetMapping("/image")
    public Result<Map> generateCaptcha(HttpServletResponse response) throws Exception {
        String codeToken = IdUtil.fastSimpleUuid();
        String code = kaptchaProducer.createText();
        redisTemplate.opsForValue().set(Constant.KAPTCHA_KEY_PREFIX + codeToken, code, 1, TimeUnit.MINUTES);
        BufferedImage image = kaptchaProducer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);
        String base64Image = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(outputStream.toByteArray());
        Map<String, Object> map = new HashMap<>();
        map.put("codeToken", codeToken);
        map.put("image", base64Image);
        return Result.success(map);
    }
}
