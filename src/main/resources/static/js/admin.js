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
	reactSideBar(path.split('_')[0]);
	var realPath = path.replace(/-/g, '/');
	console.info('realPath = ' + realPath);
	loadHTML(realPath, "content");
}
function reactSideBar(path) {
	var current = $(".nav-sidebar a[data-href=" + path + "]")[0];
	if (current != null) {
		$(".nav-sidebar a").removeClass('active');
		$(".nav-sidebar a").removeClass('menu-open');
		$(current).parents(".has-treeview").addClass('menu-open')
		$(current).addClass('active');
	}
}
function filmEdit(obj) {
	var href = obj.dataset.href;
	loadHTML(href, 'content');
}
function batFilmDelete(ids) {
	var selects = $("input[class='checkChildren']:checked");
	if(selects.length == 0){
		layer.msg("未选择任何项");
		return;
	}
	var array = new Array();
	selects.each(function(i){
		array.push($(this).attr('data-id'));
	})
	var args = array.join('_');
	layer.confirm('你确定要删除这些项吗?', {
		btn : [ '确定', '取消' ]
	// 按钮
	}, function(index) {
		var href = 'film\\'+args+'\\to_delete';
		$.ajax({
			url : href,
			type : 'get',
			success : function(data) {
				if (data.code == 0) {
					selects.each(function(i){
						table.row($(this).parents('tr')).remove();
					})
					table.draw();
					console.log($("input[class='checkChildren']:checked").length);
					console.log($(".checkChildren").length);
					$("#checkall").prop('checked', $("input[class='checkChildren']:checked").length == $(".checkChildren").length);
				}
			}
		});
		layer.close(index);
	}, function() {
	});
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
	}, function(html, textStatus, jqXHR) {
		if (textStatus == 'success') {
			$('#' + id).html(html);
		}
	})
}
