package Presentation;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.concurrent.TimeUnit;


public class ClientController {
    //TODO change server name!!!
    private static final URI url= UriBuilder.fromUri("http://132.72.65.77:8080/soccerSystem_war_exploded/rest").build();


    public static String connectToServer(String appClassName, String funcName, String... parameters){
        String ans="";
        try {
            ClientConfig config = new DefaultClientConfig();
            Client client = Client.create(config);
            client.setReadTimeout(5000);
            client.setConnectTimeout(5000);
            WebResource service = client.resource(url);
            service=service.path(appClassName).path(funcName);
            for (String param:parameters) {
                service=service.path(param);
            }
            ans=service.accept(MediaType.TEXT_PLAIN).get(ClientResponse.class).getEntity(String.class);
            if(ans.contains("HTTP Status 404")){
                ans="ERROR";
                System.out.println("exp thrown");
                Platform.runLater(() -> {
                    Alert chooseFile = new Alert(Alert.AlertType.ERROR);
                    chooseFile.setContentText("Connection problem ! Please try to connect later");
                    chooseFile.show();
                });
            }
            else{
                System.out.println("Server answer :"+ans);
            }

        }catch (Exception e){
            ans="ERROR";
            System.out.println("exp thrown");
            Platform.runLater(() -> {
                Alert chooseFile = new Alert(Alert.AlertType.ERROR);
                chooseFile.setContentText("Connection problem ! Please try to connect later");
                chooseFile.show();
            });
        }
        return ans;
    }



}
