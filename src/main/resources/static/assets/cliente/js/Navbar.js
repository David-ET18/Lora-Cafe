document.addEventListener("DOMContentLoaded", () => {
    actualizarContadorNavbar();

    const mobileMenuToggle = document.querySelector('.mobile-menu-toggle');
    const mobileNav = document.querySelector('.mobile-nav');

    if (mobileMenuToggle && mobileNav) {
        mobileMenuToggle.addEventListener('click', () => {
            mobileNav.classList.toggle('active');
        });
    }

    const userMenu = document.querySelector('.user-menu');
    if (userMenu) {
        const userMenuButton = userMenu.querySelector('.user-menu-button');

        userMenuButton.addEventListener('click', (event) => {
            event.stopPropagation();
            userMenu.classList.toggle('active');
        });

        document.addEventListener('click', () => {
            if (userMenu.classList.contains('active')) {
                userMenu.classList.remove('active');
            }
        });
    }
});

function actualizarContadorNavbar() {
    const cuentaCarrito = document.getElementById("cuenta-carrito");
    if (!cuentaCarrito) return;

    const carrito = JSON.parse(localStorage.getItem("productos")) || [];
    const totalProductos = carrito.reduce((acc, producto) => acc + producto.cantidad, 0);

    cuentaCarrito.textContent = totalProductos;

    if (totalProductos > 0) {
        cuentaCarrito.style.display = 'flex';
    } else {
        cuentaCarrito.style.display = 'none';
    }
}