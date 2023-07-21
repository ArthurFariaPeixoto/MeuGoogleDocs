import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;



@ServerEndpoint("/editor")
public class TextEditorServer {
    private static final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    @OnOpen
    public void onOpen(Session session) {
        sessions.add(session);
        broadcast(getCurrentContent()); // Envia o conteúdo atual do editor para o novo cliente
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        updateContent(message);
        broadcast(message); // Envia as alterações para todos os clientes conectados
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session);
    }

    private void broadcast(String message) {
        for (Session session : sessions) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getCurrentContent() {
        // Aqui você pode implementar a lógica para obter o conteúdo atual do editor de alguma fonte (por exemplo, banco de dados).
        // Por simplicidade, usaremos uma variável estática para armazenar o conteúdo atual neste exemplo.
        return EditorContentStore.getCurrentContent();
    }

    private void updateContent(String content) {
        // Aqui você pode implementar a lógica para atualizar o conteúdo do editor em alguma fonte (por exemplo, banco de dados).
        // Por simplicidade, usaremos uma variável estática para armazenar o conteúdo atual neste exemplo.
        EditorContentStore.updateContent(content);
    }

//    public void startServer() {
//        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
//        String serverUri = "ws://localhost:8080/editor";
//        try {
//            container.connectToServer(this, new URI(serverUri));
//            System.out.println("Servidor WebSocket iniciado em " + serverUri);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
