const tagsRegExp = /^(#\w+ )*(#\w+)? *$/; // Example: #tag1 #tag2 #tag3

// Shows an error box at the top of the $parent DOM object containing the specified message.
function showError($parent, msg) {
    let $self = $(`<div class="error-message"><p>${msg}</p></div>`).prependTo($parent);
    $self.append('<button type="button">X</button>').click(() => {
        $self.remove()
    });
}

// Uploads the article to the server.
function uploadArticle() {
   if(verifyFields()) {
       let formData = new FormData(); // Useful to create complex json requests
       formData.append('title', $('#title').val());
       formData.append('description', $('#description').val());
       formData.append('text', $('#text').val());
       formData.append('category', $('#category').val());
       formData.append('tags', $('#tags').val());
       formData.append('img', $('#img')[0].files[0]);
       $.ajax({
           url: '/Vuvuzela/services/article/write_article.php',
           type: 'POST',
           data: formData,
           processData: false,
           contentType: false,
           enctype: 'multipart/form-data',
           success: (msg) => {
               if(msg['status'] === 'error') {
                   showError($('.content'), 'Error uploading article');
                   console.log(msg['errorMsg']);
               } else if(msg['status'] === 'success') {
                   window.location.replace('/Vuvuzela/pages/home.php');
               }
           },
           error: (jqXHR, textStatus, errorThrown )  => {
               showError($('.content'), 'Error uploading article');
               console.log('Error: ' + errorThrown + ' ' + textStatus);
           },
           dataType: 'json'
       });
   }
}

// Checks whether all fields are non empty or in the correct format.
function verifyFields() {
    return $('#title').val().trim() !== '' && $('#description').val().trim() !== '' && $('#text').val().trim() !== '' &&
        $('#category').val().trim() !== '' && tagsRegExp.test($('#tags').val()) &&  $('#img')[0].files.length !== 0;
}

// Register the error listener on the elements. The error listener changes whether the upload button is
// disabled or not
function registerErrorListener($element) {
    $element.keyup(() => $('#upload-button').prop('disabled', !verifyFields()));
}

$(document).ready(() => {
    let $uploadButton = $('#upload-button');
    let $tags = $('#tags');
    $uploadButton.click(uploadArticle);
    registerErrorListener($('#title'));
    registerErrorListener($('#description'));
    registerErrorListener($('#text'));
    registerErrorListener($('#category'));
    registerErrorListener($tags);
    $('#img').change(() => $uploadButton.prop('disabled', !verifyFields()));
    $tags.keyup(() => $tags.toggleClass('input-field-text-error', !tagsRegExp.test($tags.val())));
    $('#back-button').click(() => window.location.replace('/Vuvuzela/pages/home.php'));
});