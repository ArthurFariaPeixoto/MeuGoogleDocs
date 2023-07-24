import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@ServerEndpoint("/document")
public class Servidor {
    private static Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());
    private static String documentContent = ""; // doc compartilhado
    private static Lock documentLock = new ReentrantLock(); // lock para controle de concorrência

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        // quando um novo cliente se conecta, envie o documento atualizado para ele
        sendDocumentToClient(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // processa a mensagem recebida do cliente
        String[] parts = message.split(";", 2); // mensagem no formato "posicao;texto"
        int position = Integer.parseInt(parts[0]);
        String text = parts[1];

        documentLock.lock(); // locka o documento compartilhado
        try {
            // atualiza o documento compartilhado
            StringBuilder updatedDocument = new StringBuilder(documentContent);
            updatedDocument.insert(position, text);
            documentContent = updatedDocument.toString();
        } finally {
            documentLock.unlock(); // deslocka o documento compartilhado
        }

        // manda o documento atualizado pra todos os clientes conectados
        broadcastDocument();
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        sessions.remove(session);
    }

    // metodo pra madnar o documento atualizado pra todos os clientes
    private void broadcastDocument() {
        synchronized (Servidor.class) {
            for (Session session : sessions) {
                try {
                    session.getBasicRemote().sendText(documentContent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // metodo pra mandar o documento atualizado pra um cliente específico
    private void sendDocumentToClient(Session session) {
        synchronized (Servidor.class) {
            try {
                session.getBasicRemote().sendText(documentContent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
