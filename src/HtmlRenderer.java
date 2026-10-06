import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * Server-Side HTML Renderer
 * Generates rich HTML5 pages with CSS3 styling.
 * 100% Pure Java & HTML/CSS — ZERO JavaScript.
 */
public class HtmlRenderer {

    public static String renderLayout(String title, String activeTab, String content, String message, String messageType) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>\n<html lang=\"en\">\n<head>\n");
        sb.append("  <meta charset=\"UTF-8\">\n");
        sb.append("  <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        sb.append("  <title>").append(title).append(" — AutoRent Pro</title>\n");
        sb.append("  <link rel=\"preconnect\" href=\"https://fonts.googleapis.com\">\n");
        sb.append("  <link rel=\"preconnect\" href=\"https://fonts.gstatic.com\" crossorigin>\n");
        sb.append("  <link href=\"https://fonts.googleapis.com/css2?family=Outfit:wght@400;500;600;700;800&family=Plus+Jakarta+Sans:wght@300;400;500;600;700&display=swap\" rel=\"stylesheet\">\n");
        sb.append("  <link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css\">\n");
        sb.append("  <link rel=\"stylesheet\" href=\"/styles.css\">\n");
        sb.append("</head>\n<body>\n");

        // Top Navigation
        sb.append("<header class=\"navbar\">\n");
        sb.append("  <div class=\"nav-container\">\n");
        sb.append("    <a href=\"/\" class=\"brand\">\n");
        sb.append("      <div class=\"brand-icon\"><i class=\"fa-solid fa-car-side\"></i></div>\n");
        sb.append("      <div class=\"brand-text\">\n");
        sb.append("        <h1>AutoRent <span>Pro</span></h1>\n");
        sb.append("        <p class=\"brand-subtitle\">Java OOP Vehicle Rental System</p>\n");
        sb.append("      </div>\n");
        sb.append("    </a>\n");

        sb.append("    <nav class=\"nav-links\">\n");
        sb.append("      <a href=\"/fleet\" class=\"nav-link ").append("fleet".equals(activeTab) ? "active" : "").append("\"><i class=\"fa-solid fa-layer-group\"></i> Fleet Explorer</a>\n");
        sb.append("      <a href=\"/rentals\" class=\"nav-link ").append("rentals".equals(activeTab) ? "active" : "").append("\"><i class=\"fa-solid fa-receipt\"></i> Active Rentals</a>\n");
        sb.append("      <a href=\"/customers\" class=\"nav-link ").append("customers".equals(activeTab) ? "active" : "").append("\"><i class=\"fa-solid fa-users\"></i> Customers</a>\n");
        sb.append("      <a href=\"/docs\" class=\"nav-link ").append("docs".equals(activeTab) ? "active" : "").append("\"><i class=\"fa-solid fa-book\"></i> System Docs</a>\n");
        sb.append("    </nav>\n");

        sb.append("    <div class=\"nav-actions\">\n");
        sb.append("      <a href=\"/add-customer\" class=\"btn btn-secondary\"><i class=\"fa-solid fa-user-plus\"></i> Add Customer</a>\n");
        sb.append("      <a href=\"/add-vehicle\" class=\"btn btn-primary\"><i class=\"fa-solid fa-plus\"></i> Add Vehicle</a>\n");
        sb.append("      <a href=\"/book\" class=\"btn btn-amber\"><i class=\"fa-solid fa-bolt\"></i> Book Now</a>\n");
        sb.append("    </div>\n");
        sb.append("  </div>\n");
        sb.append("</header>\n");

        // Main Content Container
        sb.append("<main class=\"main-content\">\n");

        // Flash message alert banner if present
        if (message != null && !message.trim().isEmpty()) {
            String alertClass = "success".equalsIgnoreCase(messageType) ? "alert-success" : ("error".equalsIgnoreCase(messageType) ? "alert-error" : "alert-info");
            String alertIcon = "success".equalsIgnoreCase(messageType) ? "fa-circle-check" : ("error".equalsIgnoreCase(messageType) ? "fa-triangle-exclamation" : "fa-circle-info");
            sb.append("  <div class=\"alert-banner ").append(alertClass).append("\">\n");
            sb.append("    <i class=\"fa-solid ").append(alertIcon).append("\"></i>\n");
            sb.append("    <span>").append(escapeHtml(message)).append("</span>\n");
            sb.append("  </div>\n");
        }

        sb.append(content);
        sb.append("</main>\n");

        // Footer
        sb.append("<footer class=\"footer\">\n");
        sb.append("  <div class=\"footer-container\">\n");
        sb.append("    <p>AutoRent Pro &copy; 2026 — Pure Java OOP Backend & SSR HTML/CSS</p>\n");
        sb.append("  </div>\n");
        sb.append("</footer>\n");

        sb.append("</body>\n</html>");
        return sb.toString();
    }

    public static String renderKpis(RentalAdmin admin) {
        int total = admin.getVehicles().size();
        int avail = 0;
        int rented = 0;
        for (Vehicle v : admin.getVehicles()) {
            if (v.isAvailable()) avail++;
            else rented++;
        }

        double totalRevenue = 0;
        double activeDeposits = 0;
        int activeRentals = 0;

        for (Rental r : admin.getRentals()) {
            if ("ACTIVE".equalsIgnoreCase(r.getStatus())) {
                activeRentals++;
                activeDeposits += r.getSecurityDeposit();
            }
            totalRevenue += r.getRentalCost() + r.getLatePenalty();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("<section class=\"kpi-grid\">\n");

        sb.append("  <div class=\"kpi-card glass-panel\">\n");
        sb.append("    <div class=\"kpi-icon icon-blue\"><i class=\"fa-solid fa-warehouse\"></i></div>\n");
        sb.append("    <div class=\"kpi-info\">\n");
        sb.append("      <span class=\"kpi-label\">Total Fleet</span>\n");
        sb.append("      <h3 class=\"kpi-val\">").append(total).append("</h3>\n");
        sb.append("      <span class=\"kpi-trend\">Registered Vehicles</span>\n");
        sb.append("    </div>\n");
        sb.append("  </div>\n");

        sb.append("  <div class=\"kpi-card glass-panel\">\n");
        sb.append("    <div class=\"kpi-icon icon-green\"><i class=\"fa-solid fa-circle-check\"></i></div>\n");
        sb.append("    <div class=\"kpi-info\">\n");
        sb.append("      <span class=\"kpi-label\">Available Now</span>\n");
        sb.append("      <h3 class=\"kpi-val text-green\">").append(avail).append("</h3>\n");
        sb.append("      <span class=\"kpi-trend\">Ready for Instant Rent</span>\n");
        sb.append("    </div>\n");
        sb.append("  </div>\n");

        sb.append("  <div class=\"kpi-card glass-panel\">\n");
        sb.append("    <div class=\"kpi-icon icon-amber\"><i class=\"fa-solid fa-key\"></i></div>\n");
        sb.append("    <div class=\"kpi-info\">\n");
        sb.append("      <span class=\"kpi-label\">Currently Rented</span>\n");
        sb.append("      <h3 class=\"kpi-val text-amber\">").append(rented).append("</h3>\n");
        sb.append("      <span class=\"kpi-trend\">Active On Road (").append(activeRentals).append(" bookings)</span>\n");
        sb.append("    </div>\n");
        sb.append("  </div>\n");

        sb.append("  <div class=\"kpi-card glass-panel\">\n");
        sb.append("    <div class=\"kpi-icon icon-purple\"><i class=\"fa-solid fa-shield-halved\"></i></div>\n");
        sb.append("    <div class=\"kpi-info\">\n");
        sb.append("      <span class=\"kpi-label\">Active Deposits</span>\n");
        sb.append("      <h3 class=\"kpi-val text-purple\">Rs. ").append(String.format("%,.0f", activeDeposits)).append("</h3>\n");
        sb.append("      <span class=\"kpi-trend\">Escrow Held</span>\n");
        sb.append("    </div>\n");
        sb.append("  </div>\n");

        sb.append("  <div class=\"kpi-card glass-panel\">\n");
        sb.append("    <div class=\"kpi-icon icon-emerald\"><i class=\"fa-solid fa-indian-rupee-sign\"></i></div>\n");
        sb.append("    <div class=\"kpi-info\">\n");
        sb.append("      <span class=\"kpi-label\">Total Revenue</span>\n");
        sb.append("      <h3 class=\"kpi-val text-emerald\">Rs. ").append(String.format("%,.0f", totalRevenue)).append("</h3>\n");
        sb.append("      <span class=\"kpi-trend\">Rentals & Penalties</span>\n");
        sb.append("    </div>\n");
        sb.append("  </div>\n");

        sb.append("</section>\n");
        return sb.toString();
    }

    public static String renderFleetPage(RentalAdmin admin, String searchType, String searchBrand, boolean onlyAvailable) {
        StringBuilder sb = new StringBuilder();
        sb.append(renderKpis(admin));

        // Filter / Search Bar (Pure HTML GET Form)
        sb.append("<div class=\"section-header-bar glass-panel\">\n");
        sb.append("  <form method=\"GET\" action=\"/fleet\" class=\"filter-group-form\">\n");
        sb.append("    <div class=\"search-input-wrapper\">\n");
        sb.append("      <i class=\"fa-solid fa-magnifying-glass search-icon\"></i>\n");
        sb.append("      <input type=\"text\" name=\"brand\" value=\"").append(searchBrand != null ? escapeHtml(searchBrand) : "").append("\" placeholder=\"Search by brand or model (e.g. Toyota, BMW)...\">\n");
        sb.append("    </div>\n");

        sb.append("    <div class=\"select-wrapper\">\n");
        sb.append("      <select name=\"type\">\n");
        sb.append("        <option value=\"all\" ").append("all".equalsIgnoreCase(searchType) || searchType == null ? "selected" : "").append(">All Vehicle Types</option>\n");
        sb.append("        <option value=\"Car\" ").append("Car".equalsIgnoreCase(searchType) ? "selected" : "").append(">Cars</option>\n");
        sb.append("        <option value=\"Bike\" ").append("Bike".equalsIgnoreCase(searchType) ? "selected" : "").append(">Bikes</option>\n");
        sb.append("        <option value=\"Van\" ").append("Van".equalsIgnoreCase(searchType) ? "selected" : "").append(">Vans</option>\n");
        sb.append("      </select>\n");
        sb.append("    </div>\n");

        sb.append("    <label class=\"checkbox-label\">\n");
        sb.append("      <input type=\"checkbox\" name=\"available\" value=\"true\" ").append(onlyAvailable ? "checked" : "").append(">\n");
        sb.append("      <span>Available Only</span>\n");
        sb.append("    </label>\n");

        sb.append("    <button type=\"submit\" class=\"btn btn-primary\"><i class=\"fa-solid fa-filter\"></i> Filter Fleet</button>\n");
        if ((searchBrand != null && !searchBrand.isEmpty()) || (searchType != null && !searchType.equalsIgnoreCase("all")) || onlyAvailable) {
            sb.append("    <a href=\"/fleet\" class=\"btn btn-outline\"><i class=\"fa-solid fa-xmark\"></i> Reset</a>\n");
        }
        sb.append("  </form>\n");
        sb.append("</div>\n");

        // Vehicle Grid
        List<Vehicle> list = admin.searchVehicles(searchType, searchBrand, onlyAvailable);

        sb.append("<div class=\"fleet-grid\">\n");
        if (list.isEmpty()) {
            sb.append("  <div class=\"empty-fleet-state glass-panel\">\n");
            sb.append("    <i class=\"fa-solid fa-car-rear\"></i>\n");
            sb.append("    <h3>No Vehicles Match Your Criteria</h3>\n");
            sb.append("    <p class=\"text-muted\">Try broadening your search query or reset the filters.</p>\n");
            sb.append("    <a href=\"/fleet\" class=\"btn btn-secondary\" style=\"margin-top:15px;\">View All Fleet</a>\n");
            sb.append("  </div>\n");
        } else {
            for (Vehicle v : list) {
                boolean isAvail = v.isAvailable();
                String statusClass = isAvail ? "status-available" : "status-rented";
                String statusText = isAvail ? "<i class=\"fa-solid fa-circle-check\"></i> Available" : "<i class=\"fa-solid fa-clock\"></i> On Rent";

                String typeIcon = "fa-car";
                if ("bike".equalsIgnoreCase(v.getType())) typeIcon = "fa-motorcycle";
                else if ("van".equalsIgnoreCase(v.getType())) typeIcon = "fa-van-shuttle";

                sb.append("  <div class=\"vehicle-card glass-panel\">\n");
                sb.append("    <div class=\"vehicle-image-container\">\n");
                sb.append("      <img src=\"").append(escapeHtml(v.getImageUrl())).append("\" alt=\"").append(escapeHtml(v.getBrand() + " " + v.getModel())).append("\" class=\"vehicle-image\">\n");
                sb.append("      <div class=\"vehicle-badges\">\n");
                sb.append("        <span class=\"type-pill\"><i class=\"fa-solid ").append(typeIcon).append("\"></i> ").append(escapeHtml(v.getType())).append("</span>\n");
                sb.append("        <span class=\"status-pill ").append(statusClass).append("\">").append(statusText).append("</span>\n");
                sb.append("      </div>\n");
                sb.append("    </div>\n");

                sb.append("    <div class=\"vehicle-content\">\n");
                sb.append("      <div class=\"vehicle-header\">\n");
                sb.append("        <div>\n");
                sb.append("          <span class=\"vehicle-id-tag\">VEHICLE #").append(v.getVehicleId()).append("</span>\n");
                sb.append("          <h3 class=\"vehicle-title\">").append(escapeHtml(v.getBrand())).append(" ").append(escapeHtml(v.getModel())).append("</h3>\n");
                sb.append("        </div>\n");
                sb.append("        <div class=\"vehicle-rate-badge\">\n");
                sb.append("          <span class=\"rate-amount\">Rs. ").append(String.format("%,.0f", v.getRentalRate())).append("</span>\n");
                sb.append("          <span class=\"rate-period\">/ day</span>\n");
                sb.append("        </div>\n");
                sb.append("      </div>\n");

                sb.append("      <div class=\"vehicle-features\">\n");
                sb.append("        <i class=\"fa-solid fa-sparkles text-blue\"></i>\n");
                sb.append("        <span>").append(escapeHtml(v.getFeatures())).append("</span>\n");
                sb.append("      </div>\n");

                sb.append("      <div class=\"vehicle-actions\">\n");
                if (isAvail) {
                    sb.append("        <a href=\"/book?vehicleId=").append(v.getVehicleId()).append("\" class=\"btn btn-primary btn-rent\"><i class=\"fa-solid fa-key\"></i> Rent Now</a>\n");
                } else {
                    sb.append("        <button class=\"btn btn-outline btn-rent\" disabled style=\"opacity: 0.6;\"><i class=\"fa-solid fa-lock\"></i> On Rent</button>\n");
                }
                sb.append("        <a href=\"/edit-vehicle?id=").append(v.getVehicleId()).append("\" class=\"btn btn-outline btn-sm\" title=\"Edit Details\"><i class=\"fa-solid fa-pen-to-square\"></i></a>\n");
                sb.append("        <form method=\"POST\" action=\"/delete-vehicle\" style=\"display:inline;\" onsubmit=\"return true;\">\n");
                sb.append("          <input type=\"hidden\" name=\"vehicleId\" value=\"").append(v.getVehicleId()).append("\">\n");
                sb.append("          <button type=\"submit\" class=\"btn btn-danger-outline btn-sm\" title=\"Delete Vehicle\" ").append(!isAvail ? "disabled style='opacity:0.4; cursor:not-allowed;'" : "").append("><i class=\"fa-solid fa-trash\"></i></button>\n");
                sb.append("        </form>\n");
                sb.append("      </div>\n");

                sb.append("    </div>\n");
                sb.append("  </div>\n");
            }
        }
        sb.append("</div>\n");

        return sb.toString();
    }

    public static String renderRentalsPage(RentalAdmin admin) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"section-card glass-panel\">\n");
        sb.append("  <div class=\"table-header-row\">\n");
        sb.append("    <div>\n");
        sb.append("      <h2><i class=\"fa-solid fa-file-contract text-blue\"></i> Rental Ledger & Return Processing</h2>\n");
        sb.append("      <p class=\"text-muted\">Manage active customer bookings, process vehicle returns, and settle late penalty calculations.</p>\n");
        sb.append("    </div>\n");
        sb.append("    <div>\n");
        sb.append("      <a href=\"/book\" class=\"btn btn-amber\"><i class=\"fa-solid fa-bolt\"></i> Book a Vehicle</a>\n");
        sb.append("    </div>\n");
        sb.append("  </div>\n");

        sb.append("  <div class=\"table-responsive\">\n");
        sb.append("    <table class=\"custom-table\">\n");
        sb.append("      <thead>\n");
        sb.append("        <tr>\n");
        sb.append("          <th>Rental ID</th>\n");
        sb.append("          <th>Customer</th>\n");
        sb.append("          <th>Vehicle Details</th>\n");
        sb.append("          <th>Rental Period</th>\n");
        sb.append("          <th>Rental Cost</th>\n");
        sb.append("          <th>Deposit Amount</th>\n");
        sb.append("          <th>Status</th>\n");
        sb.append("          <th>Settlement Details</th>\n");
        sb.append("          <th>Operation</th>\n");
        sb.append("        </tr>\n");
        sb.append("      </thead>\n");
        sb.append("      <tbody>\n");

        if (admin.getRentals().isEmpty()) {
            sb.append("        <tr><td colspan=\"9\" style=\"text-align:center; padding:30px;\" class=\"text-muted\">No rental transactions recorded yet.</td></tr>\n");
        } else {
            for (Rental r : admin.getRentals()) {
                boolean isActive = "ACTIVE".equalsIgnoreCase(r.getStatus());
                String statusBadge = isActive
                        ? "<span class=\"status-pill status-rented\" style=\"display:inline-flex; font-size:11px; padding:3px 8px;\"><i class=\"fa-solid fa-route\"></i> Active</span>"
                        : "<span class=\"status-pill status-available\" style=\"display:inline-flex; font-size:11px; padding:3px 8px;\"><i class=\"fa-solid fa-circle-check\"></i> Completed</span>";

                String settlementInfo;
                if (isActive) {
                    settlementInfo = "<span class=\"text-muted\">Rental In Progress</span>";
                } else {
                    settlementInfo = "<div><small class=\"text-danger\">Penalty: Rs. " + String.format("%,.0f", r.getLatePenalty()) + "</small></div>" +
                            "<div><small class=\"text-green\">Refunded: Rs. " + String.format("%,.0f", r.getRefundAmount()) + "</small></div>" +
                            "<div><small class=\"text-dim\">Returned: " + r.getActualReturnDate() + "</small></div>";
                }

                sb.append("        <tr>\n");
                sb.append("          <td><strong>#").append(r.getRentalId()).append("</strong></td>\n");
                sb.append("          <td><strong>").append(escapeHtml(r.getCustomer().getName())).append("</strong><div><small class=\"text-dim\">ID: ").append(r.getCustomer().getCustomerId()).append("</small></div></td>\n");
                sb.append("          <td><strong>").append(escapeHtml(r.getVehicle().getBrand() + " " + r.getVehicle().getModel())).append("</strong><div><small class=\"text-dim\">").append(r.getVehicle().getType()).append(" (#").append(r.getVehicle().getVehicleId()).append(")</small></div></td>\n");
                sb.append("          <td><div><small>From: <strong>").append(r.getStartDate()).append("</strong></small></div><div><small>To: <strong>").append(r.getEndDate()).append("</strong></small></div></td>\n");
                sb.append("          <td><strong class=\"text-emerald\">Rs. ").append(String.format("%,.0f", r.getRentalCost())).append("</strong></td>\n");
                sb.append("          <td><strong>Rs. ").append(String.format("%,.0f", r.getSecurityDeposit())).append("</strong></td>\n");
                sb.append("          <td>").append(statusBadge).append("</td>\n");
                sb.append("          <td>").append(settlementInfo).append("</td>\n");
                sb.append("          <td>\n");
                if (isActive) {
                    sb.append("            <a href=\"/return?rentalId=").append(r.getRentalId()).append("\" class=\"btn btn-amber btn-sm\"><i class=\"fa-solid fa-arrow-rotate-left\"></i> Process Return</a>\n");
                } else {
                    sb.append("            <span class=\"text-dim\"><i class=\"fa-solid fa-check-double\"></i> Settled</span>\n");
                }
                sb.append("          </td>\n");
                sb.append("        </tr>\n");
            }
        }

        sb.append("      </tbody>\n");
        sb.append("    </table>\n");
        sb.append("  </div>\n");
        sb.append("</div>\n");

        return sb.toString();
    }

    public static String renderBookingPage(RentalAdmin admin, int preselectedVehicleId) {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(3);

        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"form-card-container\">\n");
        sb.append("  <div class=\"form-card glass-panel\">\n");
        sb.append("    <div class=\"modal-header\">\n");
        sb.append("      <h2><i class=\"fa-solid fa-calendar-check text-blue\"></i> Book a Vehicle</h2>\n");
        sb.append("      <a href=\"/fleet\" class=\"btn btn-outline btn-sm\"><i class=\"fa-solid fa-arrow-left\"></i> Back to Fleet</a>\n");
        sb.append("    </div>\n");

        sb.append("    <form method=\"POST\" action=\"/book\">\n");
        sb.append("      <div class=\"form-grid\">\n");

        // Customer select
        sb.append("        <div class=\"form-group full-width\">\n");
        sb.append("          <label>Select Customer *</label>\n");
        sb.append("          <select name=\"customerId\" required>\n");
        sb.append("            <option value=\"\">-- Select Registered Customer --</option>\n");
        for (Customer c : admin.getCustomers()) {
            sb.append("            <option value=\"").append(c.getCustomerId()).append("\">#").append(c.getCustomerId()).append(" — ").append(escapeHtml(c.getName())).append(" (").append(escapeHtml(c.getContactDetails())).append(")</option>\n");
        }
        sb.append("          </select>\n");
        sb.append("        </div>\n");

        // Vehicle select
        sb.append("        <div class=\"form-group full-width\">\n");
        sb.append("          <label>Select Available Vehicle *</label>\n");
        sb.append("          <select name=\"vehicleId\" required>\n");
        sb.append("            <option value=\"\">-- Select Available Vehicle --</option>\n");
        for (Vehicle v : admin.getVehicles()) {
            if (v.isAvailable() || v.getVehicleId() == preselectedVehicleId) {
                boolean sel = (v.getVehicleId() == preselectedVehicleId);
                sb.append("            <option value=\"").append(v.getVehicleId()).append("\" ").append(sel ? "selected" : "").append(">[")
                        .append(v.getType()).append("] ").append(escapeHtml(v.getBrand())).append(" ").append(escapeHtml(v.getModel()))
                        .append(" — Rs. ").append(String.format("%,.0f", v.getRentalRate())).append("/day").append(v.isAvailable() ? "" : " (Currently Assigned)").append("</option>\n");
            }
        }
        sb.append("          </select>\n");
        sb.append("        </div>\n");

        // Start & End date
        sb.append("        <div class=\"form-group\">\n");
        sb.append("          <label>Rental Start Date *</label>\n");
        sb.append("          <input type=\"date\" name=\"startDate\" value=\"").append(today).append("\" required>\n");
        sb.append("        </div>\n");

        sb.append("        <div class=\"form-group\">\n");
        sb.append("          <label>Rental End Date *</label>\n");
        sb.append("          <input type=\"date\" name=\"endDate\" value=\"").append(tomorrow).append("\" required>\n");
        sb.append("        </div>\n");

        // Security deposit
        sb.append("        <div class=\"form-group full-width\">\n");
        sb.append("          <label>Security Deposit Amount (Rs.) *</label>\n");
        sb.append("          <input type=\"number\" name=\"deposit\" value=\"3000\" min=\"500\" step=\"100\" required>\n");
        sb.append("        </div>\n");

        sb.append("      </div>\n");

        // Policy notice
        sb.append("      <div class=\"cost-estimate-card\">\n");
        sb.append("        <h4><i class=\"fa-solid fa-circle-info\"></i> Pricing & Calculation Rules</h4>\n");
        sb.append("        <div class=\"cost-breakdown-row\"><span>Rental Cost:</span><strong>Daily Rate &times; Rental Days (min 1 day)</strong></div>\n");
        sb.append("        <div class=\"cost-breakdown-row\"><span>Late Return Penalty:</span><strong>Rs. 200 / day late if returned after End Date</strong></div>\n");
        sb.append("        <div class=\"cost-breakdown-row total-row\"><span>Deposit Escrow:</span><strong class=\"text-emerald\">Refunded upon vehicle return inspection</strong></div>\n");
        sb.append("      </div>\n");

        sb.append("      <div class=\"modal-footer\">\n");
        sb.append("        <a href=\"/fleet\" class=\"btn btn-outline\">Cancel</a>\n");
        sb.append("        <button type=\"submit\" class=\"btn btn-primary\"><i class=\"fa-solid fa-check\"></i> Confirm & Book Vehicle</button>\n");
        sb.append("      </div>\n");

        sb.append("    </form>\n");
        sb.append("  </div>\n");
        sb.append("</div>\n");

        return sb.toString();
    }

    public static String renderReturnPage(RentalAdmin admin, int rentalId) {
        Rental rental = admin.findRental(rentalId);
        if (rental == null) {
            return "<div class=\"alert-banner alert-error\">Rental transaction not found. <a href=\"/rentals\">Go back to Rentals</a></div>";
        }

        LocalDate today = LocalDate.now();

        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"form-card-container\">\n");
        sb.append("  <div class=\"form-card glass-panel\">\n");
        sb.append("    <div class=\"modal-header\">\n");
        sb.append("      <h2><i class=\"fa-solid fa-arrow-rotate-left text-amber\"></i> Process Vehicle Return</h2>\n");
        sb.append("      <a href=\"/rentals\" class=\"btn btn-outline btn-sm\"><i class=\"fa-solid fa-arrow-left\"></i> Back to Ledger</a>\n");
        sb.append("    </div>\n");

        sb.append("    <div class=\"rental-summary-box\">\n");
        sb.append("      <div class=\"summary-item\"><span class=\"label\">Rental ID:</span><span class=\"val\">#").append(rental.getRentalId()).append("</span></div>\n");
        sb.append("      <div class=\"summary-item\"><span class=\"label\">Customer:</span><span class=\"val\">").append(escapeHtml(rental.getCustomer().getName())).append("</span></div>\n");
        sb.append("      <div class=\"summary-item\"><span class=\"label\">Vehicle:</span><span class=\"val\">").append(escapeHtml(rental.getVehicle().getBrand() + " " + rental.getVehicle().getModel())).append("</span></div>\n");
        sb.append("      <div class=\"summary-item\"><span class=\"label\">Scheduled End Date:</span><span class=\"val text-blue\">").append(rental.getEndDate()).append("</span></div>\n");
        sb.append("      <div class=\"summary-item\"><span class=\"label\">Security Deposit Held:</span><span class=\"val text-purple\">Rs. ").append(String.format("%,.0f", rental.getSecurityDeposit())).append("</span></div>\n");
        sb.append("      <div class=\"summary-item\"><span class=\"label\">Rental Cost Paid:</span><span class=\"val text-emerald\">Rs. ").append(String.format("%,.0f", rental.getRentalCost())).append("</span></div>\n");
        sb.append("    </div>\n");

        sb.append("    <form method=\"POST\" action=\"/return\" style=\"margin-top:20px;\">\n");
        sb.append("      <input type=\"hidden\" name=\"rentalId\" value=\"").append(rental.getRentalId()).append("\">\n");

        sb.append("      <div class=\"form-group full-width\">\n");
        sb.append("        <label>Actual Vehicle Return Date *</label>\n");
        sb.append("        <input type=\"date\" name=\"returnDate\" value=\"").append(today).append("\" required>\n");
        sb.append("      </div>\n");

        sb.append("      <div class=\"cost-estimate-card\">\n");
        sb.append("        <h4><i class=\"fa-solid fa-calculator\"></i> Settlement Calculation Formula</h4>\n");
        sb.append("        <div class=\"cost-breakdown-row\"><span>Late Days:</span><strong>Days between Scheduled End and Actual Return (if after)</strong></div>\n");
        sb.append("        <div class=\"cost-breakdown-row\"><span>Late Penalty:</span><strong class=\"text-danger\">Late Days &times; Rs. 200</strong></div>\n");
        sb.append("        <div class=\"cost-breakdown-row total-row\"><span>Customer Refund:</span><strong class=\"text-green\">max(0, Security Deposit - Late Penalty)</strong></div>\n");
        sb.append("      </div>\n");

        sb.append("      <div class=\"modal-footer\">\n");
        sb.append("        <a href=\"/rentals\" class=\"btn btn-outline\">Cancel</a>\n");
        sb.append("        <button type=\"submit\" class=\"btn btn-amber\"><i class=\"fa-solid fa-circle-check\"></i> Complete Return & Calculate Refund</button>\n");
        sb.append("      </div>\n");
        sb.append("    </form>\n");

        sb.append("  </div>\n");
        sb.append("</div>\n");

        return sb.toString();
    }

    public static String renderAddOrEditVehiclePage(Vehicle editVehicle) {
        boolean isEdit = (editVehicle != null);
        String title = isEdit ? "Edit Vehicle #" + editVehicle.getVehicleId() : "Add New Vehicle";

        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"form-card-container\">\n");
        sb.append("  <div class=\"form-card glass-panel\">\n");
        sb.append("    <div class=\"modal-header\">\n");
        sb.append("      <h2><i class=\"fa-solid fa-car text-blue\"></i> ").append(title).append("</h2>\n");
        sb.append("      <a href=\"/fleet\" class=\"btn btn-outline btn-sm\"><i class=\"fa-solid fa-arrow-left\"></i> Back to Fleet</a>\n");
        sb.append("    </div>\n");

        sb.append("    <form method=\"POST\" action=\"").append(isEdit ? "/edit-vehicle" : "/add-vehicle").append("\">\n");
        if (isEdit) {
            sb.append("      <input type=\"hidden\" name=\"vehicleId\" value=\"").append(editVehicle.getVehicleId()).append("\">\n");
        }

        sb.append("      <div class=\"form-grid\">\n");
        sb.append("        <div class=\"form-group\">\n");
        sb.append("          <label>Vehicle Type *</label>\n");
        sb.append("          <select name=\"type\" required>\n");
        String curType = isEdit ? editVehicle.getType() : "Car";
        sb.append("            <option value=\"Car\" ").append("Car".equalsIgnoreCase(curType) ? "selected" : "").append(">Car (4-Wheeler)</option>\n");
        sb.append("            <option value=\"Bike\" ").append("Bike".equalsIgnoreCase(curType) ? "selected" : "").append(">Bike (2-Wheeler)</option>\n");
        sb.append("            <option value=\"Van\" ").append("Van".equalsIgnoreCase(curType) ? "selected" : "").append(">Van (Passenger/Cargo)</option>\n");
        sb.append("          </select>\n");
        sb.append("        </div>\n");

        sb.append("        <div class=\"form-group\">\n");
        sb.append("          <label>Brand / Manufacturer *</label>\n");
        sb.append("          <input type=\"text\" name=\"brand\" value=\"").append(isEdit ? escapeHtml(editVehicle.getBrand()) : "").append("\" placeholder=\"e.g. Toyota, BMW, Royal Enfield\" required>\n");
        sb.append("        </div>\n");

        sb.append("        <div class=\"form-group\">\n");
        sb.append("          <label>Model Name *</label>\n");
        sb.append("          <input type=\"text\" name=\"model\" value=\"").append(isEdit ? escapeHtml(editVehicle.getModel()) : "").append("\" placeholder=\"e.g. Camry, 3 Series, Hunter 350\" required>\n");
        sb.append("        </div>\n");

        sb.append("        <div class=\"form-group\">\n");
        sb.append("          <label>Daily Rental Rate (Rs.) *</label>\n");
        sb.append("          <input type=\"number\" name=\"rentalRate\" value=\"").append(isEdit ? (int)editVehicle.getRentalRate() : "2500").append("\" min=\"100\" step=\"50\" required>\n");
        sb.append("        </div>\n");

        sb.append("        <div class=\"form-group full-width\">\n");
        sb.append("          <label>Features & Specifications</label>\n");
        sb.append("          <input type=\"text\" name=\"features\" value=\"").append(isEdit ? escapeHtml(editVehicle.getFeatures()) : "Automatic, 5-Seater, AC, Bluetooth").append("\" placeholder=\"e.g. 5-Seater, Petrol, AC, Navigation\">\n");
        sb.append("        </div>\n");

        sb.append("        <div class=\"form-group full-width\">\n");
        sb.append("          <label>Image URL (Optional)</label>\n");
        sb.append("          <input type=\"url\" name=\"imageUrl\" value=\"").append(isEdit ? escapeHtml(editVehicle.getImageUrl()) : "").append("\" placeholder=\"https://images.unsplash.com/...\">\n");
        sb.append("        </div>\n");

        sb.append("      </div>\n");

        sb.append("      <div class=\"modal-footer\">\n");
        sb.append("        <a href=\"/fleet\" class=\"btn btn-outline\">Cancel</a>\n");
        sb.append("        <button type=\"submit\" class=\"btn btn-primary\"><i class=\"fa-solid fa-save\"></i> ").append(isEdit ? "Update Vehicle" : "Save Vehicle to Fleet").append("</button>\n");
        sb.append("      </div>\n");
        sb.append("    </form>\n");

        sb.append("  </div>\n");
        sb.append("</div>\n");

        return sb.toString();
    }

    public static String renderCustomersPage(RentalAdmin admin, Integer viewCustomerId) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"section-card glass-panel\">\n");
        sb.append("  <div class=\"table-header-row\">\n");
        sb.append("    <div>\n");
        sb.append("      <h2><i class=\"fa-solid fa-users text-purple\"></i> Registered Customers Directory</h2>\n");
        sb.append("      <p class=\"text-muted\">Manage customer profiles, contact info, driving licenses, and rental histories.</p>\n");
        sb.append("    </div>\n");
        sb.append("    <div>\n");
        sb.append("      <a href=\"/add-customer\" class=\"btn btn-secondary\"><i class=\"fa-solid fa-user-plus\"></i> Register Customer</a>\n");
        sb.append("    </div>\n");
        sb.append("  </div>\n");

        sb.append("  <div class=\"table-responsive\">\n");
        sb.append("    <table class=\"custom-table\">\n");
        sb.append("      <thead>\n");
        sb.append("        <tr>\n");
        sb.append("          <th>ID</th>\n");
        sb.append("          <th>Customer Name</th>\n");
        sb.append("          <th>Phone Number</th>\n");
        sb.append("          <th>Email Address</th>\n");
        sb.append("          <th>Driver's License</th>\n");
        sb.append("          <th>Total Bookings</th>\n");
        sb.append("          <th>Actions</th>\n");
        sb.append("        </tr>\n");
        sb.append("      </thead>\n");
        sb.append("      <tbody>\n");

        for (Customer c : admin.getCustomers()) {
            sb.append("        <tr>\n");
            sb.append("          <td><strong>#").append(c.getCustomerId()).append("</strong></td>\n");
            sb.append("          <td><strong>").append(escapeHtml(c.getName())).append("</strong></td>\n");
            sb.append("          <td>").append(escapeHtml(c.getContactDetails())).append("</td>\n");
            sb.append("          <td><span class=\"text-dim\">").append(escapeHtml(c.getEmail())).append("</span></td>\n");
            sb.append("          <td><code>").append(escapeHtml(c.getLicenseNumber())).append("</code></td>\n");
            sb.append("          <td><span class=\"badge-pill\" style=\"background:rgba(99,102,241,0.2); color:#818cf8;\">").append(c.getRentalHistory().size()).append(" Bookings</span></td>\n");
            sb.append("          <td>\n");
            sb.append("            <a href=\"/customers?id=").append(c.getCustomerId()).append("\" class=\"btn btn-outline btn-sm\"><i class=\"fa-solid fa-clock-rotate-left\"></i> View History</a>\n");
            sb.append("          </td>\n");
            sb.append("        </tr>\n");
        }

        sb.append("      </tbody>\n");
        sb.append("    </table>\n");
        sb.append("  </div>\n");
        sb.append("</div>\n");

        // If viewCustomerId is provided, render rental history below
        if (viewCustomerId != null) {
            Customer sel = admin.findCustomer(viewCustomerId);
            if (sel != null) {
                sb.append("<div class=\"section-card glass-panel\" style=\"margin-top:24px;\">\n");
                sb.append("  <div class=\"table-header-row\">\n");
                sb.append("    <div>\n");
                sb.append("      <h3><i class=\"fa-solid fa-history text-blue\"></i> Rental History for ").append(escapeHtml(sel.getName())).append(" (#").append(sel.getCustomerId()).append(")</h3>\n");
                sb.append("      <p class=\"text-muted\">Phone: ").append(escapeHtml(sel.getContactDetails())).append(" | License: ").append(escapeHtml(sel.getLicenseNumber())).append("</p>\n");
                sb.append("    </div>\n");
                sb.append("    <a href=\"/customers\" class=\"btn btn-outline btn-sm\"><i class=\"fa-solid fa-xmark\"></i> Close History</a>\n");
                sb.append("  </div>\n");

                sb.append("  <div class=\"table-responsive\">\n");
                sb.append("    <table class=\"custom-table\">\n");
                sb.append("      <thead>\n");
                sb.append("        <tr><th>Rental ID</th><th>Vehicle</th><th>Dates</th><th>Rental Cost</th><th>Deposit</th><th>Late Penalty</th><th>Refund</th><th>Status</th></tr>\n");
                sb.append("      </thead>\n");
                sb.append("      <tbody>\n");

                if (sel.getRentalHistory().isEmpty()) {
                    sb.append("        <tr><td colspan=\"8\" style=\"text-align:center; padding:20px;\" class=\"text-muted\">No rental bookings recorded for this customer yet.</td></tr>\n");
                } else {
                    for (Rental rh : sel.getRentalHistory()) {
                        sb.append("        <tr>\n");
                        sb.append("          <td><strong>#").append(rh.getRentalId()).append("</strong></td>\n");
                        sb.append("          <td>").append(escapeHtml(rh.getVehicle().getBrand() + " " + rh.getVehicle().getModel())).append(" (").append(rh.getVehicle().getType()).append(")</td>\n");
                        sb.append("          <td>").append(rh.getStartDate()).append(" &rarr; ").append(rh.getEndDate()).append("</td>\n");
                        sb.append("          <td><strong class=\"text-emerald\">Rs. ").append(String.format("%,.0f", rh.getRentalCost())).append("</strong></td>\n");
                        sb.append("          <td>Rs. ").append(String.format("%,.0f", rh.getSecurityDeposit())).append("</td>\n");
                        sb.append("          <td><span class=\"text-danger\">Rs. ").append(String.format("%,.0f", rh.getLatePenalty())).append("</span></td>\n");
                        sb.append("          <td><span class=\"text-green\">Rs. ").append(String.format("%,.0f", rh.getRefundAmount())).append("</span></td>\n");
                        sb.append("          <td><span class=\"status-pill ").append("ACTIVE".equalsIgnoreCase(rh.getStatus()) ? "status-rented" : "status-available").append("\" style=\"font-size:10.5px; padding:2px 6px;\">").append(rh.getStatus()).append("</span></td>\n");
                        sb.append("        </tr>\n");
                    }
                }

                sb.append("      </tbody>\n");
                sb.append("    </table>\n");
                sb.append("  </div>\n");
                sb.append("</div>\n");
            }
        }

        return sb.toString();
    }

    public static String renderAddCustomerPage() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"form-card-container\">\n");
        sb.append("  <div class=\"form-card glass-panel\">\n");
        sb.append("    <div class=\"modal-header\">\n");
        sb.append("      <h2><i class=\"fa-solid fa-user-plus text-purple\"></i> Register New Customer</h2>\n");
        sb.append("      <a href=\"/customers\" class=\"btn btn-outline btn-sm\"><i class=\"fa-solid fa-arrow-left\"></i> Back to Customers</a>\n");
        sb.append("    </div>\n");

        sb.append("    <form method=\"POST\" action=\"/add-customer\">\n");
        sb.append("      <div class=\"form-grid\">\n");
        sb.append("        <div class=\"form-group full-width\">\n");
        sb.append("          <label>Customer Full Name *</label>\n");
        sb.append("          <input type=\"text\" name=\"name\" placeholder=\"e.g. Vikramaditya Sharma\" required>\n");
        sb.append("        </div>\n");

        sb.append("        <div class=\"form-group\">\n");
        sb.append("          <label>Phone Number *</label>\n");
        sb.append("          <input type=\"text\" name=\"contactDetails\" placeholder=\"+91 98765 43210\" required>\n");
        sb.append("        </div>\n");

        sb.append("        <div class=\"form-group\">\n");
        sb.append("          <label>Email Address</label>\n");
        sb.append("          <input type=\"email\" name=\"email\" placeholder=\"customer@example.com\">\n");
        sb.append("        </div>\n");

        sb.append("        <div class=\"form-group full-width\">\n");
        sb.append("          <label>Driver's License Number</label>\n");
        sb.append("          <input type=\"text\" name=\"licenseNumber\" placeholder=\"e.g. DL-0420240987\">\n");
        sb.append("        </div>\n");
        sb.append("      </div>\n");

        sb.append("      <div class=\"modal-footer\">\n");
        sb.append("        <a href=\"/customers\" class=\"btn btn-outline\">Cancel</a>\n");
        sb.append("        <button type=\"submit\" class=\"btn btn-secondary\"><i class=\"fa-solid fa-user-check\"></i> Register Customer</button>\n");
        sb.append("      </div>\n");
        sb.append("    </form>\n");

        sb.append("  </div>\n");
        sb.append("</div>\n");

        return sb.toString();
    }

    public static String renderDocsPage() {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class=\"docs-grid\">\n");

        sb.append("  <div class=\"doc-card glass-panel\">\n");
        sb.append("    <h3><i class=\"fa-solid fa-diagram-project text-emerald\"></i> Object-Oriented Java Architecture</h3>\n");
        sb.append("    <p>This project is built using <strong>Object-Oriented Programming (OOP) in Java</strong> with zero JavaScript:</p>\n");
        sb.append("    <ul class=\"doc-list\">\n");
        sb.append("      <li><strong>Interface Implementation:</strong> <code>Rentable</code> interface specifies the rental contract (<code>calculateRentalCost()</code>, <code>isAvailable()</code>, <code>setAvailable()</code>).</li>\n");
        sb.append("      <li><strong>Inheritance & Polymorphism:</strong> Specialized subclasses <code>Car</code>, <code>Bike</code>, and <code>Van</code> extend the base <code>Vehicle</code> class.</li>\n");
        sb.append("      <li><strong>Encapsulation:</strong> All model fields are private, mutated via getters, setters, and business methods.</li>\n");
        sb.append("      <li><strong>Aggregation:</strong> <code>Rental</code> manages the lifecycle between <code>Customer</code> and <code>Vehicle</code>.</li>\n");
        sb.append("      <li><strong>Pure Java Server-Side Rendering:</strong> Zero JavaScript; HTTP forms and query parameters drive all interactions.</li>\n");
        sb.append("    </ul>\n");
        sb.append("  </div>\n");

        sb.append("  <div class=\"doc-card glass-panel\">\n");
        sb.append("    <h3><i class=\"fa-solid fa-calculator text-blue\"></i> Business Rules & Calculations</h3>\n");
        sb.append("    <ul class=\"doc-list\">\n");
        sb.append("      <li><strong>Rental Cost:</strong> <code>Daily Rate &times; max(1, Days between Start and End Date)</code></li>\n");
        sb.append("      <li><strong>Security Deposit:</strong> Collected upfront at booking and tracked in the transaction.</li>\n");
        sb.append("      <li><strong>Late Return Penalty:</strong> <code>Late Days &times; Rs. 200</code> (if returned after scheduled end date).</li>\n");
        sb.append("      <li><strong>Deposit Refund Amount:</strong> <code>max(0, Security Deposit - Late Penalty)</code></li>\n");
        sb.append("      <li><strong>Real-time Status Tracking:</strong> Automatic availability toggling upon booking and returns.</li>\n");
        sb.append("    </ul>\n");
        sb.append("  </div>\n");

        sb.append("</div>\n");
        return sb.toString();
    }

    private static String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
