
    //Função para pegar o "index" da posição do cursor no texto
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


    // Carregar conteúdo do editor caso haja algo salvo no armazenamento
    window.onload = function() {
        const savedContent = localStorage.getItem('editorContent');
        if (savedContent) {
            document.getElementById('editor').innerHTML = savedContent;
        }
    };

    // Salva conteúdo do editor no armazenamento local sempre que houver uma alteração
    document.getElementById('editor').addEventListener('input', function() {
        const content = this.innerHTML;
        localStorage.setItem('editorContent', content);
    });


    // O QUE PRECISARIA SER FEITO NESSE CASO SERIA O BACKEND ARMAZENAR A VERSÃO ATUALIZADA DO TEXTO NO LOCALSTORAGE E O FRONTEND MANDAR A VERSÃO ATUAL PARA O BACKEND FAZER UM MERGE

    //uma possibilidade seria usar o node.js no backend juntamente com a biblioteca socket.io (deve facilitar um pouco)
    