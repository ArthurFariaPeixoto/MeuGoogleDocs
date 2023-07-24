import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;
public class Main {
    public static void main(String[] args) {
        // starta o websocket com o tomcat
        org.apache.tomcat.websocket.server.WsServerContainer container =
                (org.apache.tomcat.websocket.server.WsServerContainer) ServerContainerProvider.getServerContainer();
        try {
            container.addEndpoint(ServerEndpointConfig.Builder.create(Servidor.class, "/document").build());
        } catch (DeploymentException e) {
            e.printStackTrace();
        }

        // starta o tomcat
        org.apache.catalina.startup.Tomcat tomcat = new org.apache.catalina.startup.Tomcat();
        tomcat.setPort(8080);
        tomcat.getConnector();

        // põe no tomcat
        tomcat.addServlet("WebSocketServlet", new org.apache.tomcat.websocket.server.WsWebSocketServlet());
        tomcat.addServletMappingDecoded("/document/*", "WebSocketServlet");

        try {
            tomcat.start();
            tomcat.getServer().await();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ServerContainerProvider {
    public static ServerContainer getServerContainer() {
        // método pra evitar conflito de dependênciar
        try {
            Class<?> serverContainerClass = Class.forName("org.apache.tomcat.websocket.server.WsServerContainer");
            return (ServerContainer) serverContainerClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}