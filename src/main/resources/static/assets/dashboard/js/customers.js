const API_URL_CUSTOMERS = '/api/dashboard/customers';


document.addEventListener('DOMContentLoaded', () => {
    if (document.getElementById('customers-page')?.classList.contains('active')) {
        populateCustomersTable();
        setupCustomersEventListeners();
    }
});


function setupCustomersEventListeners() {
    const customerSearch = document.getElementById('customer-search');
    customerSearch?.addEventListener('input', (e) => {
        populateCustomersTable(e.target.value);
    });
}

/**
  @param {string} searchTerm 
 */
function populateCustomersTable(searchTerm = '') {
    const tableBody = document.querySelector('#customers-table tbody');
    if (!tableBody) return;
    tableBody.innerHTML = '<tr><td colspan="6" style="text-align:center;">Cargando clientes...</td></tr>';

    const url = API_URL_CUSTOMERS;

    fetch(url)
        .then(response => response.json())
        .then(customers => {
            tableBody.innerHTML = '';

            const filteredCustomers = searchTerm
                ? customers.filter(c =>
                    (c.nombre + ' ' + c.apellido).toLowerCase().includes(searchTerm.toLowerCase()) ||
                    c.email.toLowerCase().includes(searchTerm.toLowerCase()))
                : customers;

            if (filteredCustomers.length === 0) {
                tableBody.innerHTML = '<tr><td colspan="6" style="text-align:center;">No se encontraron clientes.</td></tr>';
                return;
            }

            filteredCustomers.forEach(customer => {
                const row = tableBody.insertRow();
                row.innerHTML = `
                    <td>${customer.nombre} ${customer.apellido}</td>
                    <td>${customer.email}</td>
                    <td>${customer.telefono || 'N/A'}</td>
                    <td>${new Date(customer.fechaRegistro).toLocaleDateString()}</td>
                    <td>${customer.totalPedidos}</td>
                    <td>
                        <a href="/dashboard/users" class="action-btn edit" title="Gestionar en Usuarios"><i class="fas fa-edit"></i></a>
                    </td>
                `;
            });
        })
        .catch(error => {
            console.error('Error al cargar clientes:', error);
            tableBody.innerHTML = '<tr><td colspan="6" style="text-align:center;">Error al cargar los clientes.</td></tr>';
        });
}