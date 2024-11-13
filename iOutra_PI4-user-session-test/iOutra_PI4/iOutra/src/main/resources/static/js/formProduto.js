document.getElementById(`btnNovaImagem`).onclick = function () {
    let qtdFieldset = document.querySelectorAll('#inputImagem > fieldset').length;
    document.getElementById('inputImagem').insertAdjacentHTML('beforeend',
        `
    <fieldset  class="col-md-12 card mx-2 " style="max-width: 250px; max-height: 350px;">
        <img id="selectedImage${qtdFieldset}" onclick="document.getElementById('incluirImagem${qtdFieldset}').click()" src="/img/add-img.svg" class="card-img-top" style="cursor: pointer;" />
        <input class="form-control d-none" id="incluirImagem${qtdFieldset}" type="file" name="imagens[${qtdFieldset}].arquivo"
            onchange="displaySelectedImage(event, 'selectedImage${qtdFieldset}')" accept="image/*"/>
        <div class="d-flex" style="gap: 10px;">
        <input class="checkbox" type="checkbox" name="imagens[${qtdFieldset}].principal" ${(qtdFieldset <= 0) ? 'checked' : ''}/>
            <label class="form-check-label" for="txtOrdenacao${qtdFieldset}">Principal: </label>
        </div>
        <div class="card-footer">
            <button class="btn btn-danger" onclick="removeFieldset(this)">Deletar</button>
        </div>
    </fieldset>
    `);
}

function displaySelectedImage(event, elementId) {
    const selectedImage = document.getElementById(elementId);
    const fileInput = event.target;

    if (fileInput.files && fileInput.files[0]) {
        const reader = new FileReader();

        reader.onload = function (e) {
            selectedImage.src = e.target.result;
        };

        reader.readAsDataURL(fileInput.files[0]);
    }
}


function removeFieldset(button) {
    // Encontre o elemento pai (fieldset) do botão clicado
    var fieldset = button.closest('fieldset');
    // Remova o fieldset do DOM
    fieldset.remove();
}

$(document).ready(function() {
    $('.delete-button').click(function() {
        var fieldset = $(this).closest('fieldset'); // Encontra o fieldset pai
        var imageId = $(this).data('image-id');
        var confirmation = confirm('Tem certeza de que deseja excluir esta imagem?');
        if (confirmation) {
            $.ajax({
                url: '/backoffice/produto/removerimg/' + imageId,
                type: 'DELETE',
                success: function(response) {
                    // Recarregar a página ou atualizar a lista de imagens após a exclusão
                    location.reload(); // Recarregar a página
                    // Você pode atualizar a lista de imagens sem recarregar a página
                    // Aqui você pode adicionar código para atualizar a lista de imagens
                },
                error: function(xhr, status, error) {
                    console.error('Erro ao excluir imagem:', error);
                }
            });
        }
        location.reload();
    });
    
});



