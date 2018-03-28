$('#returnTop').click(function() {
    $("html, body").animate({ scrollTop: 0 }, 120);
});

$(window).bind("scroll", function () {
    var st = $(document).scrollTop();
    (st > 0)? $('#returnTop').show('slow'): $('#returnTop').hide('slow');
});