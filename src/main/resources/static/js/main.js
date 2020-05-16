const desktopMenu = $("#desktop-menu").first()
const mobileMenu = $("#mobile-menu").first()

desktopMenu.sticky()

mobileMenu.sidebar({
    exclusive: true,
}).sidebar("attach events", ".mobile-menu-button")

$(window).resize(function() {
    if ($(window).width() > 992) {
        mobileMenu.sidebar("hide")
    }
})
