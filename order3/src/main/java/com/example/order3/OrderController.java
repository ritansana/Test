package com.example.order3;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

	
	@RestController
	public class OrderController {

	    private static final String DB_URL = "jdbc:postgresql://localhost:5432/Order3";

	    private static final String USER = "postgres";

	    private static final String PASS = "password";
	    
	    
	    
	    @PostMapping(value="/test", consumes="application/xml", produces="application/xml")
	    	public Order test(@RequestBody Order order) {
	    	    return order;
	    	}
	    
	    @PostMapping(value="/create",consumes="application/xml",produces="application/xml")
	    public String create(@RequestBody String xml) {
	    	
	        @PostMapping(
	                value = "/getItem",
	                consumes = "application/xml",
	                produces = "application/xml"
	        )
	        public String getItem(@RequestBody String xml)
	       
	        // USER EXIT VALIDATION
	       
	    	  String result = userExit(xml);

	    	    if (result.startsWith("<Errors>")) {
	    	        return result;
	    	    }
	    	    xml = result;
	    	    	    	    
	        try {
	        		        	
	        	XmlMapper mapper = new XmlMapper();
	        	Order order = mapper.readValue(xml, Order.class);

	            Class.forName("org.postgresql.Driver");

	            Connection con = DriverManager.getConnection(DB_URL,USER,PASS);

	            Statement stmt = con.createStatement();

	     
	            // INSERT ORDER
	    

	            String orderSql = "INSERT INTO Orders(document_type,enterprise_code) VALUES('"
	                    + order.getDocumentType() + "','"
	                    + order.getEnterpriseCode() + "')";

	            stmt.executeUpdate(orderSql);

	            ResultSet rsOrder = stmt.executeQuery("SELECT MAX(order_id) FROM Orders");

	            rsOrder.next();

	            int orderId = rsOrder.getInt(1);

	         
	            // INSERT EXTN
	      

	            if(order.getExtn() != null) {

	                Extn extn = order.getExtn();

	                String extnSql = "INSERT INTO Extn("
	                        + "order_id,"
	                        + "wis_order_amount,"
	                        + "affiliate_id)"
	                        + " VALUES("
	                        + orderId + ",'"
	                        + extn.getExtnWISOrderAmount()
	                        + "','"
	                        + extn.getExtnAffiliateID()
	                        + "')";

	                stmt.executeUpdate(extnSql);
	            }

	         
	            // INSERT SHIP TO
	        

	            if(order.getPersonInfoShipTo() != null) {

	                String shipSql = "INSERT INTO PersonInfoShipTo("
	                        + "order_id,country)"
	                        + " VALUES("
	                        + orderId + ",'"
	                        + order.getPersonInfoShipTo()
	                                .getCountry()
	                        + "')";

	                stmt.executeUpdate(shipSql);
	            }

	          
	            // INSERT BILL TO
	          

	            if(order.getPersonInfoBillTo() != null) {

	                String billSql = "INSERT INTO PersonInfoBillTo("
	                        + "order_id,country)"
	                        + " VALUES("
	                        + orderId + ",'"
	                        + order.getPersonInfoBillTo()
	                                .getCountry()
	                        + "')";

	                stmt.executeUpdate(billSql);
	            }

	            if(order.getPaymentMethods() != null &&
	            order.getPaymentMethods().getPaymentMethod() != null){

	               for(PaymentMethod pm : order.getPaymentMethods().getPaymentMethod()){

	            	   String paymentSql = "INSERT INTO PaymentMethods(" +
	            		  "order_id," +
	            		  "charge_sequence," +
	            		  "credit_card_exp_date," +
	            		  "credit_card_name," +
	            		  "credit_card_no," +
	            		  "credit_card_type," +
	            		  "display_credit_card_no," +
	            		  "payment_type," +
	            		  "svc_no," +
	            		  "unlimited_charges)" +
	            		  " VALUES(" +
	            		  orderId + ",'" +
	            		  pm.getChargeSequence() + "','" +
	            		  pm.getCreditCardExpDate() + "','" +
	            		  pm.getCreditCardName() + "','" +
	            		  pm.getCreditCardNo() + "','" +
	            		  pm.getCreditCardType() + "','" +
	            		  pm.getDisplayCreditCardNo() + "','" +
	            		  pm.getPaymentType() + "','" +
	            		  pm.getSvcNo() + "','" +
	            		  pm.getUnlimitedCharges() + "')";

	            		  stmt.executeUpdate(paymentSql);
	               }
	            }
	            		    
	            		    
	            if(order.getOrderLines()!=null &&
	            order.getOrderLines().getOrderLine()!=null){

	            	for(OrderLine line : order.getOrderLines().getOrderLine()){

	            	  String lineSql ="INSERT INTO OrderLines(" +
	            		    		   "order_id," +
	            		    		   "ordered_qty," +
	            		    		   "prime_line_no," +
	            		    		   "sub_line_no," +
	            		    		   "delivery_method," +
	            		    		   "ship_node)" +
	            		    		   " VALUES(" +
	            		    		   orderId + "," +
	            		    		   line.getOrderedQty() + "," +
	            		    		   line.getPrimeLineNo() + "," +
	            		    		   line.getSubLineNo() + ",'" +
	            		    		   line.getDeliveryMethod() + "','" +
	            		    		   line.getShipNode() + "')";

	            		    		   stmt.executeUpdate(lineSql);

	            ResultSet rsLine =  stmt.executeQuery("SELECT MAX(line_id) FROM OrderLines");

	            rsLine.next();

	            int lineId = rsLine.getInt(1);
	            Item item = line.getItem();

	            String itemSql = "INSERT INTO Items(" +
	            	             "line_id," +
	            	             "item_id," +
	            	             "product_class," +
	            	             "unit_of_measure)" +
	            	             " VALUES(" +
	            	              lineId + ",'" +
	            	              item.getItemID() + "','" +
	            	              item.getProductClass() + "','" +
	            	              item.getUnitOfMeasure() + "')";

	             stmt.executeUpdate(itemSql);
	             LinePriceInfo price = line.getLinePriceInfo();

	             String priceSql = "INSERT INTO LinePriceInfo(" +
	            	               "line_id," +
	            	               "unit_price)" +
	            	               " VALUES(" +
	            	               lineId + "," +
	            	               price.getUnitPrice() + ")";

	             stmt.executeUpdate(priceSql);

	           }
	         }
	                 		            

	            con.close();

	            return "<Message>Order Created Successfully</Message>";

	        }
	            
	        
	        catch(Exception e) {

	            e.printStackTrace();

	            return "<Errors>"
	                    + "<Error ErrorCode=\"YFS99999\" "
	                    + "ErrorDescription=\""
	                    + e.getMessage()
	                    + "\"/>"
	                    + "</Errors>";
	        }
	    }
	    
	    private String userExit(String xml) {

	        Connection con = null;

	        try {

	            // Parse XML
	            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	            DocumentBuilder builder = factory.newDocumentBuilder();

	            InputSource is = new InputSource(new StringReader(xml));

	            Document doc = builder.parse(is);

	            // CHECK EMPTY ELEMENTS

	            NodeList nodes = doc.getElementsByTagName("*");

	            for (int i = 0; i < nodes.getLength(); i++) {

	                Element element = (Element) nodes.item(i);

	                boolean noAttributes = element.getAttributes().getLength() == 0;
	                boolean noChildren = element.getChildNodes().getLength() == 0;
	                boolean emptyText = element.getTextContent().trim().isEmpty();

	                if (noAttributes && noChildren && emptyText) {

	                    return "<Errors>"
	                            + "<Error ErrorCode=\"YFS10004\" "
	                            + "ErrorDescription=\""
	                            + element.getNodeName()
	                            + " cannot be empty\"/>"
	                            + "</Errors>";
	                }
	            }

	            // CHECK BLANK ATTRIBUTES

	            for (int i = 0; i < nodes.getLength(); i++) {

	                Element element = (Element) nodes.item(i);

	                NamedNodeMap attrs = element.getAttributes();

	                for (int j = 0; j < attrs.getLength(); j++) {

	                    Node attr = attrs.item(j);

	                    if (attr.getNodeValue() == null
	                            || attr.getNodeValue().trim().isEmpty()) {

	                        return "<Errors>"
	                                + "<Error ErrorCode=\"YFS10004\" "
	                                + "ErrorDescription=\""
	                                + attr.getNodeName()
	                                + " cannot be blank\"/>"
	                                + "</Errors>";
	                    }
	                }
	            }

	            // CHECK WHETHER ITEM EXISTS

	            Class.forName("org.postgresql.Driver");

	            con = DriverManager.getConnection(DB_URL, USER, PASS);

	            NodeList itemNodes = doc.getElementsByTagName("Item");

	            StringBuilder message = new StringBuilder();

	            for (int i = 0; i < itemNodes.getLength(); i++) {

	                Element itemElement = (Element) itemNodes.item(i);

	                String itemId = itemElement.getAttribute("ItemID");

	                PreparedStatement ps = con.prepareStatement("SELECT unit_price FROM ItemMaster WHERE item_id=?");

	                ps.setString(1, itemId);

	                ResultSet rs = ps.executeQuery();

	                if (!rs.next()) {

	                    rs.close();
	                    ps.close();
	                    con.close();

	                    return "<Errors>"
	                            + "<Error ErrorCode=\"YFS10010\" "
	                            + "ErrorDescription=\"Item "
	                            + itemId
	                            + " does not exist\"/>"
	                            + "</Errors>";
	                }

	                double dbPrice = rs.getDouble("unit_price");

	                Node parent = itemElement.getParentNode();
	                NodeList children = parent.getChildNodes();
	                
	                for (int j = 0; j < children.getLength(); j++) {

	                    Node child = children.item(j);

	                    if (child.getNodeType() == Node.ELEMENT_NODE &&
	                        child.getNodeName().equals("LinePriceInfo")) {

	                        Element priceElement = (Element) child;

	                        priceElement.setAttribute(
	                                "UnitPrice",
	                                String.valueOf(dbPrice));

	                        break;
	                    }
	                }

	                rs.close();
	                ps.close();
	            }

	            con.close();

	            TransformerFactory tf = TransformerFactory.newInstance();

	            Transformer transformer = tf.newTransformer();

	            StringWriter writer = new StringWriter();

	            transformer.transform(new DOMSource(doc), new StreamResult(writer));

	            return writer.toString();

	        }
	        catch (Exception e) {

	            try {
	                if (con != null) {
	                    con.close();
	                }
	            }
	            catch (Exception ex) {
	                ex.printStackTrace();
	            }

	            return "<Errors>"
	                    + "<Error ErrorCode=\"YFS99999\" "
	                    + "ErrorDescription=\""
	                    + e.getMessage()
	                    + "\"/>"
	                    + "</Errors>";
	        }
	    }
	    
	    
	    	    
	    @PostMapping(value = "/readone",consumes = "application/xml", produces = "application/xml")
	    public Order readOne(@RequestBody OrderRequest request) {

	        Order order = new Order();
	        int id = request.getOrderId();

	        try {

	            Class.forName("org.postgresql.Driver");

	            Connection con =
	                    DriverManager.getConnection(
	                            DB_URL,
	                            USER,
	                            PASS);

	          
	            // ORDER
	        

	            Statement stmtOrder = con.createStatement();

	            ResultSet rsOrder = stmtOrder.executeQuery( "SELECT * FROM Orders WHERE order_id="  + id);

	            if(rsOrder.next()) {

	                order.setDocumentType(rsOrder.getString("document_type"));

	                order.setEnterpriseCode(rsOrder.getString("enterprise_code"));
	            }

	    
	            // EXTN
	      

	            Statement stmtExtn = con.createStatement();

	            ResultSet rsExtn = stmtExtn.executeQuery("SELECT * FROM Extn WHERE order_id=" + id);

	            if(rsExtn.next()) {

	                Extn extn =  new Extn();

	                extn.setExtnWISOrderAmount(rsExtn.getString("wis_order_amount"));

	                extn.setExtnAffiliateID(rsExtn.getString("affiliate_id"));

	                order.setExtn(extn);
	            }

	      
	            // SHIP TO
	         

	            Statement stmtShip = con.createStatement();

	            ResultSet rsShip = stmtShip.executeQuery("SELECT * FROM PersonInfoShipTo WHERE order_id=" + id);

	            if(rsShip.next()) {

	                PersonInfoShipTo ship = new PersonInfoShipTo();

	                ship.setCountry(rsShip.getString("country"));

	                order.setPersonInfoShipTo(ship);
	            }

	      
	            // BILL TO
	       

	            Statement stmtBill = con.createStatement();

	            ResultSet rsBill = stmtBill.executeQuery( "SELECT * FROM PersonInfoBillTo WHERE order_id=" + id);

	            if(rsBill.next()) {

	                PersonInfoBillTo bill = new PersonInfoBillTo();

	                bill.setCountry(rsBill.getString( "country"));

	                order.setPersonInfoBillTo(bill);
	            }

	       
	            // PAYMENT METHODS
	      

	            List<PaymentMethod> payments = new ArrayList<>();
	            

	            Statement stmtPay = con.createStatement();

	            ResultSet rsPay = stmtPay.executeQuery("SELECT * FROM PaymentMethods WHERE order_id=" + id);
	            

	            while(rsPay.next()) {
	            	

	                PaymentMethod pm = new PaymentMethod();

	                pm.setChargeSequence(rsPay.getString("charge_sequence"));

	                pm.setCreditCardExpDate(rsPay.getString( "credit_card_exp_date"));

	                pm.setCreditCardName(rsPay.getString("credit_card_name"));

	                pm.setCreditCardNo(rsPay.getString("credit_card_no"));

	                pm.setCreditCardType(rsPay.getString("credit_card_type"));

	                pm.setDisplayCreditCardNo(rsPay.getString( "display_credit_card_no"));

	                pm.setPaymentType( rsPay.getString("payment_type"));

	                pm.setSvcNo(rsPay.getString("svc_no"));

	                pm.setUnlimitedCharges(rsPay.getString("unlimited_charges"));
	                

	                payments.add(pm);
	                
	                
	            }
	            
	            PaymentMethods paymentMethods = new PaymentMethods();
	            paymentMethods.setPaymentMethod(payments);
	            order.setPaymentMethods(paymentMethods);

	    
	            // ORDER LINES
	   

	            List<OrderLine> lines = new ArrayList<>();

	            Statement stmtLine = con.createStatement();

	            ResultSet rsLine = stmtLine.executeQuery("SELECT * FROM OrderLines WHERE order_id=" + id);

	            while(rsLine.next()) {

	                OrderLine line = new OrderLine();

	                line.setOrderedQty(rsLine.getInt("ordered_qty"));

	                line.setPrimeLineNo(rsLine.getInt("prime_line_no"));

	                line.setSubLineNo(rsLine.getInt("sub_line_no"));

	                line.setDeliveryMethod(rsLine.getString("delivery_method"));

	                line.setShipNode(rsLine.getString("ship_node"));

	                int lineId = rsLine.getInt("line_id");

	       
	                // ITEM
	     

	                Statement stmtItem = con.createStatement();

	                ResultSet rsItem = stmtItem.executeQuery( "SELECT * FROM Items WHERE line_id=" + lineId);

	                if(rsItem.next()) {

	                    Item item = new Item();

	                    item.setItemID(rsItem.getString("item_id"));

	                    item.setProductClass(rsItem.getString("product_class"));

	                    item.setUnitOfMeasure(rsItem.getString("unit_of_measure"));

	                    line.setItem(item);
	                }

	        
	                // PRICE INFO
	        

	                Statement stmtPrice = con.createStatement();

	                ResultSet rsPrice = stmtPrice.executeQuery("SELECT * FROM LinePriceInfo WHERE line_id="+ lineId);

	                if(rsPrice.next()) {

	                LinePriceInfo price = new LinePriceInfo();

	                price.setUnitPrice(rsPrice.getDouble("unit_price"));

	                line.setLinePriceInfo(price);
	                }

	                lines.add(line);
	            }
	            OrderLines orderLines = new OrderLines();
	            orderLines.setOrderLine(lines);
	            order.setOrderLines(orderLines);

	            con.close();

	        }
	        catch(Exception e) {

	            e.printStackTrace();
	        }

	        return order;
	    }
	
	    @GetMapping(value="/readall",produces="application/xml")
	    public Orders readAll() {

	        Orders orders = new Orders();

	        List<Order> orderList = new ArrayList<>();

	        try {

	            Class.forName("org.postgresql.Driver");

	            Connection con = DriverManager.getConnection(DB_URL,USER,PASS);

	            Statement stmtOrder = con.createStatement();

	            ResultSet rsOrder = stmtOrder.executeQuery("SELECT * FROM Orders");

	            while(rsOrder.next()) {

	                Order order = new Order();

	                int orderId = rsOrder.getInt("order_id");

	                order.setDocumentType(rsOrder.getString( "document_type"));

	                order.setEnterpriseCode(rsOrder.getString("enterprise_code"));

	           
	                // EXTN
	        

	                Statement stmtExtn = con.createStatement();

	                ResultSet rsExtn = stmtExtn.executeQuery("SELECT * FROM Extn WHERE order_id=" + orderId);

	                if(rsExtn.next()) {

	                    Extn extn = new Extn();

	                    extn.setExtnWISOrderAmount(rsExtn.getString("wis_order_amount"));

	                    extn.setExtnAffiliateID(rsExtn.getString("affiliate_id"));

	                    order.setExtn(extn);
	                }

	      
	                // SHIP TO
	      

	                Statement stmtShip = con.createStatement();

	                ResultSet rsShip = stmtShip.executeQuery("SELECT * FROM PersonInfoShipTo WHERE order_id=" + orderId);

	                if(rsShip.next()) {

	                    PersonInfoShipTo ship = new PersonInfoShipTo();

	                    ship.setCountry(rsShip.getString("country"));

	                    order.setPersonInfoShipTo(ship);
	                }

	    
	                // BILL TO
	      

	                Statement stmtBill = con.createStatement();

	                ResultSet rsBill = stmtBill.executeQuery("SELECT * FROM PersonInfoBillTo WHERE order_id=" + orderId);

	                if(rsBill.next()) {

	                    PersonInfoBillTo bill = new PersonInfoBillTo();

	                    bill.setCountry(rsBill.getString("country"));

	                    order.setPersonInfoBillTo(bill);
	                }

	        
	                // PAYMENT METHODS
	        

	                List<PaymentMethod> payments = new ArrayList<>();

	                Statement stmtPay = con.createStatement();

	                ResultSet rsPay = stmtPay.executeQuery( "SELECT * FROM PaymentMethods WHERE order_id=" + orderId);
	                
	               

	                while(rsPay.next()) {

	                    PaymentMethod pm = new PaymentMethod();

	                    pm.setChargeSequence(String.valueOf(rsPay.getInt("charge_sequence")));

	                    pm.setCreditCardExpDate(rsPay.getString("credit_card_exp_date"));

	                    pm.setCreditCardName(rsPay.getString("credit_card_name"));

	                    pm.setCreditCardNo(rsPay.getString("credit_card_no"));

	                    pm.setCreditCardType(rsPay.getString("credit_card_type"));

	                    pm.setDisplayCreditCardNo(rsPay.getString("display_credit_card_no"));

	                    pm.setPaymentType(rsPay.getString("payment_type"));

	                    pm.setSvcNo(rsPay.getString("svc_no"));

	                    pm.setUnlimitedCharges(rsPay.getString("unlimited_charges"));

	                    payments.add(pm);
	                }
	                PaymentMethods paymentMethods = new PaymentMethods();
	                paymentMethods.setPaymentMethod(payments);
	                order.setPaymentMethods(paymentMethods);

	            
	                // ORDER LINES
	          

	                List<OrderLine> lines = new ArrayList<>();

	                Statement stmtLine = con.createStatement();

	                ResultSet rsLine = stmtLine.executeQuery("SELECT * FROM OrderLines WHERE order_id=" + orderId);

	                while(rsLine.next()) {

	                    OrderLine line = new OrderLine();

	                    line.setOrderedQty(rsLine.getInt("ordered_qty"));

	                    line.setPrimeLineNo(rsLine.getInt("prime_line_no"));

	                    line.setSubLineNo(rsLine.getInt("sub_line_no"));

	                    line.setDeliveryMethod(rsLine.getString("delivery_method"));

	                    line.setShipNode(rsLine.getString("ship_node"));

	                    int lineId = rsLine.getInt("line_id");

	                 
	                    // ITEM
	                

	                    Statement stmtItem = con.createStatement();

	                    ResultSet rsItem = stmtItem.executeQuery("SELECT * FROM Items WHERE line_id=" + lineId);

	                    if(rsItem.next()) {

	                        Item item = new Item();

	                        item.setItemID(rsItem.getString("item_id"));

	                        item.setProductClass(rsItem.getString("product_class"));

	                        item.setUnitOfMeasure(rsItem.getString("unit_of_measure"));

	                        line.setItem(item);
	                    }

	           
	                    // PRICE INFO
	             

	                    Statement stmtPrice = con.createStatement();

	                    ResultSet rsPrice = stmtPrice.executeQuery("SELECT * FROM LinePriceInfo WHERE line_id=" + lineId);

	                    if(rsPrice.next()) {

	                        LinePriceInfo price = new LinePriceInfo();

	                        price.setUnitPrice(rsPrice.getDouble("unit_price"));

	                        line.setLinePriceInfo(price);
	                    }

	                    lines.add(line);
	                }
	                OrderLines orderLines = new OrderLines();
	                orderLines.setOrderLine(lines);
	                order.setOrderLines(orderLines);

	                orderList.add(order);
	            }

	            orders.setOrders(
	                    orderList);

	            con.close();
	        }
	        catch(Exception e) {

	            e.printStackTrace();
	        }

	        return orders;
	    }
	    
	    
	}
	

