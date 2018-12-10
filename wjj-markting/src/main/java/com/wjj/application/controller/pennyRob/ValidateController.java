package com.wjj.application.controller.pennyRob;


import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.bingoohuang.patchca.custom.ConfigurableCaptchaService;
import com.github.bingoohuang.patchca.filter.FilterFactory;
import com.github.bingoohuang.patchca.filter.predefined.CurvesRippleFilterFactory;
import com.github.bingoohuang.patchca.filter.predefined.DiffuseRippleFilterFactory;
import com.github.bingoohuang.patchca.filter.predefined.DoubleRippleFilterFactory;
import com.github.bingoohuang.patchca.filter.predefined.MarbleRippleFilterFactory;
import com.github.bingoohuang.patchca.filter.predefined.WobbleRippleFilterFactory;
import com.github.bingoohuang.patchca.utils.encoder.EncoderHelper;
import com.wjj.application.response.Response;
import com.wjj.application.response.ReturnCode;
import com.wjj.application.util.CaptchaFactory;
import com.wjj.application.util.RedisClient;

import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@Controller
@RequestMapping("/application/verificationCode")
@Slf4j
public class ValidateController {

    /**
     * 日志对象
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private static final long serialVersionUID = 1L;

    private static ConfigurableCaptchaService cs = CaptchaFactory.getInstance();

    private static List<FilterFactory> factories;
    
    @Autowired
    private  HttpServletRequest request;
    @Autowired
    private  HttpServletResponse response;
    @Autowired
    private  HttpSession session;
    private static BASE64Encoder encoder = new sun.misc.BASE64Encoder();
    private static BASE64Decoder decoder = new sun.misc.BASE64Decoder();
    
    @Autowired
    private RedisClient redisClient;
    static {
        factories = new ArrayList<FilterFactory>();
        factories.add(new CurvesRippleFilterFactory(cs.getColorFactory()));
        factories.add(new MarbleRippleFilterFactory());
        factories.add(new DoubleRippleFilterFactory());
        factories.add(new WobbleRippleFilterFactory());
        factories.add(new DiffuseRippleFilterFactory());
    }
    static void base64StringToImage(String base64String) {
    	  try {
    	   byte[] bytes1 = decoder.decodeBuffer(base64String);
    	   ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
    	   BufferedImage bi1 = ImageIO.read(bais);
    	   File f1 = new File("d://out.jpg");
    	   ImageIO.write(bi1, "jpg", f1);
    	  } catch (IOException e) {
    	   e.printStackTrace();
    	  }
    	 }
    @RequestMapping("/defaultKaptcha")
    @ResponseBody
    public Response getImage(String openId) {
        try {
        	
        	if (StringUtils.isBlank(openId)) {
        		return Response.returnCode(ReturnCode.PARAM_INVALID.getCode(),ReturnCode.PARAM_INVALID.getMsg());
            }
            cs.setFilterFactory(factories.get(new Random().nextInt(5)));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            String token = EncoderHelper.getChallangeAndWriteImage(cs, "jpg",
            		out);
            redisClient.setex("kaptcha_"+ openId, token, 120);
            byte[] bytes = out.toByteArray();
            String picStr= encoder.encodeBuffer(bytes).trim();
            return Response.ok(picStr);
        } catch (Exception e) {
        	 log.error(e.getMessage(), e);
        	 return Response.fail();
        }
    }
    
//    private void setResponseHeaders(HttpServletResponse response) {
//        response.setContentType("image/png");
//        response.setHeader("Cache-Control", "no-cache, no-store");
//        response.setHeader("Pragma", "no-cache");
//        long time = System.currentTimeMillis();
//        response.setDateHeader("Last-Modified", time);
//        response.setDateHeader("Date", time);
//        response.setDateHeader("Expires", time);
//    }
//    private void setResponseHeadersJson(HttpServletResponse response,Response r){
//    	response.setCharacterEncoding("UTF-8");  
//    	response.setContentType("application/json;charset=utf-8");  
//    	response.setHeader("Cache-Control", "no-cache, no-store");
//    	response.setHeader("Pragma", "no-cache");
//    	long time = System.currentTimeMillis();
//    	response.setDateHeader("Last-Modified", time);
//    	response.setDateHeader("Date", time);
//    	response.setDateHeader("Expires", time);
//    	PrintWriter out = null;  
//        try {  
//            out = response.getWriter();  
//            out.append( JSON.toJSONString(r));  
//        } catch (IOException e) {  
//            e.printStackTrace();  
//            log.error(e.getMessage(), e);
//        } finally {  
//            if (out != null) {  
//                out.close();  
//            }  
//        }  
//    }

    
}
