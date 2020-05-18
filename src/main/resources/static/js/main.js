// Set up sticky desktop sidebar, and collapsed mobile sidebar

const desktopMenu = $("#desktop-menu").first()
const mobileMenu = $("#mobile-menu").first()

desktopMenu.sticky({
    context: "#content-pusher",
})

mobileMenu.sidebar({
    exclusive: true,
}).sidebar("attach events", ".mobile-menu-button")

$(window).resize(function () {
    if ($(window).width() > 992) {
        mobileMenu.sidebar("hide")
    }
})

// If we have accordions, set them up

$('.ui.accordion')
    .accordion({
        exclusive: false,
    })
