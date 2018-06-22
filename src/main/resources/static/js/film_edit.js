function save_film() {
	var json = getFormRequestBody("#film-edit");
	$.ajax({
		url : "film/edit",
		data : JSON.stringify(json),
		method : "POST",
		dataType : "json",
		timeout : 30000,
		contentType : "application/json",
		success : function(data) {
			if (data.code == 0) {
				layer.msg("修改成功");
				back();
			}
		}
	})
}