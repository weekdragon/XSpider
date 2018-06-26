$(function(){
	 $('#category').tagsInput({
        width: 'auto',
        onChange: function(elem, elem_tags)
        {
          var languages = ['php','ruby','javascript'];
          $('.tag', elem_tags).each(function()
          {
            if($(this).text().search(new RegExp('\\b(' + languages.join('|') + ')\\b')) >= 0)
              $(this).css('background-color', 'red');
          });
        },
        onAddTag:function(elem,elem_tags){
        	var languages = ['php','ruby','javascript'];
            $('.tag', elem_tags).each(function()
            {
              if($(this).text().search(new RegExp('\\b(' + languages.join('|') + ')\\b')) >= 0)
                $(this).css('background-color', 'red');
            });
        },
        defaultText:'添加标签'
      });
})
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