package com.ruoyi.project.system.user.controller;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.utils.ServletUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.sendMsg;
import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.system.user.domain.User;
import com.ruoyi.project.system.user.service.IUserService;
import org.apache.ibatis.annotations.Param;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录验证
 * 
 * @author ruoyi
 */
@Controller
public class LoginController extends BaseController
{
    @Autowired
    private IUserService userService;
    @GetMapping("/login")
    public String login(HttpServletRequest request, HttpServletResponse response)
    {
        // 如果是Ajax请求，返回Json字符串。
        if (ServletUtils.isAjaxRequest(request))
        {
            return ServletUtils.renderString(response, "{\"code\":\"1\",\"msg\":\"未登录或登录超时。请重新登录\"}");
        }

        return "login";
    }
    @GetMapping("/register")
    public String register(HttpServletRequest request, HttpServletResponse response)
    {
        // 如果是Ajax请求，返回Json字符串。

        return "register";
    }
    @GetMapping("/wangjimima")
    public String wangjimima(HttpServletRequest request, HttpServletResponse response)
    {
        // 如果是Ajax请求，返回Json字符串。

        return "wangjimima";
    }
    @PostMapping("/wangjimima")
    @ResponseBody
    public AjaxResult wangjimima(User user)
    {
        if(!user.getPassword().equals(user.getPassword1())){
            return error("两次输入密码不同，请确定密码！");
    }
        User user1= userService.selectUserByPhoneNumber(user.getPhonenumber());
        if(user1!=null){
            if (StringUtils.isNotEmpty(user.getPassword()))
            {
                user1.setPassword(user.getPassword());
                if (userService.resetUserPwd(user1) > 0)
                {

                    return success();
                }
                return error();
            }
            else
            {
                return error("修改密码失败");
            }
        }else {
            return error("手机号未注册！");

        }

    }
    @PostMapping("/register")
    @ResponseBody
    public AjaxResult addSave(@Validated User user)
    {
        if(!user.getPassword().equals(user.getPassword1())){
            return error("两次输入密码不同，请确定密码！");
        }
        user.setUserName(user.getLoginName());
        user.setDeptId(116l);
        user.setSex("0");
        user.setStatus("0");
        Long[] a={5l};
        user.setRoleIds(a);
        if (UserConstants.USER_NAME_NOT_UNIQUE.equals(userService.checkLoginNameUnique(user.getLoginName())))
        {
            return error("新增用户'" + user.getLoginName() + "'失败，登录账号已存在");
        }
        else if (UserConstants.USER_PHONE_NOT_UNIQUE.equals(userService.checkPhoneUnique(user)))
        {
            return error("新增用户'" + user.getLoginName() + "'失败，手机号码已存在");
        }
        else if (UserConstants.USER_EMAIL_NOT_UNIQUE.equals(userService.checkEmailUnique(user)))
        {
            return error("新增用户'" + user.getLoginName() + "'失败，邮箱账号已存在");
        }
        return toAjax(userService.insertUser(user));
    }
    @PostMapping("/login")
    @ResponseBody
    public AjaxResult ajaxLogin(String username, String password, Boolean rememberMe)
    {
        UsernamePasswordToken token = new UsernamePasswordToken(username, password, rememberMe);
        Subject subject = SecurityUtils.getSubject();
        try
        {
            subject.login(token);
            return success();
        }
        catch (AuthenticationException e)
        {
            String msg = "用户或密码错误";
            if (StringUtils.isNotEmpty(e.getMessage()))
            {
                msg = e.getMessage();
            }
            return error(msg);
        }
    }

    @GetMapping("/unauth")
    public String unauth()
    {
        return "error/unauth";
    }

    @RequestMapping(value = "/send-message")
    @ResponseBody
    public AjaxResult sendMessage(@Param(value = "mobile") String mobile) {
        //设置超时时间-可自行调整
//        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
//        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
//        //初始化ascClient需要的几个参数
//        final String product = "Dysmsapi";//短信API产品名称（短信产品名固定，无需修改）
//        final String domain = "dysmsapi.aliyuncs.com";//短信API产品域名（接口地址固定，无需修改）
//        //替换成你的AK
//        final String accessKeyId = "LTAI4FwfUq4LpEMKN4uDsUNp";//你的accessKeyId,参考本文档步骤2
//        final String accessKeySecret = "AvBwfO1SQYPjQt9pqSFfQv6SqvyYTf";//你的accessKeySecret，参考本文档步骤2

        String code = getRegisterCode();
//        try {
//            //初始化ascClient,暂时不支持多region（请勿修改）
//            IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
//            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
//            IAcsClient acsClient = new DefaultAcsClient(profile);
//            //组装请求对象
//            SendSmsRequest request = new SendSmsRequest();
//            //使用post提交
//            request.setMethod(MethodType.POST);
//            //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
//            request.setPhoneNumbers(mobile);
//            //必填:短信签名-可在短信控制台中找到
//            request.setSignName("北京艺传教育科技有限公司 ");
//            //必填:短信模板-可在短信控制台中找到
//            request.setTemplateCode("SMS_176180053");
//            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
//            //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
//            request.setTemplateParam("{\"code\":\"" + code + "\"}");
//            //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
//            //request.setSmsUpExtendCode("90997");
//            //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
//            request.setOutId("yourOutId");
//            //请求失败这里会抛ClientException异常
//            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
//            if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
                //请求成功
        try {
            String a=sendMsg.sendMsg1(code,mobile);
            if("0".equals(a)){
                return success(code);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

//            }
//        } catch (ClientException clientException) {
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
        return error("验证码发送错误");
    }
    public static String getRegisterCode(){
        StringBuilder code = new StringBuilder();
        for(int i=0;i<6;i++){
            int number = (int)(Math.random()*10);
            code.append(number);
        }
        return code.toString();
    }

}
