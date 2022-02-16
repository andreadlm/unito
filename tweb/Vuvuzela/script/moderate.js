let lastCommentID;

// Shows an error message inside the $parent container.
function showError($parent, msg) {
    let $self = $(`<div class="error-message"><p>${msg}</div>`).prependTo($parent);
    $self.append('</p><button type="button">X</button>').click(() => { $self.remove() });
}

// Comment class. Each comment has an associated config object.
class Comment {
    constructor(config) {
        this.config = config;
    }

    // Creates the comment inside the $parent container.
    create($parent) {
        let $self =
            $(`<div id="comment-${this.config['id']}" class="comment comment-highlighted">` +
              `<div class="comment-header">` +
                  `<span class="comment-author">${this.config['author']}</span>` +
                  `<span class="comment-date">${this.config['date']}</span>` +
              `</div>` +
              `<div class="comment-text">${this.config['text']}</div>` +
              `<div class="comment-moderate-buttons"></div>` +
          `</div>`).appendTo($('#comments-list'));

        $('<button class="button-fill" type="button">✅ Accept</button>')
            .appendTo($self.children('.comment-moderate-buttons'))
            .click(() => { this.executeAction('accept'); });

        $('<button class="button-fill" type="button">❌ Reject</button>')
            .appendTo($self.children('.comment-moderate-buttons'))
            .click(() => { this.executeAction('reject'); });
    }

    // Deletes the comment from the DOM.
    delete() {
        $(`#comment-${this.config['id']}`).remove();
    }

    // Execute action of a certain type (accept/delete) by calling the remote service.
    executeAction(type) {
        $.ajax({
            url: '/Vuvuzela/services/comment/set_comment_moderate.php',
            type: 'POST',
            data: {
                comment_id: this.config['id'],
                type: type
            },
            success: (msg) => {
                if (msg['status'] === 'error') {
                    showError($('#comments-list'), 'Error executing action');
                    console.log(msg['errorMsg']);
                } else if (msg['status'] === 'success') this.delete(); // Comment moderated successfully, removed from list
            },
            error: (jqXHR, textStatus, errorThrow)  => {
                showError($('#comments-list'),'Error executing action');
                console.log(jqXHR.responseText + "\n" + textStatus + "\n" + errorThrow)
            },
            dataType: 'json'
        });
    }
}

// Call to the remote service to load the comments to be moderated.
// Only not already moderated comments are shown.
function loadComments() {
    $.ajax({
        url: '/Vuvuzela/services/comment/get_comments_moderate.php',
        type: 'GET',
        data: {
            num: 10,
            first: lastCommentID === undefined ? -1 : lastCommentID,
        },
        success: (msg) => {
            if(msg['status'] === 'error') {
                showError($('#comments-list'),'Error downloading comments');
                console.log(msg['errorMsg']);
            } else if(msg['status'] === 'success') {
                let commentsData = msg['data'];

                if(commentsData.length < 10) {
                    $('#load-more-button').hide();
                    $('#list-end').show();
                }

                if(commentsData.length > 0) {
                    commentsData.forEach(data => new Comment(data).create($('#comments-list')));
                    lastCommentID = commentsData[commentsData.length - 1]['id'];
                }
            }
        },
        error: (jqXHR, textStatus, errorThrow)  => {
            showError($('#comments-list'),'Error downloading comments');
            console.log(jqXHR.responseText + "\n" + textStatus + "\n" + errorThrow)
        },
        dataType: 'json'
    });
}

$(document).ready(() => {
    loadComments();
    $('#load-more-button').click(() => loadComments());
    $('#back-button').click(() => window.location.replace('/Vuvuzela/pages/home.php'));
});