import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Native Java Web Server (Zero JavaScript, 100% Java & Server-Side Rendered HTML/CSS)
 */
public class WebServer {

    private final int port;
    private final RentalAdmin admin;
    private final File webDir;
    private HttpServer server;

    public WebServer(int port, RentalAdmin admin) {
        this.port = port;
        this.admin = admin;
        this.webDir = new File("web");
    }

    public void start() throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.setExecutor(Executors.newFixedThreadPool(10));

        // Core Web Handlers (Zero JavaScript)
        server.createContext("/", new RootHandler());
        server.createContext("/fleet", new FleetHandler());
        server.createContext("/rentals", new RentalsHandler());
        server.createContext("/customers", new CustomersHandler());
        server.createContext("/docs", new DocsHandler());
        server.createContext("/book", new BookHandler());
        server.createContext("/return", new ReturnHandler());
        server.createContext("/add-vehicle", new AddVehicleHandler());
        server.createContext("/edit-vehicle", new EditVehicleHandler());
        server.createContext("/delete-vehicle", new DeleteVehicleHandler());
        server.createContext("/add-customer", new AddCustomerHandler());
        server.createContext("/styles.css", new CssHandler());

        server.start();
        System.out.println("=================================================");
        System.out.println("  VEHICLE RENTAL SYSTEM WEB APPLICATION STARTED  ");
        System.out.println("  (100% Java & Server-Side Rendered — No JS)     ");
        System.out.println("  URL: http://localhost:" + port);
        System.out.println("=================================================");
    }

    public void stop() {
        if (server != null) {
            server.stop(0);
        }
    }

    // --- Helper Methods ---
    private static void sendHtmlResponse(HttpExchange exchange, int statusCode, String html) throws IOException {
        byte[] bytes = html.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/html; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static void redirect(HttpExchange exchange, String location) throws IOException {
        exchange.getResponseHeaders().set("Location", location);
        exchange.sendResponseHeaders(302, -1);
    }

    private static Map<String, String> parseFormData(HttpExchange exchange) throws IOException {
        String body;
        try (InputStream is = exchange.getRequestBody();
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            body = baos.toString(StandardCharsets.UTF_8);
        }
        return parseQueryString(body);
    }

    private static Map<String, String> parseQueryString(String query) {
        Map<String, String> map = new HashMap<>();
        if (query == null || query.trim().isEmpty()) return map;
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            try {
                if (idx > 0) {
                    String key = URLDecoder.decode(pair.substring(0, idx), "UTF-8");
                    String value = URLDecoder.decode(pair.substring(idx + 1), "UTF-8");
                    map.put(key, value);
                } else if (idx < 0) {
                    map.put(URLDecoder.decode(pair, "UTF-8"), "");
                }
            } catch (UnsupportedEncodingException ignored) {}
        }
        return map;
    }

    private static String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (Exception e) {
            return s;
        }
    }

    // --- Request Handlers ---

    private class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (path.equals("/") || path.isEmpty()) {
                redirect(exchange, "/fleet");
            } else {
                sendHtmlResponse(exchange, 404, "<h1>404 Not Found</h1><p><a href='/fleet'>Return to Fleet</a></p>");
            }
        }
    }

    private class FleetHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, String> query = parseQueryString(exchange.getRequestURI().getQuery());
            String type = query.get("type");
            String brand = query.get("brand");
            boolean available = "true".equalsIgnoreCase(query.get("available"));
            String msg = query.get("msg");
            String msgType = query.get("type_msg");

            String content = HtmlRenderer.renderFleetPage(admin, type, brand, available);
            String html = HtmlRenderer.renderLayout("Fleet Explorer", "fleet", content, msg, msgType);
            sendHtmlResponse(exchange, 200, html);
        }
    }

    private class RentalsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, String> query = parseQueryString(exchange.getRequestURI().getQuery());
            String msg = query.get("msg");
            String msgType = query.get("type_msg");

            String content = HtmlRenderer.renderRentalsPage(admin);
            String html = HtmlRenderer.renderLayout("Active Rentals", "rentals", content, msg, msgType);
            sendHtmlResponse(exchange, 200, html);
        }
    }

    private class CustomersHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, String> query = parseQueryString(exchange.getRequestURI().getQuery());
            Integer viewId = null;
            if (query.containsKey("id")) {
                try { viewId = Integer.parseInt(query.get("id")); } catch (Exception ignored) {}
            }
            String msg = query.get("msg");
            String msgType = query.get("type_msg");

            String content = HtmlRenderer.renderCustomersPage(admin, viewId);
            String html = HtmlRenderer.renderLayout("Customers Directory", "customers", content, msg, msgType);
            sendHtmlResponse(exchange, 200, html);
        }
    }

    private class DocsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String content = HtmlRenderer.renderDocsPage();
            String html = HtmlRenderer.renderLayout("System Documentation", "docs", content, null, null);
            sendHtmlResponse(exchange, 200, html);
        }
    }

    private class BookHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                Map<String, String> query = parseQueryString(exchange.getRequestURI().getQuery());
                int vehicleId = 0;
                if (query.containsKey("vehicleId")) {
                    try { vehicleId = Integer.parseInt(query.get("vehicleId")); } catch (Exception ignored) {}
                }
                String msg = query.get("msg");
                String content = HtmlRenderer.renderBookingPage(admin, vehicleId);
                String html = HtmlRenderer.renderLayout("Book a Vehicle", "fleet", content, msg, "error");
                sendHtmlResponse(exchange, 200, html);
            } else if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                Map<String, String> form = parseFormData(exchange);
                try {
                    int custId = Integer.parseInt(form.get("customerId"));
                    int vehId = Integer.parseInt(form.get("vehicleId"));
                    LocalDate start = LocalDate.parse(form.get("startDate"));
                    LocalDate end = LocalDate.parse(form.get("endDate"));
                    double deposit = Double.parseDouble(form.get("deposit"));

                    Rental rental = admin.bookVehicle(custId, vehId, start, end, deposit);
                    if (rental != null) {
                        String msg = "Booking #" + rental.getRentalId() + " successfully confirmed for " + rental.getCustomer().getName() + "!";
                        redirect(exchange, "/rentals?msg=" + urlEncode(msg) + "&type_msg=success");
                    } else {
                        String err = "Booking could not be completed. Vehicle may be already rented or customer ID invalid.";
                        redirect(exchange, "/book?msg=" + urlEncode(err));
                    }
                } catch (Exception e) {
                    redirect(exchange, "/book?msg=" + urlEncode("Invalid booking form inputs: " + e.getMessage()));
                }
            }
        }
    }

    private class ReturnHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                Map<String, String> query = parseQueryString(exchange.getRequestURI().getQuery());
                int rentalId = 0;
                if (query.containsKey("rentalId")) {
                    try { rentalId = Integer.parseInt(query.get("rentalId")); } catch (Exception ignored) {}
                }
                String content = HtmlRenderer.renderReturnPage(admin, rentalId);
                String html = HtmlRenderer.renderLayout("Return Vehicle", "rentals", content, null, null);
                sendHtmlResponse(exchange, 200, html);
            } else if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                Map<String, String> form = parseFormData(exchange);
                try {
                    int rentalId = Integer.parseInt(form.get("rentalId"));
                    LocalDate returnDate = LocalDate.parse(form.get("returnDate"));

                    Rental rental = admin.returnVehicle(rentalId, returnDate);
                    if (rental != null) {
                        String msg = "Vehicle returned! Late Penalty: Rs. " + String.format("%,.0f", rental.getLatePenalty()) + " | Refund: Rs. " + String.format("%,.0f", rental.getRefundAmount());
                        redirect(exchange, "/rentals?msg=" + urlEncode(msg) + "&type_msg=success");
                    } else {
                        redirect(exchange, "/rentals?msg=" + urlEncode("Return failed. Rental not found or already returned.") + "&type_msg=error");
                    }
                } catch (Exception e) {
                    redirect(exchange, "/rentals?msg=" + urlEncode("Error processing return: " + e.getMessage()) + "&type_msg=error");
                }
            }
        }
    }

    private class AddVehicleHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                String content = HtmlRenderer.renderAddOrEditVehiclePage(null);
                String html = HtmlRenderer.renderLayout("Add Vehicle", "fleet", content, null, null);
                sendHtmlResponse(exchange, 200, html);
            } else if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                Map<String, String> form = parseFormData(exchange);
                try {
                    String type = form.getOrDefault("type", "Car");
                    String brand = form.get("brand");
                    String model = form.get("model");
                    double rate = Double.parseDouble(form.get("rentalRate"));
                    String features = form.get("features");
                    String imageUrl = form.get("imageUrl");

                    Vehicle v;
                    if ("Bike".equalsIgnoreCase(type)) {
                        v = new Bike(0, brand, model, rate, 250, "Standard", imageUrl);
                    } else if ("Van".equalsIgnoreCase(type)) {
                        v = new Van(0, brand, model, rate, 8, 800, imageUrl);
                    } else {
                        v = new Car(0, brand, model, rate, 5, "Petrol", imageUrl);
                    }
                    if (features != null && !features.trim().isEmpty()) {
                        v.setFeatures(features);
                    }

                    admin.addVehicle(v);
                    redirect(exchange, "/fleet?msg=" + urlEncode("Vehicle #" + v.getVehicleId() + " (" + brand + " " + model + ") added to fleet!") + "&type_msg=success");
                } catch (Exception e) {
                    redirect(exchange, "/fleet?msg=" + urlEncode("Failed to add vehicle: " + e.getMessage()) + "&type_msg=error");
                }
            }
        }
    }

    private class EditVehicleHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                Map<String, String> query = parseQueryString(exchange.getRequestURI().getQuery());
                int id = Integer.parseInt(query.getOrDefault("id", "0"));
                Vehicle v = admin.findVehicle(id);
                if (v == null) {
                    redirect(exchange, "/fleet?msg=" + urlEncode("Vehicle not found.") + "&type_msg=error");
                    return;
                }
                String content = HtmlRenderer.renderAddOrEditVehiclePage(v);
                String html = HtmlRenderer.renderLayout("Edit Vehicle #" + id, "fleet", content, null, null);
                sendHtmlResponse(exchange, 200, html);
            } else if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                Map<String, String> form = parseFormData(exchange);
                try {
                    int id = Integer.parseInt(form.get("vehicleId"));
                    String type = form.get("type");
                    String brand = form.get("brand");
                    String model = form.get("model");
                    double rate = Double.parseDouble(form.get("rentalRate"));
                    String features = form.get("features");
                    String imageUrl = form.get("imageUrl");

                    boolean ok = admin.updateVehicle(id, type, brand, model, rate);
                    if (ok) {
                        Vehicle v = admin.findVehicle(id);
                        if (features != null) v.setFeatures(features);
                        if (imageUrl != null && !imageUrl.trim().isEmpty()) v.setImageUrl(imageUrl);
                        redirect(exchange, "/fleet?msg=" + urlEncode("Vehicle #" + id + " updated successfully!") + "&type_msg=success");
                    } else {
                        redirect(exchange, "/fleet?msg=" + urlEncode("Vehicle #" + id + " could not be updated.") + "&type_msg=error");
                    }
                } catch (Exception e) {
                    redirect(exchange, "/fleet?msg=" + urlEncode("Error updating vehicle: " + e.getMessage()) + "&type_msg=error");
                }
            }
        }
    }

    private class DeleteVehicleHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                Map<String, String> form = parseFormData(exchange);
                try {
                    int id = Integer.parseInt(form.get("vehicleId"));
                    boolean ok = admin.removeVehicle(id);
                    if (ok) {
                        redirect(exchange, "/fleet?msg=" + urlEncode("Vehicle #" + id + " removed from fleet.") + "&type_msg=success");
                    } else {
                        redirect(exchange, "/fleet?msg=" + urlEncode("Cannot remove vehicle #" + id + " (either not found or currently rented).") + "&type_msg=error");
                    }
                } catch (Exception e) {
                    redirect(exchange, "/fleet?msg=" + urlEncode("Error removing vehicle: " + e.getMessage()) + "&type_msg=error");
                }
            } else {
                redirect(exchange, "/fleet");
            }
        }
    }

    private class AddCustomerHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if ("GET".equalsIgnoreCase(exchange.getRequestMethod())) {
                String content = HtmlRenderer.renderAddCustomerPage();
                String html = HtmlRenderer.renderLayout("Register Customer", "customers", content, null, null);
                sendHtmlResponse(exchange, 200, html);
            } else if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
                Map<String, String> form = parseFormData(exchange);
                try {
                    String name = form.get("name");
                    String contact = form.get("contactDetails");
                    String email = form.get("email");
                    String license = form.get("licenseNumber");

                    Customer c = new Customer(0, name, contact, email, license);
                    admin.addCustomer(c);
                    redirect(exchange, "/customers?msg=" + urlEncode("Customer '" + name + "' registered with ID #" + c.getCustomerId()) + "&type_msg=success");
                } catch (Exception e) {
                    redirect(exchange, "/customers?msg=" + urlEncode("Failed to register customer: " + e.getMessage()) + "&type_msg=error");
                }
            }
        }
    }

    private class CssHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            File file = new File(webDir, "styles.css");
            if (!file.exists()) {
                exchange.sendResponseHeaders(404, -1);
                return;
            }
            byte[] bytes = Files.readAllBytes(file.toPath());
            exchange.getResponseHeaders().set("Content-Type", "text/css; charset=UTF-8");
            exchange.sendResponseHeaders(200, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
    }
}
