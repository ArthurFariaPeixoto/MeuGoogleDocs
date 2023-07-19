const editor = document.querySelector("#editor")


document.addEventListener("DOMContentLoaded", function() {
    var editorDiv = document.getElementById("editor");

    editorDiv.addEventListener("input", function() {
        var cursorPosition = getCursorPosition();
        console.log("Posição do cursor: " + cursorPosition);
    });

    function getCursorPosition() {
        var selection = window.getSelection();
        var range = selection.getRangeAt(0);
        var preSelectionRange = range.cloneRange();
        preSelectionRange.selectNodeContents(editorDiv);
        preSelectionRange.setEnd(range.startContainer, range.startOffset);
        var cursorPosition = preSelectionRange.toString().length;
        return cursorPosition;
    }
});