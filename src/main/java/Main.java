import javax.websocket.ContainerProvider;
import javax.websocket.WebSocketContainer;
import java.net.URI;

public class Main {
    public static void main(String[] args) {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        String serverUri = "ws://localhost:63342/editor";

        try {
            container.connectToServer(TextEditorServer.class, new URI(serverUri));
            System.out.println("Servidor WebSocket iniciado em " + serverUri);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
