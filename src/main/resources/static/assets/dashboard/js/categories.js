const API_URL_CATEGORIES = '/api/dashboard/categories';

function getCsrfToken() {
    const token = document.querySelector("meta[name='_csrf']")?.content;
    const header = document.querySelector("meta[name='_csrf_header']")?.content;
    return { token, header };
}


document.addEventListener('DOMContentLoaded', () => {
    if (document.getElementById('categories-page')?.classList.contains('active')) {
        populateCategoriesTable();
        document.getElementById('add-category-btn')?.addEventListener('click', () => showCategoryModal());
    }
});


function populateCategoriesTable() {
    const tableBody = document.querySelector('#categories-table tbody');
    if (!tableBody) return;
    tableBody.innerHTML = '<tr><td colspan="4" style="text-align:center;">Cargando categorías...</td></tr>';

    fetch(API_URL_CATEGORIES)
        .then(res => res.json())
        .then(categories => {
            tableBody.innerHTML = '';
            if (categories.length === 0) {
                tableBody.innerHTML = '<tr><td colspan="4" style="text-align:center;">No hay categorías creadas.</td></tr>';
                return;
            }
            categories.forEach(cat => {
                const row = tableBody.insertRow();
                row.innerHTML = `
                    <td>${cat.nombre}</td>
                    <td>${cat.descripcion || 'Sin descripción'}</td>
                    <td><span class="status ${cat.activa ? 'completed' : 'cancelled'}">${cat.activa ? 'Activa' : 'Inactiva'}</span></td>
                    <td>
                        <button class="action-btn edit" data-id="${cat.id}"><i class="fas fa-edit"></i></button>
                        <button class="action-btn delete" data-id="${cat.id}"><i class="fas fa-trash"></i></button>
                    </td>`;
            });
            attachCategoryActionListeners();
        })
        .catch(err => {
            console.error("Error al cargar categorías:", err);
            tableBody.innerHTML = '<tr><td colspan="4" style="text-align:center;">Error al cargar las categorías.</td></tr>';
        });
}


function attachCategoryActionListeners() {
    document.querySelectorAll('#categories-table .action-btn.edit').forEach(btn => {
        btn.addEventListener('click', async e => {
            const id = e.currentTarget.dataset.id;
            const response = await fetch(API_URL_CATEGORIES);
            const categories = await response.json();
            const categoryToEdit = categories.find(c => c.id == id);
            if (categoryToEdit) {
                showCategoryModal(categoryToEdit);
            }
        });
    });

    document.querySelectorAll('#categories-table .action-btn.delete').forEach(btn => {
        btn.addEventListener('click', e => {
            if (confirm('¿Estás seguro de que quieres eliminar esta categoría? \n¡Esto podría afectar a los productos asociados!')) {
                deleteCategory(e.currentTarget.dataset.id);
            }
        });
    });
}

function deleteCategory(id) {
    const csrf = getCsrfToken();
    fetch(`${API_URL_CATEGORIES}/${id}`, {
        method: 'DELETE',
        headers: { [csrf.header]: csrf.token }
    }).then(response => {
        if (response.ok) {
            alert('Categoría eliminada con éxito.');
            populateCategoriesTable();
        } else { throw new Error('No se pudo eliminar la categoría. Asegúrate de que no tenga productos asociados.'); }
    }).catch(error => alert(error.message));
}

function showCategoryModal(cat = null) {
    const isEditMode = cat !== null;
    const modalContent = `
        <div class="modal active" id="category-modal">
            <div class="modal-content">
                <div class="modal-header">
                    <h3>${isEditMode ? 'Editar Categoría' : 'Nueva Categoría'}</h3>
                    <button class="close-modal" onclick="closeModal()">×</button>
                </div>
                <div class="modal-body">
                    <form id="category-form">
                        <input type="hidden" id="cat-id" value="${isEditMode ? cat.id : ''}">
                        <div class="form-group">
                            <label for="cat-nombre">Nombre de la Categoría</label>
                            <input type="text" id="cat-nombre" class="form-control" value="${isEditMode ? cat.nombre : ''}" required>
                        </div>
                        <div class="form-group">
                            <label for="cat-descripcion">Descripción</label>
                            <textarea id="cat-descripcion" class="form-control" rows="3">${isEditMode ? cat.descripcion || '' : ''}</textarea>
                        </div>
                        <div class="form-group">
                            <label for="cat-activa">Estado</label>
                            <select id="cat-activa" class="form-control">
                                <option value="true" ${isEditMode && cat.activa ? 'selected' : ''}>Activa</option>
                                <option value="false" ${isEditMode && !cat.activa ? 'selected' : ''}>Inactiva</option>
                            </select>
                        </div>
                    </form>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn" onclick="closeModal()">Cancelar</button>
                    <button type="button" class="btn btn-primary" id="save-cat-btn">${isEditMode ? 'Actualizar' : 'Guardar'}</button>
                </div>
            </div>
        </div>`;

    document.getElementById('modal-container').innerHTML = modalContent;
    document.getElementById('save-cat-btn').addEventListener('click', saveCategory);
}

function saveCategory() {
    const id = document.getElementById('cat-id').value;
    const isEditMode = id !== '';

    const categoryData = {
        nombre: document.getElementById('cat-nombre').value,
        descripcion: document.getElementById('cat-descripcion').value,
        activa: document.getElementById('cat-activa').value === 'true'
    };

    if (!categoryData.nombre) {
        alert('El nombre de la categoría es obligatorio.');
        return;
    }

    const csrf = getCsrfToken();
    const method = isEditMode ? 'PUT' : 'POST';
    const url = isEditMode ? `${API_URL_CATEGORIES}/${id}` : API_URL_CATEGORIES;

    fetch(url, {
        method: method,
        headers: { 'Content-Type': 'application/json', [csrf.header]: csrf.token },
        body: JSON.stringify(categoryData)
    })
        .then(response => {
            if (!response.ok) throw new Error(`Error al guardar la categoría.`);
            return response.json();
        })
        .then(() => {
            alert(`Categoría ${isEditMode ? 'actualizada' : 'creada'} con éxito.`);
            closeModal();
            populateCategoriesTable();
        })
        .catch(error => alert(error.message));
}

function closeModal() {
    document.getElementById('modal-container').innerHTML = '';
}