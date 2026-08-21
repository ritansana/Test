package oms10;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import com.sterlingcommerce.baseutil.SCXmlUtil;
import com.yantra.interop.japi.YIFApi;
import com.yantra.interop.japi.YIFClientFactory;
import com.yantra.yfs.japi.YFSEnvironment;
import com.yantra.yfs.japi.YFSUserExitException;
import com.yantra.yfs.japi.ue.YFSBeforeCreateOrderUE;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MTSBeforeCreateOrderUE implements YFSBeforeCreateOrderUE {

    @Override
    public String beforeCreateOrder(YFSEnvironment env, String input)
            throws YFSUserExitException {
        return input;
    }

    @Override
    public Document beforeCreateOrder(YFSEnvironment env, Document inputXml)
            throws YFSUserExitException {

        System.out.println("Ritan Input XML:");
        System.out.println(SCXmlUtil.getString(inputXml));
         
        Element order = inputXml.getDocumentElement();
       
        // Validate Order attributes
        validateAttribute(order, "DocumentType");
        validateAttribute(order, "EnterpriseCode");
        validateAttribute(order, "OrderNo");

        // Validate OrderLine
        NodeList orderLines = inputXml.getElementsByTagName("OrderLine");
        

        if (orderLines.getLength() == 0) {
            throw new YFSUserExitException("OrderLine element is missing.");
        }

        Element orderLine = (Element) orderLines.item(0);

        validateAttribute(orderLine, "OrderedQty");
        validateAttribute(orderLine, "PrimeLineNo");
        validateAttribute(orderLine, "SubLineNo");
        validateAttribute(orderLine, "DeliveryMethod");

        // Validate Item
        NodeList items = inputXml.getElementsByTagName("Item");

        if (items.getLength() == 0) {
            throw new YFSUserExitException("Item element is missing.");
        }

        Element item = (Element) items.item(0);
       

        validateAttribute(item, "ItemID");
        validateAttribute(item, "ProductClass");
        validateAttribute(item, "UnitOfMeasure");
        
        
        
      //validate CustomerPOno
                 
        String customerPONo = order.getAttribute("CustomerPONo");
        if (customerPONo == null || customerPONo.trim().isEmpty()) {
            throw new YFSUserExitException("CustomerPONo attribute is missing.");
        }
                
        String itemID = item.getAttribute("ItemID");
        
               
        Document getItemDetailsInput = SCXmlUtil.createDocument("Item");
        Element root = getItemDetailsInput.getDocumentElement();
        
             
        Document getOrderListInput = SCXmlUtil.createDocument("Order");
        Element orderListRoot = getOrderListInput.getDocumentElement();
               
        // getOrderList input
        orderListRoot.setAttribute("DocumentType", order.getAttribute("DocumentType"));
        orderListRoot.setAttribute("EnterpriseCode", order.getAttribute("EnterpriseCode"));
        orderListRoot.setAttribute("CustomerPONo", customerPONo);
        
        root.setAttribute("ItemID", item.getAttribute("ItemID"));
        root.setAttribute("OrganizationCode", order.getAttribute("EnterpriseCode"));
        root.setAttribute("ProductClass", item.getAttribute("ProductClass"));
        root.setAttribute("UnitOfMeasure", item.getAttribute("UnitOfMeasure"));
          
       
        
        System.out.println("ItemID = " + itemID);
        System.out.println("OrganizationCode = " + order.getAttribute("EnterpriseCode"));
        System.out.println(SCXmlUtil.getString(getItemDetailsInput));
        
        System.out.println("getOrderList Input:");
        System.out.println(SCXmlUtil.getString(getOrderListInput));
      
        
        
        try {

            YIFApi api = YIFClientFactory.getInstance().getApi();

            String requestXml = SCXmlUtil.getString(getItemDetailsInput);

            URL url =   new URL("http://localhost:8080/orders/getItem");

            HttpURLConnection connection = (HttpURLConnection)url.openConnection();

            connection.setRequestMethod("POST");

            connection.setRequestProperty( "Content-Type", "application/xml");

            connection.setDoOutput(true);

            OutputStream os =  connection.getOutputStream();

            os.write(requestXml.getBytes("UTF-8"));

            os.close();

            BufferedReader br = new BufferedReader( new InputStreamReader(connection.getInputStream()));

            StringBuilder sb =  new StringBuilder();

            String line;
            
            while ((line = br.readLine()) != null) {

                sb.append(line);
            }

            br.close();

            String responseXml = sb.toString();

            System.out.println("Spring Boot Response: " + responseXml);


            if (responseXml.contains("ITEM_NOT_FOUND")) {

                throw new YFSUserExitException("Item does not exist: " + itemID);
            }
            
            //Create the XML for the address validation
            Document validateAddressInput = SCXmlUtil.createDocument("Order");

            Element addressRoot = validateAddressInput.getDocumentElement();

            Element shipTo = (Element) inputXml.getElementsByTagName("PersonInfoShipTo").item(0);

            Element newShipTo = validateAddressInput.createElement("PersonInfoShipTo");

            newShipTo.setAttribute("AddressLine1",shipTo.getAttribute("AddressLine1"));

            newShipTo.setAttribute("AddressLine2", shipTo.getAttribute("AddressLine2"));

            newShipTo.setAttribute("City",shipTo.getAttribute("City"));

            newShipTo.setAttribute("State",shipTo.getAttribute("State"));

            newShipTo.setAttribute("ZipCode", shipTo.getAttribute("ZipCode"));

            newShipTo.setAttribute("Country",shipTo.getAttribute("Country"));

            addressRoot.appendChild(newShipTo);

            System.out.println(SCXmlUtil.getString(validateAddressInput));
            
            //Call the new Spring Boot API
            String addressRequestXml = SCXmlUtil.getString(validateAddressInput);

            URL addressUrl = new URL("http://localhost:8080/orders/validateAddress");

            HttpURLConnection addressConnection = (HttpURLConnection) addressUrl.openConnection();

            addressConnection.setRequestMethod("POST");

            addressConnection.setRequestProperty("Content-Type", "application/xml");

            addressConnection.setDoOutput(true);

            OutputStream addressOs = addressConnection.getOutputStream();

            addressOs.write(addressRequestXml.getBytes("UTF-8"));

            addressOs.close();
            
            //read the response
            
            BufferedReader addressBr = new BufferedReader(new InputStreamReader(addressConnection.getInputStream()));

            StringBuilder addressSb = new StringBuilder();

            String addressLine;

            while ((addressLine = addressBr.readLine()) != null) {

                addressSb.append(addressLine);

            }

            addressBr.close();

            String addressResponse = addressSb.toString();

            System.out.println(addressResponse);
            
            
            //check the response
            if (addressResponse.contains("INVALID_ADDRESS")) {

                throw new YFSUserExitException("Invalid Shipping Address");
            }
            
            
            
            
            Document getOrderListOutputresponse = api.invoke(env,"getOrderList",getOrderListInput);

            System.out.println("getOrderList Response:");
            System.out.println( SCXmlUtil.getString(getOrderListOutputresponse));

            NodeList orderNodes =  getOrderListOutputresponse.getElementsByTagName("Order");

            if (orderNodes.getLength() > 0) {

                throw new YFSUserExitException("Customer PO Number already exists: "+ customerPONo);
            }

            System.out.println("Customer PO Number does not exist. Proceeding with order creation.");

            return inputXml;

        } catch (Exception e) {

            e.printStackTrace();

            throw new YFSUserExitException(
                    e.getMessage()
            );
        }
    }
    

    private void validateAttribute(Element element, String attributeName)
            throws YFSUserExitException {

        if (!element.hasAttribute(attributeName)) {
            throw new YFSUserExitException("Missing mandatory attribute: " + attributeName);
        }

        String value = element.getAttribute(attributeName).trim();

        if (value.isEmpty()) {
            throw new YFSUserExitException("Attribute '" + attributeName + "' cannot be empty.");
        }
      		
    }
    }
