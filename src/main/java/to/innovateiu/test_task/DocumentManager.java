package to.innovateiu.test_task;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * For implement this task focus on clear code, and make this solution as simple readable as possible
 * Don't worry about performance, concurrency, etc.
 * You can use in Memory collection for sore data
 * <p>
 * Please, don't change class name, and signature for methods save, search, findById
 * Implementations should be in a single class
 * This class could be auto tested
 */
public class DocumentManager {
    private final Map<String, Document> storage = new HashMap<>();

    /**
     * Implementation of this method should upsert the document to your storage
     * And generate unique id if it does not exist, don't change [created] field
     *
     * @param document - document content and author data
     * @return saved document
     */
    public Document save(Document document) {
        if (document.getId() == null) {
            String newId = UUID.randomUUID().toString();
            document = Document.builder()
                    .id(newId)
                    .title(document.getTitle())
                    .content(document.getContent())
                    .author(document.getAuthor())
                    .created(document.getCreated())
                    .build();
        }

        storage.put(document.getId(), document);
        return document;
    }

    /**
     * Implementation this method should find documents which match with request
     *
     * @param request - search request, each field could be null
     * @return list matched documents
     */
    public List<Document> search(SearchRequest request) {
        return storage.values().stream()
                .filter(doc -> matchesSearchRequest(doc, request))
                .collect(Collectors.toList());
    }

    /**
     * Implementation this method should find document by id
     *
     * @param id - document id
     * @return optional document
     */
    public Optional<Document> findById(String id) {
        return Optional.of(storage.get(id));
    }

    private boolean matchesSearchRequest(Document doc, SearchRequest request) {
        if (request.titlePrefixes != null && !request.titlePrefixes.isEmpty()) {
            boolean titleMatch = request.getTitlePrefixes().stream()
                    .anyMatch(prefix -> doc.getTitle().startsWith(prefix));
            if (!titleMatch) return false;
        }

        if (request.containsContents != null && !request.containsContents.isEmpty()) {
            boolean contentMatch = request.getContainsContents().stream()
                    .anyMatch(content -> doc.getContent() != null && doc.getContent().contains(content));
            if (!contentMatch) return false;
        }

        if (request.getAuthorIds() != null && !request.getAuthorIds().isEmpty()) {
            boolean authorMatch = doc.getAuthor() != null &&
                    request.getAuthorIds().contains(doc.getAuthor().getId());
            if (!authorMatch) return false;
        }

        if (request.getCreatedFrom() != null) {
            if (doc.getCreated() != null || doc.getCreated().isBefore(request.getCreatedFrom())) {
                return false;
            }
        }

        if (request.getCreatedTo() != null) {
            if (doc.getCreated() != null || doc.getCreated().isAfter(request.getCreatedTo())) {
                return false;
            }
        }

        return true;
    }

    @Data
    @Builder
    public static class SearchRequest {
        private List<String> titlePrefixes;
        private List<String> containsContents;
        private List<String> authorIds;
        private Instant createdFrom;
        private Instant createdTo;
    }

    @Data
    @Builder
    public static class Document {
        private String id;
        private String title;
        private String content;
        private Author author;
        private Instant created;
    }

    @Data
    @Builder
    public static class Author {
        private String id;
        private String name;
    }

}