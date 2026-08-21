package newConnect;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import java.net.URL;

@Service
public class SterlingService {

    private static final String URL = "jdbc:postgresql://localhost:5432/Order3";

    private static final String USERNAME = "postgres";

    private static final String PASSWORD = "password";
    
    //private static final String GOOGLE_API_KEY = "YOUR_GOOGLE_API_KEY";

    public String getItem(String xml) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
        	
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(new InputSource(new StringReader(xml)));

            Element item = (Element) doc.getElementsByTagName("Item").item(0);

            String itemId = item.getAttribute("ItemID");

            System.out.println("Checking ItemID = " + itemId);

            //postgress connect
            Class.forName("org.postgresql.Driver");

            con = DriverManager.getConnection(URL,USERNAME,PASSWORD);

            //check itemMaster table
            ps = con.prepareStatement("SELECT * " + "FROM ItemMaster " + "WHERE item_id = ?");

            ps.setString(1, itemId);

            rs = ps.executeQuery();

            // if item exists
            if (rs.next()) {

                return

                    "<Item " +
                    "ItemID=\"" + itemId + "\" " +
                    "Status=\"FOUND\"/>";
            }

            // check if item not found
            return

                "<Errors>" +
                    "<Error " +
                    "ErrorCode=\"ITEM_NOT_FOUND\" " +
                    "ErrorDescription=\"Item "
                    + itemId +
                    " does not exist\"/>" +
                "</Errors>";

        } catch (Exception e) {

            e.printStackTrace();

            return

                "<Errors>" +
                    "<Error " +
                    "ErrorCode=\"SYSTEM_ERROR\" " +
                    "ErrorDescription=\"" +
                    e.getMessage() +
                    "\"/>" +
                "</Errors>";

        } finally {

            try {

                if (rs != null) {
                    rs.close();
                }

                if (ps != null) {
                    ps.close();
                }

                if (con != null) {
                    con.close();
                }

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }
    public String validateAddress(String xml) {

        try {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

            DocumentBuilder builder = factory.newDocumentBuilder();

            Document doc = builder.parse(new InputSource(new StringReader(xml)));

            Element shipTo = (Element) doc.getElementsByTagName("PersonInfoShipTo").item(0);

            String address1 = shipTo.getAttribute("AddressLine1");
            
            String address2 = shipTo.getAttribute("AddressLine2");

            String city = shipTo.getAttribute("City");

            String state = shipTo.getAttribute("State");

            String zip = shipTo.getAttribute("ZipCode");

            String country = shipTo.getAttribute("Country");

            System.out.println("Address1 = " + address1);
            System.out.println("Address2 = " + address2);
            System.out.println("City = " + city);
            System.out.println("State = " + state);
            System.out.println("Zip = " + zip);
            System.out.println("Country = " + country);
            
            String fullAddress =
                    address1 + ", " +
                    address2 + ", " +
                    city + ", " +
                    state + ", " +
                    zip + ", " +
                    country;

            System.out.println("Full Address = " + fullAddress);

//             Google api will go here
//
//           String encodedAddress = URLEncoder.encode(fullAddress, "UTF-8");
//
//           String url = "https://maps.googleapis.com/maps/api/geocode/json?address="
//                   	+ encodedAddress
//                    	+ "&key="
//                  	+ GOOGLE_API_KEY;
            
            String encodedAddress = URLEncoder.encode(fullAddress, "UTF-8");

            String url ="https://nominatim.openstreetmap.org/search?q="
            + encodedAddress
            + "&format=json";

            System.out.println(url);
            
            //URL googleUrl = new URL(url);
            
            URL osmUrl = new URL(url);

           // HttpURLConnection connection = (HttpURLConnection) googleUrl.openConnection();
            
            HttpURLConnection connection = (HttpURLConnection) osmUrl.openConnection();

            connection.setRequestMethod("GET");
            
            connection.setRequestProperty( "User-Agent", "IBM-Sterling-OMS");
            
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            StringBuilder response = new StringBuilder();

            String line;

            while ((line = br.readLine()) != null) {

                response.append(line);

            }

            br.close();

            //String googleResponse = response.toString();
            
            String osmResponse = response.toString();

//          System.out.println("Google Response:");
//          System.out.println(googleResponse);
            
            System.out.println("OpenStreetMap Response:");
            System.out.println(osmResponse);
            
//          if (googleResponse.contains("\"status\" : \"OK\"") || googleResponse.contains("\"status\":\"OK\"")) {
//
//          return "<Validation Status=\"SUCCESS\"/>";
//
//            }
            if (!osmResponse.trim().equals("[]")) {

                return "<Validation Status=\"SUCCESS\"/>";

            }

            return "<Errors>"
                    + "<Error ErrorCode=\"INVALID_ADDRESS\" "
                    + "ErrorDescription=\"Address not found\"/>"
                    + "</Errors>";

        } catch(Exception e){

            e.printStackTrace();

            return "<Errors><Error ErrorCode=\"ADDRESS_ERROR\"/></Errors>";

        }

    }
}