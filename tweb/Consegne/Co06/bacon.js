$(document).ready(() => {
    $('#button-all').click(() => {
        $.ajax({
            url: 'search.php',
            type: 'POST',
            data: {
                first_name: $('#input-all-first-name').val(),
                last_name: $('#input-all-last-name').val(),
                all: 'true'
            },
            success: loadData,
            error: (xhr, status, error) => {
                alert(error);
            },
            dataType: 'json'
        })
    });
});

function loadData(data) {
    $('#firstN').html($('#input-all-first-name').val());
    $('#lastN').html($('#input-all-last-name').val());
    let i = 1;
    data.forEach(movie => {
        $('#list').append(
            `<tr><td>${i++}</td><td>${movie['name']}</td><td>${movie['year']}</td></tr>`
        )
    });
}
