// Starting blank position
let currBX = 4;
let currBY = 4;

let gameStarted = false;

function move(cell) {
    removeListeners();

    $('.' + pos(cell))
        .removeClass(pos(cell))
        .addClass(pos({x: currBX, y: currBY}));

    currBX = cell.x;
    currBY = cell.y;

    setListeners();
}

function checkWin() {
    let tiles = $('#puzzlearea').children();
    let cont = true;
    for(let i = 0; cont && i < 4; i++)
        for(let j = 0; cont && j < 4; j++)
            if($(tiles[i * 4 + j]).attr('class') !== undefined &&
               $(tiles[i * 4 + j]).attr('class') !== pos({x: j + 1, y: i + 1}))
                cont = false;
    if(cont) alert("vittoria!");
}

function setListeners() {
    let neighborhoods = getBlankNeighborhoodClassList();
    neighborhoods.forEach((neighborhood) => {
        $('.' + pos(neighborhood)).click(() => {
            move(neighborhood);
            checkWin();
            });
    });
}

function removeListeners() {
    neighborhoods = getBlankNeighborhoodClassList();
    neighborhoods.forEach((neighborhood) => {
        $('.' + pos(neighborhood)).off();
    })

}

function pos(cell) {
    return `pos_${cell.y}_${cell.x}`;
}

function getBlankNeighborhoodClassList() {
    let list = [];

    if(currBX !== 1) list.push({x: currBX - 1, y: currBY});     // Left
    if(currBY !== 1) list.push({x: currBX,     y: currBY - 1}); // Top
    if(currBX !== 4) list.push({x: currBX + 1, y: currBY});     // Right
    if(currBY !== 4) list.push({x: currBX,     y: currBY + 1}); // Bottom

    return list;
}

function shuffle() {
    for(let i = 0; i < 50; i++) {
        let neighborhoods = getBlankNeighborhoodClassList();
        move(neighborhoods[Math.floor(Math.random() * neighborhoods.length)]);
    }
}

$(document).ready(() => {
    $("#shufflebutton").on("click", shuffle);
    setListeners();
});