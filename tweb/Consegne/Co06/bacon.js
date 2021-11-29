$(document).ready(() => {
    $('#button-all').click(() => ajaxCall(
        {
            first_name: $('#input-all-first-name').val(),
            last_name: $('#input-all-last-name').val(),
            all: 'true'
        })
    );

    $('#button-with').click(
        () => ajaxCall(
        {
            first_name: $('#input-with-first-name').val(),
            last_name: $('#input-with-last-name').val(),
            all: 'false'
        })
    );
});

function ajaxCall(data) {
    $.ajax({
        url: 'search.php',
        type: 'POST',
        data: data,
        success: (ret) => loadData(ret, data.first_name, data.last_name),
        error: (xhr, status, error) => {
            alert(error);
        },
        dataType: 'json'
    });
}

function loadData(data, firstName, lastName) {
    let errHTML = '';
    let resHTML = '';

    if(data.hasOwnProperty('error')) {
        errHTML = `<div>${data['error']}<\div>`;
    } else {
        resHTML = `<h2>Results for ${firstName} ${lastName}</h2>
                   <table id="list">
                       <caption> All Films </caption>
                           <tr class="evenRow">
                               <th>#</th>
                               <th>Name</th>
                               <th>Year</th>
                           </tr>`

        let i = 1;
        data.forEach(movie => {
            resHTML += `<tr ${i % 2 === 0 ? 'class="evenRow"' : 'class="oddRow"'}>
                        <td>${i++}</td>
                        <td>${movie['name']}</td>
                        <td>${movie['year']}</td>
                    </tr>`;
        });

        resHTML += '</table>'
    }

    $('#errMsg').html(errHTML);
    $('#results').html(resHTML);
}
