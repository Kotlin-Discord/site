const mobileMenu = $(".mobile-menu").first()

mobileMenu.sidebar({
    exclusive: true,
}).sidebar("attach events", ".mobile-menu-button")
