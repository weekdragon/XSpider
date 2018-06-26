//展示loading
function g_showLoading() {
	var idx = layer.msg('处理中...', {
		icon : 16,
		shade : [ 0.5, '#f5f5f5' ],
		scrollbar : false,
		offset : '0px',
		time : 100000
	});
	return idx;
}
// salt
var g_passsword_salt = "1a2b3c4d"
// 获取url参数
function g_getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
};
// 获取#号后面的数据
function g_getAnchorString() {
	var ss = window.location.href.split("#");
	if (ss.length > 1)
		return ss[ss.length - 1];
	return "";
};
// 设定时间格式化函数，使用new Date().format("yyyyMMddhhmmss");
Date.prototype.format = function(format) {
	var args = {
		"M+" : this.getMonth() + 1,
		"d+" : this.getDate(),
		"h+" : this.getHours(),
		"m+" : this.getMinutes(),
		"s+" : this.getSeconds(),
	};
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	for ( var i in args) {
		var n = args[i];
		if (new RegExp("(" + i + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? n : ("00" + n).substr(("" + n).length));
	}
	return format;
};

function getFormRequestBody(fromSelector) {
	var formFields = $(fromSelector).serializeArray();
	var requestBody = {};
	for (var i = 0; i < formFields.length; i++) {
		var el = $(fromSelector).find("input[name='" + formFields[i]['name'] + "']");
		if (el.attr("type") === "checkbox") {
			requestBody[formFields[i]['name']] = formFields[i]['value'] !== undefined;
		} else {
			if (formFields[i]['name'] == "category") {
				requestBody[formFields[i]['name']] = formFields[i]['value'].split(',');
			} else if (!formFields[i]['value']) {
				requestBody[formFields[i]['name']] = null;
			} else {
				requestBody[formFields[i]['name']] = formFields[i]['value'];
			}
		}
	}
	var checkBoxes = $(fromSelector).find("input[type='checkbox']");
	for (var i = 0; i < checkBoxes.length; i++) {
		var name = $(checkBoxes[i]).attr("name");
		if (name !== '' && !requestBody[name]) {
			requestBody[name] = false;
		}
	}
	return requestBody;
}
