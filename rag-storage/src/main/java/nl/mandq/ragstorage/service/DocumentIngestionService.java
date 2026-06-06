package nl.mandq.ragstorage.service;


import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentIngestionService {
    private final VectorStore vectorStore;
    public DocumentIngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }
    public void ingestPdf(Resource pdfResource) {
        var reader = new PagePdfDocumentReader(
                pdfResource,
                PdfDocumentReaderConfig.builder()
                        .withPagesPerDocument(1)
                        .build()
        );

        List<Document> pages = reader.get();

        var splitter = TokenTextSplitter.builder()
                .withChunkSize(800)
                .withMinChunkSizeChars(400)
                .withMinChunkLengthToEmbed(10)
                .withMaxNumChunks(10_000)
                .withKeepSeparator(true)
                .build();
        List<Document> chunks = splitter.apply(pages);

        vectorStore.add(chunks);

    }
}
