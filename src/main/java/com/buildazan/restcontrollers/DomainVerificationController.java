package com.buildazan.restcontrollers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.CNAMERecord;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.SimpleResolver;
import org.xbill.DNS.Type;

@RestController
public class DomainVerificationController {

    private static final String TARGET_DOMAIN = "revboost.shop"; // Target CNAME domain for comparison

    @GetMapping("/verify-domain")
    public ResponseEntity<?> verifyDomainCname(@RequestParam String domain) {
        System.out.println("Verifying domain: " + domain);

        if (domain == null || domain.isEmpty()) {
            return ResponseEntity.badRequest().body("Domain is required.");
        }

        try {
            // Perform CNAME and A record verification
            boolean isVerified = isValidDomain(domain);
            if (isVerified) {
                return ResponseEntity.ok("domain is valid"); // 200 OK when the domain is valid
            } else {
                return ResponseEntity.status(400).body("CNAME or A record not found or incorrect.");
            }
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            return ResponseEntity.status(500).body("An unexpected error occurred on the server.");
        }
    }

    private boolean isValidDomain(String domain) throws Exception {
        // First, check if the domain has A record
        if (hasARecord(domain)) {
            return true; // Domain resolves to an IP (A Record)
        }

        // Otherwise, check for CNAME record
        return hasCNAME(domain);
    }

    private boolean hasARecord(String domain) throws Exception {
        Lookup lookup = new Lookup(domain, Type.A);
        lookup.setResolver(new SimpleResolver("8.8.8.8")); // Use Google's DNS resolver
        Record[] records = lookup.run();

        if (records == null || records.length == 0) {
            return false; // No A record found
        }

        for (Record record : records) {
            if (record instanceof ARecord) {
                // You can further validate the IP address if needed
                return true; // A record exists
            }
        }
        return false;
    }

    private boolean hasCNAME(String domain) throws Exception {
        Lookup lookup = new Lookup(domain, Type.CNAME);
        lookup.setResolver(new SimpleResolver("8.8.8.8")); // Use Google's DNS resolver
        Record[] records = lookup.run();

        if (records == null || records.length == 0) {
            return false; // No CNAME record found
        }

        // Loop through the records and check if any CNAME record points to 'revboost.shop'
        for (Record record : records) {
            if (record instanceof CNAMERecord) {
                CNAMERecord cname = (CNAMERecord) record;
                if (cname.getTarget().toString().equals(TARGET_DOMAIN + ".")) {
                    return true;
                }
            }
        }
        return false;
    }
}
