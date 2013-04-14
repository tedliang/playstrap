require(['parser', '$', 
		'dijit/form/Button', 
		'bootstrap/Button', 
		'bootstrap/Modal', 
		'ready!'], function(parser, $){
	parser.parse();
    $("#fat-btn").on("click", function(e){
        $(e.target).button('loading');
        setTimeout(function(){
            $(e.target).button('reset');
        }, 3000);
    });
});