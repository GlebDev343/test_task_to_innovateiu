package to.innovateiu.test_task;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static to.innovateiu.test_task.DocumentManager.Author;
import static to.innovateiu.test_task.DocumentManager.Document;
import static to.innovateiu.test_task.DocumentManager.SearchRequest;
import static org.junit.jupiter.api.Assertions.*;

class DocumentManagerTest {
    private DocumentManager manager;
    private Document doc1;
    private Document doc2;
    private Document doc3;
    private Author author1;
    private Author author2;

    @BeforeEach
    void setUp() {
        manager = new DocumentManager();

        author1 = Author.builder().id("auth1").name("John Doe").build();
        author2 = Author.builder().id("auth2").name("Jane Smith").build();

        doc1 = manager.save(Document.builder()
                .title("Oak Tree Facts")
                .content("A sturdy tree with broad leaves and strong wood")
                .author(author1)
                .created(Instant.now().minusSeconds(7200))
                .build());

        doc2 = manager.save(Document.builder()
                .title("Pine Tree Guide")
                .content("Evergreen tree with needle-like leaves")
                .author(author2)
                .created(Instant.now().minusSeconds(3600))
                .build());

        doc3 = manager.save(Document.builder()
                .title("Oak Woodland")
                .content("Forests dominated by oak trees")
                .author(author1)
                .created(Instant.now())
                .build());
    }

    @Test
    void testSaveDocumentGeneratesId() {
        Document newDoc = Document.builder()
                .title("Maple Tree Notes")
                .content("Known for vibrant autumn colors")
                .author(author1)
                .created(Instant.now())
                .build();
        Document saved = manager.save(newDoc);
        assertNotNull(saved.getId(), "Saved document should have a non-null ID");
    }

    @Test
    void testFindByExistingId() {
        Optional<Document> found = manager.findById(doc1.getId());
        assertTrue(found.isPresent(), "Document should be found");
        assertEquals("Oak Tree Facts", found.get().getTitle(), "Found document should have correct title");
    }

    @Test
    void testFindByNonExistingId() {
        Optional<Document> found = manager.findById("non-existing-id");
        assertFalse(found.isPresent(), "Non-existing ID should return empty Optional");
    }

    @Test
    void testSearchByTitlePrefix() {
        SearchRequest request = SearchRequest.builder()
                .titlePrefixes(List.of("Oak"))
                .build();
        List<Document> results = manager.search(request);
        assertEquals(2, results.size(), "Should find 2 documents with 'Oak' prefix");
        assertTrue(results.stream().allMatch(doc -> doc.getTitle().startsWith("Oak")),
                "All results should have 'Oak' prefix");
    }

    @Test
    void testSearchByContent() {
        SearchRequest request = SearchRequest.builder()
                .containsContents(List.of("leaves"))
                .build();
        List<Document> results = manager.search(request);
        assertEquals(2, results.size(), "Should find 2 documents containing 'leaves'");
        assertTrue(results.stream().allMatch(doc -> doc.getContent().contains("leaves")),
                "All results should contain 'leaves'");
    }

    @Test
    void testSearchByAuthorId() {
        SearchRequest request = SearchRequest.builder()
                .authorIds(List.of("auth1"))
                .build();
        List<Document> results = manager.search(request);
        assertEquals(2, results.size(), "Should find 2 documents by author1");
        assertTrue(results.stream().allMatch(doc -> "auth1".equals(doc.getAuthor().getId())),
                "All results should be by author1");
    }

    @Test
    void testSearchByTimeRange() {
        SearchRequest request = SearchRequest.builder()
                .createdFrom(Instant.now().minusSeconds(5000))
                .createdTo(Instant.now().plusSeconds(1000))
                .build();
        List<Document> results = manager.search(request);
        assertEquals(2, results.size(), "Should find 2 documents in time range");
    }

    @Test
    void testSearchWithMultipleCriteria() {
        SearchRequest request = SearchRequest.builder()
                .titlePrefixes(List.of("Oak"))
                .containsContents(List.of("tree"))
                .authorIds(List.of("auth1"))
                .build();
        List<Document> results = manager.search(request);
        assertEquals(2, results.size(), "Should find 2 documents matching all criteria");
        assertTrue(results.stream().allMatch(doc -> doc.getTitle().startsWith("Oak") &&
                        doc.getContent().contains("tree") &&
                        "auth1".equals(doc.getAuthor().getId())),
                "All results should match all criteria");
    }
}
