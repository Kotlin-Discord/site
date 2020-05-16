const mobileMenu = $(".mobile-menu").first()

mobileMenu.sidebar({
    exclusive: true,
}).sidebar("attach events", ".mobile-menu-button")

$(window).resize(function() {
    if ($(window).width() > 992) {
        mobileMenu.sidebar("hide")
    }
})
