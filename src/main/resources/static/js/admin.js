var pageIndex = 1;
var pageSize = 15;
$(window).load = function() {
	NProgress.start();
}
$(function() {
	$(document).ajaxStart(function() {
		NProgress.start();
	})
	$(document).ajaxStop(function() {
		NProgress.done();
	})
	var menuItem = $(".nav-link").not($(".has-treeview > .nav-link"));
	menuItem.click(function() {
		if ($(this).hasClass('active')) {
			return true;
		}
		$(".nav-link").removeClass("active");
		var menu = $(this).parent().parent().parent();
		if (menu.hasClass("has-treeview")) {
			menu.children(":first").addClass("active");
		}
		$(this).addClass("active");
		return true;
	})

	route(function(path) {
		loadPage(path);
	});
	route.start(true);

})

function loadPage(path) {
	// clear(path);
	if (path === '') {
		path = "dashboard";
	}
	reactSideBar(path);
	console.info('path = ' + path);
	loadHTML(path, "content");
}
function reactSideBar(path){
	var current = $(".nav-sidebar a[data-href="+path+"]")[0];
	if(current != null){
		$(".nav-sidebar a").removeClass('active');
		$(".nav-sidebar a").removeClass('menu-open');
		$(current).parents(".has-treeview").addClass('menu-open')
		$(current).addClass('active');
	}
}
function back() {
	var path = g_getAnchorString();
	loadHTML(path, "content");
}
function filmEdit(obj) {
	var href = obj.dataset.href;
	loadHTML(href, 'content');
}
function filmDelete(obj) {
	layer.confirm('你确定要删除此项吗?', {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function(index) {
		var href = obj.dataset.href;
		$.ajax({
			url : href,
			type : 'get',
			success : function(data) {
				if (data.code == 0) {
					table.row($(obj).parents('tr')).remove().draw();
					$(obj).parent().parent().remove();
				}
			}
		});
		layer.close(index);
	}, function() {
	});
}
function loadHTML(api, id) {
	$.ajaxSetup({
		'cache' : true
	});
	$.get(api, {
		async : true
	}, function(html,textStatus,jqXHR) {
		if(textStatus == 'success'){
			$('#' + id).html(html);
		}
	})
}
