const TYPE_LOG_IN = 0;
const TYPE_SIGN_UP = 1;

const MSG_OK = 0;
const MSG_WRONG_CREDENTIALS = 1;
const MSG_USER_ALREADY_EXISTS = 2;
const MSG_GENERIC_ERROR = 3;

let type = TYPE_LOG_IN;

// Shows an error message inside the $parent container.
function showError(msg) {
    let $message = $('#message');
    let $self = $(`<div class="error-message"><p>${msg}</p></div>`).prependTo($message);
    $self.append('<button type="button">X</button>').click(() => { $self.remove() });
    $message.show();
}

function switchButtonClickHandler() {
    $('#message').hide();
    if (type === TYPE_LOG_IN) {
        $('#access-title').text("Sign up");
        $('#access-action-button').text("Sign up");
        $('#switch-text').text("Already have an account?");
        $('#switch-button').text("Log in");
        $('#editor-wrapper').css('display', 'none');

        type = TYPE_SIGN_UP;
    } else if(type === TYPE_SIGN_UP) {
        $('#access-title').text("Log in");
        $('#access-action-button').text("Log in");
        $('#switch-text').text("Don't have an account yet?");
        $('#switch-button').text("Create new account");
        $('#editor-wrapper').css('display', '');

        type = TYPE_LOG_IN;
    }
}

// Sends the login/signup request to the remote script.
function formSubmitHandler(event) {
    event.preventDefault();
    $.ajax({
        url: '/Vuvuzela/services/access/login.php',
        type: 'POST',
        data: {
            'username': $('#username').val(),
            'password': $('#password').val(),
            'editor': $('#editor').prop('checked'),
            'type': type
        },
        success: (ret) => {
            let message;
            switch(ret) {
                case MSG_OK:
                    window.location.replace("/Vuvuzela/pages/home.php");
                    return;
                case MSG_WRONG_CREDENTIALS:
                    message = 'Invalid username or password';
                    break;
                case MSG_USER_ALREADY_EXISTS:
                    message = 'Username already exists';
                    break;
                case MSG_GENERIC_ERROR:
                    message = 'Access error';
                    break;
            }
            showError(message); // Error
        },
        error: (jqXHR, textStatus, errorThrown )  => {
            console.log("Error: " + errorThrown + " " + textStatus)
        },
        dataType: 'json'
    });
}

$(document).ready(() => {
    $('input').click(() => {$('#message').hide();});
    $('#switch-button').click(switchButtonClickHandler);
    $('form').submit(formSubmitHandler);
});