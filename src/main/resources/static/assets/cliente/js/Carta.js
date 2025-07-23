document.addEventListener("DOMContentLoaded", () => {
    fetchAndDisplayProducts('todos');

    setupFilterButtons();
});


function setupFilterButtons() {
    const filterButtons = document.querySelectorAll(".btn-item");
    filterButtons.forEach((button) => {
        button.addEventListener("click", (e) => {
            filterButtons.forEach(btn => btn.classList.remove('active'));
            e.currentTarget.classList.add('active');

            const categoriaId = e.currentTarget.getAttribute('data-id-categoria');

            fetchAndDisplayProducts(categoriaId);
        });
    });
}

/**
  @param {string} categoriaId 
 */
function fetchAndDisplayProducts(categoriaId) {
    const container = document.getElementById("productos-container");
    if (!container) {
        console.error("El contenedor de productos no fue encontrado.");
        return;
    }

    container.innerHTML = '<p style="text-align:center; color: #333;">Cargando productos...</p>';

    const url = (categoriaId === 'todos')
        ? '/api/client/products'
        : `/api/client/products?categoriaId=${categoriaId}`;

    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Error al cargar los productos desde el servidor.');
            }
            return response.json();
        })
        .then(productos => {
            container.innerHTML = '';

            if (productos.length === 0) {
                container.innerHTML = '<p style="text-align:center; color: #333;">No hay productos disponibles en esta categoría.</p>';
                return;
            }

            productos.forEach(producto => {
                const productoHTML = `
                    <div class="menu-items col-lg-6 col-sm-12">
                        <img src="${producto.imagenUrl || '/Vista/Imagenes/img-carta/default-product.png'}" alt="${producto.nombre}" class="photo" />
                        <div class="menu-info">
                            <div class="menu-title">
                                <h4>${producto.nombre}</h4>
                            </div>
                            <div class="menu-text">
                                ${producto.descripcion}
                            </div>
                            <div class="price-buy-container">
                                <h4 class="price">S/ ${producto.precio.toFixed(2)}</h4>
                                <button class="cart-button" data-product-id="${producto.id}">
                                    <span class="add-to-cart">Comprar</span>
                                    <span class="added">Comprado</span>
                                    <i class="fa-solid fa-mug-saucer"></i>
                                    <i class="fa-solid fa-cube"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                `;
                container.innerHTML += productoHTML;
            });

            attachAddToCartListeners();
        })
        .catch(error => {
            console.error('Error al obtener productos:', error);
            container.innerHTML = '<p style="text-align:center; color: #cc0000;">Ocurrió un error al cargar los productos.</p>';
        });
}


function attachAddToCartListeners() {
    const cartButtons = document.querySelectorAll(".cart-button");
    cartButtons.forEach((button) => {
        button.addEventListener("click", (e) => {
            const id = parseInt(e.currentTarget.getAttribute('data-product-id'));

            fetch(`/api/client/products/${id}`)
                .then(res => {
                    if (!res.ok) throw new Error('Producto no encontrado');
                    return res.json();
                })
                .then(producto => {
                    agregarAlCarrito(producto);
                    activarAnimacion(button);
                })
                .catch(err => {
                    console.error("Error al añadir al carrito:", err);
                    alert("No se pudo añadir el producto al carrito.");
                });
        });
    });
}

/**
  @param {object} producto 
 */
function agregarAlCarrito(producto) {
    let carrito = JSON.parse(localStorage.getItem("productos")) || [];

    const indiceProducto = carrito.findIndex((item) => item.id === producto.id);

    if (indiceProducto === -1) {
        producto.cantidad = 1;
        carrito.push(producto);
    } else {
        carrito[indiceProducto].cantidad++;
    }

    localStorage.setItem("productos", JSON.stringify(carrito));

    if (typeof actualizarContadorNavbar === 'function') {
        actualizarContadorNavbar();
    }
}

/**
  @param {HTMLElement} button 
 */
function activarAnimacion(button) {
    button.classList.add("clicked");
    setTimeout(() => {
        button.classList.remove("clicked");
    }, 2000);
}