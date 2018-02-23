
$(function() {
    
	var includes = $('[data-include]');
    jQuery.each(includes, function(){
        $(this).load($(this).data('include') + '.html');
    });
    
});
