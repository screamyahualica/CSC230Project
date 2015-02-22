
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;


public class HelloWorld {

  private static String CLIENT_ID = "415878093442-f6j0a53vn2jkv7t8vnqg76ojs2952fn8.apps.googleusercontent.com";
  private static String CLIENT_SECRET = "qmcHeE2928Zn1WluFv7pR9zY";

  private static String REDIRECT_URI = "https://www.example.com/oauth2callback";
  
  public static void main(String[] args) throws IOException {
    HttpTransport httpTransport = new NetHttpTransport();
    JsonFactory jsonFactory = new JacksonFactory();
   
    //debug
    System.out.println("Entered the program");
    
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(DriveScopes.DRIVE))
        .setAccessType("online")
        .setApprovalPrompt("auto").build();
    
    String url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
    System.out.println("Please open the following URL in your browser then type the authorization code:");
    System.out.println("  " + url);
    BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    String code = br.readLine();
    
    //debug
    System.out.println("About to get Token: " + code);
    
    GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
    GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);
    
    //debug
    System.out.println("About to create API client");
    
    //Create a new authorized API client
    Drive service = new Drive.Builder(httpTransport, jsonFactory, credential).build();

    //Insert a file  
    File body = new File();
    body.setTitle("My document");
    body.setDescription("A test document");
    body.setMimeType("text/plain");
    
    //added this
    try
    {
    	//debug
    	System.out.println("About to open file");
    	
    	java.io.File fileContent = new java.io.File("document.txt");
    	FileContent mediaContent = new FileContent("text/plain", fileContent);

    	//debug
    	System.out.println("Opened file");
    	
    	File file = service.files().insert(body, mediaContent).execute();
    	System.out.println("File ID: " + file.getId());
    } catch (IOException e) {
  	  System.out.println("ERROR: Could not open the file " + e.getMessage());
    }
    
    System.out.println("I AM DONE!");
  }  
  
  

}

