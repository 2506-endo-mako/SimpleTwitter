$(function(){
	$('#typo').on('click', function(){
		$('.account').animate({
			opacity: 5,
			fontSize: '2px'
		},
		150
		);
	});
});