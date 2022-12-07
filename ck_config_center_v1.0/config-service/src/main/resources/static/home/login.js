$(function () {
    validateRule();
});

$.validator.setDefaults({
    submitHandler: function () {
        login();
    }
});

function login() {
    console.log("点击登录");
    // $.modal.loading($("#btnSubmit").data("loading"));
    var username = $.common.trim($("input[name='username']").val());
    var password = $.common.trim($("input[name='password']").val());
    var logUrl = ctx + "web/login";
    $.ajax({
        type: "post",
        url: logUrl,
        data: {
            "username": username,
            "password": password,
        },
        success: function (r) {
            console.log("Request url ： " + logUrl);
            var homeUrl = ctx + "web/index";
            if (r.operateStatusEnum === "SUCCESS") {
                location.href = homeUrl;
            } else {
                $.modal.closeLoading();
                $.modal.msg(r.message);
            }
        }
    });
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
            }
        },
        messages: {
            username: {
                required: icon + "请输入您的用户名",
            },
            password: {
                required: icon + "请输入您的密码",
            }
        }
    })
}

