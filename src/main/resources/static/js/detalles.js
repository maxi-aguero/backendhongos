document.addEventListener('DOMContentLoaded', () => {
    const details = JSON.parse(localStorage.getItem('hongoDetalles'));
    if (details) {
        document.getElementById('titulo').textContent = details.title;
        document.getElementById('imagen').src = details.imgSrc;
        document.getElementById('descripcion').textContent = details.extract;
    } else {
        document.getElementById('titulo').textContent = 'Detalles no disponibles';
    }

    //
});
