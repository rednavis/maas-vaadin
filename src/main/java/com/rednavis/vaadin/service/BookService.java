package com.rednavis.vaadin.service;

import static com.rednavis.shared.util.RestUrlUtils.BOOK_URL_COUNT;
import static com.rednavis.shared.util.RestUrlUtils.BOOK_URL_DELETE;
import static com.rednavis.shared.util.RestUrlUtils.BOOK_URL_FINDALL;
import static com.rednavis.shared.util.RestUrlUtils.BOOK_URL_INSERT;
import static com.rednavis.shared.util.RestUrlUtils.BOOK_URL_SAVE;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import com.rednavis.shared.dto.book.Book;
import com.rednavis.vaadin.property.MaasProperty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

  private final MaasProperty maasProperty;
  private final RestService restService;

  /**
   * insert.
   *
   * @param book book
   * @return
   */
  public Book insert(Book book) {
    String url = maasProperty.createBookUrl(BOOK_URL_INSERT);
    return restService.post(url, book, Book.class);
  }

  /**
   * save.
   *
   * @param book book
   * @return
   */
  public Book save(Book book) {
    String url = maasProperty.createBookUrl(BOOK_URL_SAVE);
    return restService.put(url, book, Book.class);
  }

  /**
   * findAll.
   *
   * @param limit  limit
   * @param offset offset
   * @return
   */
  public List<Book> findAll(int limit, int offset) {
    String url = maasProperty.createBookUrl(BOOK_URL_FINDALL);
    try {
      URI builder = new URIBuilder(url)
          .addParameter("limit", String.valueOf(limit))
          .addParameter("offset", String.valueOf(offset))
          .build();
      log.info("findAll uri [url: {}]", builder.toString());
      return restService.get(builder.toString(), new ParameterizedTypeReference<>() {
      });
    } catch (URISyntaxException e) {
      log.error("Error parse uri [url: {}]", url);
      return Collections.emptyList();
    }
  }

  /**
   * count.
   *
   * @return
   */
  public long count() {
    String url = maasProperty.createBookUrl(BOOK_URL_COUNT);
    return restService.get(url, Long.class);
  }

  /**
   * delete.
   *
   * @param bookId bookId
   */
  public void delete(String bookId) {
    String url = maasProperty.createBookUrl(BOOK_URL_DELETE);
    try {
      URI builder = new URIBuilder(url)
          .addParameter("bookId", bookId)
          .build();
      restService.delete(builder.toString(), Void.class);
    } catch (URISyntaxException e) {
      log.error("Error parse uri [url: {}]", url);
    }
  }
}
