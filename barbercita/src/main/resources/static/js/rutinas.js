// funcion para hacer un preview de una imagen
function mostrarImagen(input, previewId = 'blah') {
    if (input.files && input.files[0]) {
        const imagen = input.files[0];
        const maximo = 512 * 1024; //Se limita el tamano a 512 Kb las imagenes.
        if (imagen.size <= maximo) {
            var lector = new FileReader();
            lector.onload = function (e) {
                $('#' + previewId).attr('src', e.target.result).height(200).removeClass('d-none');
            };
            lector.readAsDataURL(input.files[0]);
        } else {
            alert("La imagen seleccionada es muy grande... no debe superar los 512 Kb!");
            input.value = '';
        }
    }
}

//Para insertar informacion en el modal de eliminar segun el registro...
document.addEventListener('DOMContentLoaded', function () {
    const confirmModal = document.getElementById('confirmModal');
    if (!confirmModal) {
        return;
    }
    confirmModal.addEventListener('show.bs.modal', function (event) {
        const button = event.relatedTarget;
        document.getElementById('modalId').value = button.getAttribute('data-bs-id');
        document.getElementById('modalDescripcion').textContent = button.getAttribute('data-bs-descripcion');
    });
});

//Para quitar el toast despues de 4 segundos
document.addEventListener('DOMContentLoaded', function () {
    setTimeout(() => {
        document.querySelectorAll('.toast').forEach(toast =>
            toast.classList.remove('show'));
    }, 4000);
});