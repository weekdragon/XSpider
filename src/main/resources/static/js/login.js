$("#btn-login").click(function() {
	g_showLoading();
	var inputPass = $("#input-password").val();
	var salt = g_passsword_salt;
	var str = ""+salt.charAt(0)+salt.charAt(2) + inputPass +salt.charAt(5) + salt.charAt(4);
	var password = md5(str);
	$.ajax({
		url : "/admin/login",
		type : "post",
		data : {
			userName : $("#input-username").val(),
			password : password
		},
		success : function(data) {
			layer.closeAll();
			if (data.code == 0) {
				layer.msg("成功");
				window.location.href = "/admin/index";
			} else {
				layer.msg(data.msg);
			}
		}
	});
	return false;
})