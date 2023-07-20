const socket = io();

// Carregar conteúdo do editor caso haja algo salvo no armazenamento
window.onload = function() {
    const savedContent = localStorage.getItem('editorContent');
    if (savedContent) {
        document.getElementById('editor').innerHTML = savedContent;
    }
};

// Salvar conteúdo do editor no armazenamento local sempre que houver uma alteração
document.getElementById('editor').addEventListener('input', function() {
    const content = this.innerHTML;
    localStorage.setItem('editorContent', content);
    socket.emit('editorUpdate', content);
});


// Atualizar o conteúdo do editor quando uma alteração for recebida do servidor
socket.on('updateFromServer', (content) => {
    document.getElementById('editor').innerHTML = content;
});





//EXEMPLO DE CODIGO NODE.JS:

const express = require('express');
const http = require('http');
const socketIO = require('socket.io');

const app = express();
const server = http.createServer(app);
const io = socketIO(server);

const port = 3000;

// Lista para armazenar o conteúdo do editor
let editorContent = '';

io.on('connection', (socket) => {
    // Enviar o conteúdo atual do editor para um novo cliente conectado
    socket.emit('initialContent', editorContent);

    // Receber atualizações do editor de um cliente e enviá-las para todos os outros clientes
    socket.on('editorUpdate', (content) => {
        editorContent = content;
        socket.broadcast.emit('updateFromServer', content);
    });
});

server.listen(port, () => {
    console.log(`Servidor está ouvindo na porta ${port}`);
});
