
$(function() {

    validateRule();

});
$.validator.setDefaults({
    submitHandler: function() {
		login();
    }
});

var msgCode
//发送验证码
function sendMsg(_this,val){
    if(!val==""){
        if((/^1[345789]\d{9}$/.test(val))){
            var $this = $(_this);
            $(this).prop("disabled",true);
            $.ajax({
                url:ctx+"send-message",
                data:{
                    "mobile":val,
                },
                type:"POST",
                dataType:"json",
                success:function(res){
                    console.log(res)
                    if(res.code ===0){
                        msgCode = res.msg;
                        var sendDate = 60;
                        var inter = setInterval(function(){
                            sendDate--;
                            $this.text(sendDate)
                            if(sendDate<=0){
                                $this.text("发送验证码");
                                $this.prop("disabled",false);
                                clearInterval(inter)
                            }
                        },1000)
                    }else{
                        alert("获取失败");
                        $this.prop("disabled",false);
                    }
                }
            })
        }else{
            alert("手机号码有误，请重填");
        }
    }else{
        alert("手机号不能为空！");
    }
}
function login() {
	var username = $.common.trim($("input[name='username']").val());
    var phonenumber = $.common.trim($("input[name='phonenumber']").val());
    var email = $.common.trim($("input[name='email']").val());
    var password = $.common.trim($("input[name='password']").val());
    var password1 = $.common.trim($("input[name='password1']").val());
    if(Number($(".register-input2").val()!=="")){
    if(Number($(".register-input2").val()===msgCode)) {
        $.ajax({
            type: "post",
            url: ctx + "register",
            dataType: "json",
            data: {
                "username": username,
                "password": password,
                "password1": password1,
                "phonenumber": phonenumber,
                "email": email,
                "loginName": username,

            },
            success: function(r) {
                if (r.code == 0) {
                    alert("注册成功！");
                    location.href = ctx + 'login';
                } else {
                    $.modal.closeLoading();
                    $('.imgcode').click();
                    $(".code").val("");
                    $.modal.msg(r.msg);
                }
            }
        });
    }else {
        alert("验证码错误！")
    }}

}

function validateRule() {
    var icon = "<i class='fa fa-times-circle'></i> ";
    $("#signupForm").validate({
        rules: {
            username: {
                required: true
            },
            password: {
                required: true
            },
            phonenumber: {
                required: true
            },
            email: {
                required: true
            }
        },
        messages: {
            username: {
                required: icon + "请输入您的用户名",
            },
            password: {
                required: icon + "请输入您的密码",
            },
            phonenumber: {
                required: icon + "请输入您的手机号",
            },
            email: {
                required: icon + "请输入您的邮箱",
            }
        }
    })
}

function validateKickout() {
	if (getParam("kickout") == 1) {
	    layer.alert("<font color='red'>您已在别处登录，请您修改密码或重新登录</font>", {
	        icon: 0,
	        title: "系统提示"
	    },
	    function(index) {
	        //关闭弹窗
	        layer.close(index);
	        if (top != self) {
	            top.location = self.location;
	        } else {
	            var url  =  location.search;
	            if (url) {
	                var oldUrl  = window.location.href;
	                var newUrl  = oldUrl.substring(0,  oldUrl.indexOf('?'));
	                self.location  = newUrl;
	            }
	        }
	    });
	}
}

function getParam(paramName) {
    var reg = new RegExp("(^|&)" + paramName + "=([^&]*)(&|$)");
    var r = window.location.search.substr(1).match(reg);
    if (r != null) return decodeURI(r[2]);
    return null;
}