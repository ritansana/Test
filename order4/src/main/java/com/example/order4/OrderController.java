package com.example.order4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.io.StringReader;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@RestController
public class OrderController {

	private static final String DB_URL = "jdbc:postgresql://localhost:5432/Order";

	private static final String USER = "postgres";

	private static final String PASS = "password";

	private String createErrorResponse(List<String> errors) {

	    StringBuilder xml = new StringBuilder();

	    xml.append("<Errors>");

	    int errorCode = 10001;

	    for(String error : errors) {

	        xml.append("<Error ");
	        xml.append("ErrorCode=\"YFS");
	        xml.append(errorCode);
	        xml.append("\" ");

	        xml.append("ErrorDescription=\"");
	        xml.append(error);
	        xml.append("\"/>");

	        errorCode++;
	    }

	    xml.append("</Errors>");

	    return xml.toString();
	}
	@PostMapping(
		    value="/create",
		    consumes="application/xml",
		    produces="application/xml")
		public String create(@RequestBody String xml) {
		
		
		try {
			
			DocumentBuilderFactory factory =
			        DocumentBuilderFactory.newInstance();
			factory.setFeature(
				    "http://apache.org/xml/features/disallow-doctype-decl",
				    true);

				factory.setFeature(
				    "http://xml.org/sax/features/external-general-entities",
				    false);

				factory.setFeature(
				    "http://xml.org/sax/features/external-parameter-entities",
				    false);

			DocumentBuilder builder =
			        factory.newDocumentBuilder();

			Document doc = builder.parse(
			        new InputSource(new StringReader(xml)));
			

			List<String> errors = new ArrayList<>();

			validateNode(doc.getDocumentElement(), errors);


			if(!errors.isEmpty()) {

			    return createErrorResponse(errors);

			}
			XmlMapper mapper = new XmlMapper();

			Orders orders = mapper.readValue(xml, Orders.class);

			Order order = orders.getOrderList().get(0);

			Class.forName("org.postgresql.Driver");

			Connection con = DriverManager.getConnection(DB_URL, USER, PASS);

			// ------------------------
			// ORDER HEADER
			// ------------------------

			String orderSql = "INSERT INTO orders " + "(document_type, enterprise_code, "
					+ "ship_country, bill_country) " + "VALUES(?,?,?,?)";

			PreparedStatement orderPs = con.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS);

			orderPs.setString(1, order.getDocumentType());

			orderPs.setString(2, order.getEnterpriseCode());

			orderPs.setString(3, order.getPersonInfoShipTo().getCountry());

			orderPs.setString(4, order.getPersonInfoBillTo().getCountry());

			orderPs.executeUpdate();

			ResultSet orderRs = orderPs.getGeneratedKeys();

			int orderId = 0;

			if (orderRs.next()) {
				orderId = orderRs.getInt(1);
			}

			// ------------------------
			// ORDER LINES
			// ------------------------

			if (order.getOrderLines() != null) {

				for (OrderLine line : order.getOrderLines().getOrderLineList()) {

					String lineSql = "INSERT INTO order_line " + "(order_id, ordered_qty, "
							+ "prime_line_no, sub_line_no, " + "delivery_method, ship_node, "
							+ "item_id, product_class, " + "uom, unit_price) " + "VALUES(?,?,?,?,?,?,?,?,?,?)";

					PreparedStatement linePs = con.prepareStatement(lineSql, Statement.RETURN_GENERATED_KEYS);

					linePs.setInt(1, orderId);
					linePs.setString(2, line.getOrderedQty());
					linePs.setString(3, line.getPrimeLineNo());
					linePs.setString(4, line.getSubLineNo());
					linePs.setString(5, line.getDeliveryMethod());
					linePs.setString(6, line.getShipNode());

					linePs.setString(7, line.getItem().getItemID());

					linePs.setString(8, line.getItem().getProductClass());

					linePs.setString(9, line.getItem().getUnitOfMeasure());

					linePs.setString(10, line.getLinePriceInfo().getUnitPrice());

					linePs.executeUpdate();

					ResultSet lineRs = linePs.getGeneratedKeys();

					int lineId = 0;

					if (lineRs.next()) {
						lineId = lineRs.getInt(1);
					}

					// ------------------------
					// LINE CHARGES
					// ------------------------

					if (line.getLineCharges() != null) {

						for (LineCharge charge : line.getLineCharges().getLineChargeList()) {

							String chargeSql = "INSERT INTO line_charge " + "(order_line_id, " + "charge_category, "
									+ "charge_name, " + "charge_per_line) " + "VALUES(?,?,?,?)";

							PreparedStatement chargePs = con.prepareStatement(chargeSql);

							chargePs.setInt(1, lineId);

							chargePs.setString(2, charge.getChargeCategory());

							chargePs.setString(3, charge.getChargeName());

							chargePs.setString(4, charge.getChargePerLine());

							chargePs.executeUpdate();
						}
					}

					// ------------------------
					// LINE TAXES
					// ------------------------

					if (line.getLineTaxes() != null) {

						for (LineTax tax : line.getLineTaxes().getLineTaxList()) {

							String taxSql = "INSERT INTO line_tax " + "(order_line_id, " + "charge_category, "
									+ "tax, tax_name, " + "tax_percentage) " + "VALUES(?,?,?,?,?)";

							PreparedStatement taxPs = con.prepareStatement(taxSql);

							taxPs.setInt(1, lineId);

							taxPs.setString(2, tax.getChargeCategory());

							taxPs.setString(3, tax.getTax());

							taxPs.setString(4, tax.getTaxName());

							taxPs.setString(5, tax.getTaxPercentage());

							taxPs.executeUpdate();
						}
					}
				}
			}

			// ------------------------
			// PAYMENT METHODS
			// ------------------------

			if (order.getPaymentMethods() != null) {

				for (PaymentMethod payment : order.getPaymentMethods().getPaymentMethodList()) {

					String paymentSql = "INSERT INTO payment_method " + "(order_id, charge_sequence, "
							+ "credit_card_exp_date, " + "credit_card_name, " + "credit_card_no, "
							+ "credit_card_type, " + "display_credit_card_no, " + "payment_type, " + "svc_no, "
							+ "unlimited_charges) " + "VALUES(?,?,?,?,?,?,?,?,?,?)";

					PreparedStatement paymentPs = con.prepareStatement(paymentSql);

					paymentPs.setInt(1, orderId);

					paymentPs.setString(2, payment.getChargeSequence());

					paymentPs.setString(3, payment.getCreditCardExpDate());

					paymentPs.setString(4, payment.getCreditCardName());

					paymentPs.setString(5, payment.getCreditCardNo());

					paymentPs.setString(6, payment.getCreditCardType());

					paymentPs.setString(7, payment.getDisplayCreditCardNo());

					paymentPs.setString(8, payment.getPaymentType());

					paymentPs.setString(9, payment.getSvcNo());

					paymentPs.setString(10, payment.getUnlimitedCharges());

					paymentPs.executeUpdate();
				}
			}

			con.close();

			return "<Message>Order Created Successfully</Message>";

		} catch (Exception e) {

			e.printStackTrace();

			return "<Error>" + e.getMessage() + "</Error>";
		}

	}

	private void validateNode(Node node, List<String> errors) {

	    NamedNodeMap attrs = node.getAttributes();

	    if (attrs != null) {

	        for (int i = 0; i < attrs.getLength(); i++) {

	            Node attr = attrs.item(i);

	            if (attr.getNodeValue() == null ||
	                attr.getNodeValue().trim().isEmpty()) {

	                errors.add(
	                    "Attribute "
	                    + attr.getNodeName()
	                    + " is blank in element "
	                    + node.getNodeName()
	                );
	            }
	        }
	    }


	    NodeList children = node.getChildNodes();

	    for (int i = 0; i < children.getLength(); i++) {

	        Node child = children.item(i);

	        if (child.getNodeType() == Node.ELEMENT_NODE) {

	            validateNode(child, errors);
	        }
	    }
	}

	@GetMapping(value = "/read/{id}", produces = "application/xml")
	public Order readOneByPath(@PathVariable int id) {
		Order order = new Order();

		try {

			Class.forName("org.postgresql.Driver");

			Connection con = DriverManager.getConnection(DB_URL, USER, PASS);

			// =========================
			// ORDER HEADER
			// =========================

			String orderSql = "SELECT * FROM orders " + "WHERE order_id=?";

			PreparedStatement orderPs = con.prepareStatement(orderSql);

			orderPs.setInt(1, id);

			ResultSet orderRs = orderPs.executeQuery();

			if (orderRs.next()) {

				order.setDocumentType(orderRs.getString("document_type"));

				order.setEnterpriseCode(orderRs.getString("enterprise_code"));

				PersonInfoShipTo ship = new PersonInfoShipTo();

				ship.setCountry(orderRs.getString("ship_country"));

				order.setPersonInfoShipTo(ship);

				PersonInfoBillTo bill = new PersonInfoBillTo();

				bill.setCountry(orderRs.getString("bill_country"));

				order.setPersonInfoBillTo(bill);
			}

			// =========================
			// ORDER LINES
			// =========================

			List<OrderLine> lineList = new ArrayList<>();

			String lineSql = "SELECT * FROM order_line " + "WHERE order_id=?";

			PreparedStatement linePs = con.prepareStatement(lineSql);

			linePs.setInt(1, id);

			ResultSet lineRs = linePs.executeQuery();

			while (lineRs.next()) {

				OrderLine line = new OrderLine();

				line.setOrderedQty(lineRs.getString("ordered_qty"));

				line.setPrimeLineNo(lineRs.getString("prime_line_no"));

				line.setSubLineNo(lineRs.getString("sub_line_no"));

				line.setDeliveryMethod(lineRs.getString("delivery_method"));

				line.setShipNode(lineRs.getString("ship_node"));

				// ITEM

				Item item = new Item();

				item.setItemID(lineRs.getString("item_id"));

				item.setProductClass(lineRs.getString("product_class"));

				item.setUnitOfMeasure(lineRs.getString("uom"));

				line.setItem(item);

				// PRICE

				LinePriceInfo price = new LinePriceInfo();

				price.setUnitPrice(lineRs.getString("unit_price"));

				line.setLinePriceInfo(price);

				int lineId = lineRs.getInt("order_line_id");

				// =====================
				// CHARGES
				// =====================

				List<LineCharge> chargeList = new ArrayList<>();

				String chargeSql = "SELECT * FROM line_charge " + "WHERE order_line_id=?";

				PreparedStatement chargePs = con.prepareStatement(chargeSql);

				chargePs.setInt(1, lineId);

				ResultSet chargeRs = chargePs.executeQuery();

				while (chargeRs.next()) {

					LineCharge charge = new LineCharge();

					charge.setChargeCategory(chargeRs.getString("charge_category"));

					charge.setChargeName(chargeRs.getString("charge_name"));

					charge.setChargePerLine(chargeRs.getString("charge_per_line"));

					chargeList.add(charge);
				}

				LineCharges lineCharges = new LineCharges();

				lineCharges.setLineChargeList(chargeList);

				line.setLineCharges(lineCharges);

				// =====================
				// TAXES
				// =====================

				List<LineTax> taxList = new ArrayList<>();

				String taxSql = "SELECT * FROM line_tax " + "WHERE order_line_id=?";

				PreparedStatement taxPs = con.prepareStatement(taxSql);

				taxPs.setInt(1, lineId);

				ResultSet taxRs = taxPs.executeQuery();

				while (taxRs.next()) {

					LineTax tax = new LineTax();

					tax.setChargeCategory(taxRs.getString("charge_category"));

					tax.setTax(taxRs.getString("tax"));

					tax.setTaxName(taxRs.getString("tax_name"));

					tax.setTaxPercentage(taxRs.getString("tax_percentage"));

					taxList.add(tax);
				}

				LineTaxes lineTaxes = new LineTaxes();

				lineTaxes.setLineTaxList(taxList);

				line.setLineTaxes(lineTaxes);

				lineList.add(line);
			}

			OrderLines orderLines = new OrderLines();

			orderLines.setOrderLineList(lineList);

			order.setOrderLines(orderLines);

			// =========================
			// PAYMENT METHODS
			// =========================

			List<PaymentMethod> paymentList = new ArrayList<>();

			String paymentSql = "SELECT * FROM payment_method " + "WHERE order_id=?";

			PreparedStatement paymentPs = con.prepareStatement(paymentSql);

			paymentPs.setInt(1, id);

			ResultSet paymentRs = paymentPs.executeQuery();

			while (paymentRs.next()) {

				PaymentMethod payment = new PaymentMethod();

				payment.setChargeSequence(paymentRs.getString("charge_sequence"));

				payment.setCreditCardExpDate(paymentRs.getString("credit_card_exp_date"));

				payment.setCreditCardName(paymentRs.getString("credit_card_name"));

				payment.setCreditCardNo(paymentRs.getString("credit_card_no"));

				payment.setCreditCardType(paymentRs.getString("credit_card_type"));

				payment.setDisplayCreditCardNo(paymentRs.getString("display_credit_card_no"));

				payment.setPaymentType(paymentRs.getString("payment_type"));

				payment.setSvcNo(paymentRs.getString("svc_no"));

				payment.setUnlimitedCharges(paymentRs.getString("unlimited_charges"));

				paymentList.add(payment);
			}

			PaymentMethods paymentMethods = new PaymentMethods();

			paymentMethods.setPaymentMethodList(paymentList);

			order.setPaymentMethods(paymentMethods);

			con.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return order;
	}

	@PostMapping(value = "/read", consumes = "application/xml", produces = "application/xml")
	public Order readOne(@RequestBody OrderRequest request) {

		int id = request.getId();

		Order order = new Order();

		try {

			Class.forName("org.postgresql.Driver");

			Connection con = DriverManager.getConnection(DB_URL, USER, PASS);

			// =========================
			// ORDER HEADER
			// =========================

			String orderSql = "SELECT * FROM orders " + "WHERE order_id=?";

			PreparedStatement orderPs = con.prepareStatement(orderSql);

			orderPs.setInt(1, id);

			ResultSet orderRs = orderPs.executeQuery();

			if (orderRs.next()) {

				order.setDocumentType(orderRs.getString("document_type"));

				order.setEnterpriseCode(orderRs.getString("enterprise_code"));

				PersonInfoShipTo ship = new PersonInfoShipTo();

				ship.setCountry(orderRs.getString("ship_country"));

				order.setPersonInfoShipTo(ship);

				PersonInfoBillTo bill = new PersonInfoBillTo();

				bill.setCountry(orderRs.getString("bill_country"));

				order.setPersonInfoBillTo(bill);
			}

			// =========================
			// ORDER LINES
			// =========================

			List<OrderLine> lineList = new ArrayList<>();

			String lineSql = "SELECT * FROM order_line " + "WHERE order_id=?";

			PreparedStatement linePs = con.prepareStatement(lineSql);

			linePs.setInt(1, id);

			ResultSet lineRs = linePs.executeQuery();

			while (lineRs.next()) {

				OrderLine line = new OrderLine();

				line.setOrderedQty(lineRs.getString("ordered_qty"));

				line.setPrimeLineNo(lineRs.getString("prime_line_no"));

				line.setSubLineNo(lineRs.getString("sub_line_no"));

				line.setDeliveryMethod(lineRs.getString("delivery_method"));

				line.setShipNode(lineRs.getString("ship_node"));

				// ITEM

				Item item = new Item();

				item.setItemID(lineRs.getString("item_id"));

				item.setProductClass(lineRs.getString("product_class"));

				item.setUnitOfMeasure(lineRs.getString("uom"));

				line.setItem(item);

				// PRICE

				LinePriceInfo price = new LinePriceInfo();

				price.setUnitPrice(lineRs.getString("unit_price"));

				line.setLinePriceInfo(price);

				int lineId = lineRs.getInt("order_line_id");

				// =====================
				// CHARGES
				// =====================

				List<LineCharge> chargeList = new ArrayList<>();

				String chargeSql = "SELECT * FROM line_charge " + "WHERE order_line_id=?";

				PreparedStatement chargePs = con.prepareStatement(chargeSql);

				chargePs.setInt(1, lineId);

				ResultSet chargeRs = chargePs.executeQuery();

				while (chargeRs.next()) {

					LineCharge charge = new LineCharge();

					charge.setChargeCategory(chargeRs.getString("charge_category"));

					charge.setChargeName(chargeRs.getString("charge_name"));

					charge.setChargePerLine(chargeRs.getString("charge_per_line"));

					chargeList.add(charge);
				}

				LineCharges lineCharges = new LineCharges();

				lineCharges.setLineChargeList(chargeList);

				line.setLineCharges(lineCharges);

				// =====================
				// TAXES
				// =====================

				List<LineTax> taxList = new ArrayList<>();

				String taxSql = "SELECT * FROM line_tax " + "WHERE order_line_id=?";

				PreparedStatement taxPs = con.prepareStatement(taxSql);

				taxPs.setInt(1, lineId);

				ResultSet taxRs = taxPs.executeQuery();

				while (taxRs.next()) {

					LineTax tax = new LineTax();

					tax.setChargeCategory(taxRs.getString("charge_category"));

					tax.setTax(taxRs.getString("tax"));

					tax.setTaxName(taxRs.getString("tax_name"));

					tax.setTaxPercentage(taxRs.getString("tax_percentage"));

					taxList.add(tax);
				}

				LineTaxes lineTaxes = new LineTaxes();

				lineTaxes.setLineTaxList(taxList);

				line.setLineTaxes(lineTaxes);

				lineList.add(line);
			}

			OrderLines orderLines = new OrderLines();

			orderLines.setOrderLineList(lineList);

			order.setOrderLines(orderLines);

			// =========================
			// PAYMENT METHODS
			// =========================

			List<PaymentMethod> paymentList = new ArrayList<>();

			String paymentSql = "SELECT * FROM payment_method " + "WHERE order_id=?";

			PreparedStatement paymentPs = con.prepareStatement(paymentSql);

			paymentPs.setInt(1, id);

			ResultSet paymentRs = paymentPs.executeQuery();

			while (paymentRs.next()) {

				PaymentMethod payment = new PaymentMethod();

				payment.setChargeSequence(paymentRs.getString("charge_sequence"));

				payment.setCreditCardExpDate(paymentRs.getString("credit_card_exp_date"));

				payment.setCreditCardName(paymentRs.getString("credit_card_name"));

				payment.setCreditCardNo(paymentRs.getString("credit_card_no"));

				payment.setCreditCardType(paymentRs.getString("credit_card_type"));

				payment.setDisplayCreditCardNo(paymentRs.getString("display_credit_card_no"));

				payment.setPaymentType(paymentRs.getString("payment_type"));

				payment.setSvcNo(paymentRs.getString("svc_no"));

				payment.setUnlimitedCharges(paymentRs.getString("unlimited_charges"));

				paymentList.add(payment);
			}

			PaymentMethods paymentMethods = new PaymentMethods();

			paymentMethods.setPaymentMethodList(paymentList);

			order.setPaymentMethods(paymentMethods);

			con.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return order;
	}

	@GetMapping(value = "/readall", produces = "application/xml")
	public Orders readAll() {

		Orders ordersWrapper = new Orders();

		List<Order> orderList = new ArrayList<>();

		try {

			Class.forName("org.postgresql.Driver");

			Connection con = DriverManager.getConnection(DB_URL, USER, PASS);

			String orderSql = "SELECT * FROM orders";

			PreparedStatement orderPs = con.prepareStatement(orderSql);

			ResultSet orderRs = orderPs.executeQuery();

			while (orderRs.next()) {

				int orderId = orderRs.getInt("order_id");

				Order order = new Order();

				order.setDocumentType(orderRs.getString("document_type"));

				order.setEnterpriseCode(orderRs.getString("enterprise_code"));

				PersonInfoShipTo ship = new PersonInfoShipTo();

				ship.setCountry(orderRs.getString("ship_country"));

				order.setPersonInfoShipTo(ship);

				PersonInfoBillTo bill = new PersonInfoBillTo();

				bill.setCountry(orderRs.getString("bill_country"));

				order.setPersonInfoBillTo(bill);

				// =====================
				// ORDER LINES
				// =====================

				List<OrderLine> lineList = new ArrayList<>();

				String lineSql = "SELECT * FROM order_line " + "WHERE order_id=?";

				PreparedStatement linePs = con.prepareStatement(lineSql);

				linePs.setInt(1, orderId);

				ResultSet lineRs = linePs.executeQuery();

				while (lineRs.next()) {

					OrderLine line = new OrderLine();

					line.setOrderedQty(lineRs.getString("ordered_qty"));

					line.setPrimeLineNo(lineRs.getString("prime_line_no"));

					line.setSubLineNo(lineRs.getString("sub_line_no"));

					line.setDeliveryMethod(lineRs.getString("delivery_method"));

					line.setShipNode(lineRs.getString("ship_node"));

					Item item = new Item();

					item.setItemID(lineRs.getString("item_id"));

					item.setProductClass(lineRs.getString("product_class"));

					item.setUnitOfMeasure(lineRs.getString("uom"));

					line.setItem(item);

					LinePriceInfo price = new LinePriceInfo();

					price.setUnitPrice(lineRs.getString("unit_price"));

					line.setLinePriceInfo(price);

					int lineId = lineRs.getInt("order_line_id");

					// =====================
					// LINE CHARGES
					// =====================

					List<LineCharge> chargeList = new ArrayList<>();

					String chargeSql = "SELECT * FROM line_charge " + "WHERE order_line_id=?";

					PreparedStatement chargePs = con.prepareStatement(chargeSql);

					chargePs.setInt(1, lineId);

					ResultSet chargeRs = chargePs.executeQuery();

					while (chargeRs.next()) {

						LineCharge charge = new LineCharge();

						charge.setChargeCategory(chargeRs.getString("charge_category"));

						charge.setChargeName(chargeRs.getString("charge_name"));

						charge.setChargePerLine(chargeRs.getString("charge_per_line"));

						chargeList.add(charge);
					}

					LineCharges lineCharges = new LineCharges();

					lineCharges.setLineChargeList(chargeList);

					line.setLineCharges(lineCharges);

					// =====================
					// LINE TAXES
					// =====================

					List<LineTax> taxList = new ArrayList<>();

					String taxSql = "SELECT * FROM line_tax " + "WHERE order_line_id=?";

					PreparedStatement taxPs = con.prepareStatement(taxSql);

					taxPs.setInt(1, lineId);

					ResultSet taxRs = taxPs.executeQuery();

					while (taxRs.next()) {

						LineTax tax = new LineTax();

						tax.setChargeCategory(taxRs.getString("charge_category"));

						tax.setTax(taxRs.getString("tax"));

						tax.setTaxName(taxRs.getString("tax_name"));

						tax.setTaxPercentage(taxRs.getString("tax_percentage"));

						taxList.add(tax);
					}

					LineTaxes lineTaxes = new LineTaxes();

					lineTaxes.setLineTaxList(taxList);

					line.setLineTaxes(lineTaxes);

					lineList.add(line);
				}

				OrderLines orderLines = new OrderLines();

				orderLines.setOrderLineList(lineList);

				order.setOrderLines(orderLines);

				// =====================
				// PAYMENT METHODS
				// =====================

				List<PaymentMethod> paymentList = new ArrayList<>();

				String paymentSql = "SELECT * FROM payment_method " + "WHERE order_id=?";

				PreparedStatement paymentPs = con.prepareStatement(paymentSql);

				paymentPs.setInt(1, orderId);

				ResultSet paymentRs = paymentPs.executeQuery();

				while (paymentRs.next()) {

					PaymentMethod payment = new PaymentMethod();

					payment.setChargeSequence(paymentRs.getString("charge_sequence"));

					payment.setCreditCardExpDate(paymentRs.getString("credit_card_exp_date"));

					payment.setCreditCardName(paymentRs.getString("credit_card_name"));

					payment.setCreditCardNo(paymentRs.getString("credit_card_no"));

					payment.setCreditCardType(paymentRs.getString("credit_card_type"));

					payment.setDisplayCreditCardNo(paymentRs.getString("display_credit_card_no"));

					payment.setPaymentType(paymentRs.getString("payment_type"));

					payment.setSvcNo(paymentRs.getString("svc_no"));

					payment.setUnlimitedCharges(paymentRs.getString("unlimited_charges"));

					paymentList.add(payment);
				}

				PaymentMethods paymentMethods = new PaymentMethods();

				paymentMethods.setPaymentMethodList(paymentList);

				order.setPaymentMethods(paymentMethods);

				orderList.add(order);
			}

			ordersWrapper.setOrderList(orderList);

			con.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return ordersWrapper;

	}
}
