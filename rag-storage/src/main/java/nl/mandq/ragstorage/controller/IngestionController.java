package nl.mandq.ragstorage.controller;

import nl.mandq.ragstorage.service.DocumentIngestionService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IngestionController {
    private final DocumentIngestionService ingestionService;
    public IngestionController(DocumentIngestionService ingestionService) {
        this.ingestionService = ingestionService;
    }
    @PostMapping("/ingest")
    public String ingest() {
        var resource = new ClassPathResource("/doc/Get_Started_With_Smallpdf.pdf");
        ingestionService.ingestPdf(resource);
        return "Ingestion complete.";
    }
}
