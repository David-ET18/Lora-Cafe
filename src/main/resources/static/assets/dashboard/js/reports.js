document.addEventListener('DOMContentLoaded', () => {
    if (document.getElementById('reports-page')?.classList.contains('active')) {
        setupReportsEventListeners();
    }
});

function setupReportsEventListeners() {

    document.querySelector('.generate-report[data-type="products"]')?.addEventListener('click', () => {
        window.open('/api/dashboard/reports/products', '_blank');
    });

    document.querySelector('.generate-report[data-type="sales"]')?.addEventListener('click', () => {
        window.open('/api/dashboard/reports/sales', '_blank');
    });

    document.querySelector('.generate-report[data-type="customers"]')?.addEventListener('click', () => {
        window.open('/api/dashboard/reports/customers', '_blank');
    });
}