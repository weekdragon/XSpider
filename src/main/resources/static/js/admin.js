var pageIndex = 1;
var pageSize = 15;
$(window).load = function(){
	NProgress.start();
}
$(function() {
	var menuItem = $(".nav-link").not($(".has-treeview > .nav-link"));
	menuItem.click(function() {
		if($(this).hasClass('active')){
			return true;
		}
		$(".nav-link").removeClass("active");
		var menu = $(this).parent().parent().parent();
		if(menu.hasClass("has-treeview")){
			menu.children(":first").addClass("active");
		}
		$(this).addClass("active");
		var href =  $(this).attr("data-href");
		loadHTMLData(href,"content");
		return true;
	})
	$(document).ajaxStart(function(){
		NProgress.start();
	})
	$(document).ajaxStop(function(){
		NProgress.done();
	})
})
function back(){
	var path = g_getAnchorString();
	loadHTMLData(path,"content");
}
function filmEdit(obj){
	var href = obj.dataset.href;
	loadHTML(href,'content');
}
function loadHTML(api,id){
	$.ajax({
		url:api,
		type:'get',
		success:function(html){
				$('#'+id).html(html);
		}
	});
}
function loadHTMLData(api,id){
	$.ajax({
		url:api,
		type:'get',
		data:{async:true},
		success:function(data){
			if(data.code == 0){
				$('#'+id).html(data.data);
			}else{
				//这里应该放出错误页面
			}
		}
	});
}