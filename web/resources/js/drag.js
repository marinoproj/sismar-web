

var dragMe = document.getElementById("drag_me"),
        dragOfX = 0,
        dragOfY = 0;

function dragStart(e) {
    dragOfX = e.pageX - dragMe.offsetLeft;
    dragOfY = e.pageY - dragMe.offsetTop;
    addEventListener("mousemove", dragMove);
    addEventListener("mouseup", dragEnd);
}

function dragMove(e) {
    dragMe.style.left = (e.pageX - dragOfX) + 'px';
    dragMe.style.top = (e.pageY - dragOfY) + 'px';
}

function dragEnd() {
    removeEventListener("mousemove", dragMove);
    removeEventListener("mouseup", dragEnd);
}

dragMe.addEventListener("mousedown", dragStart);