var pageIndex = 1;
var pageSize = 15;
$(window).load = function(){
	NProgress.start();
}
$(function() {
	var menuItem = $(".nav-link").not($(".has-treeview > .nav-link"));
	menuItem.click(function() {
		if($(this).hasClass('active')){
			return false;
		}
		$(".nav-link").removeClass("active");
		var menu = $(this).parent().parent().parent();
		if(menu.hasClass("has-treeview")){
			menu.children(":first").addClass("active");
		}
		$(this).addClass("active");
		var href =  $(this).attr("data-href");
		getContentData(href);
		return false;
	})
	
	$(document).not($(".has-treeview > .nav-link")).ajaxStart(function(){
		console.log('start');
		NProgress.start();
	})
	$(document).not($(".has-treeview > .nav-link")).ajaxStop(function(){
		console.log('done');
		NProgress.done();
	})
	NProgress.done();
})
function getContentData(api){
	$.ajax({
		url:api,
		type:'get',
		data:{async:'true'},
		success:function(data){
			if(data.code == 0){
				$("#content").html(data.data);
			}
		}
	});
}