package com.rednavis.vaadin.service;

import static com.rednavis.shared.util.RestUrlUtils.BOOK_URL_COUNT;
import static com.rednavis.shared.util.RestUrlUtils.BOOK_URL_FINDALL;

import com.rednavis.shared.dto.book.Book;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {

  private final RestService restService;

  //public Book insert(Book book) {
  //  String url = restService.createBookUrl(BOOK_URL_INSERT);
  //  return restService.post(url, book, Book.class);
  //}

  //public Book save(Book book) {
  //  String url = restService.createBookUrl(BOOK_URL_SAVE);
  //  return restService.post(url, book, Book.class);
  //}

  public List<Book> findAll(int page, int size) {
    String url = restService.createBookUrl(BOOK_URL_FINDALL);
    try {
      URI builder = new URIBuilder(url)
          .addParameter("page", String.valueOf(page))
          .addParameter("size", String.valueOf(size))
          .build();
      log.info("findAll uri [url: {}]", builder.toString());
      return restService.get(builder.toString(), new ParameterizedTypeReference<>() {
      });
    } catch (URISyntaxException e) {
      log.error("Error parse uri [url: {}]", url);
      return Collections.emptyList();
    }
  }

  public long count() {
    String url = restService.createBookUrl(BOOK_URL_COUNT);
    return restService.get(url, Long.class);
  }

  //public void delete(Book book) {
  //  String url = restService.createBookUrl(BOOK_URL_DELETE);
  //  restService.post(url, book, String.class);
  //}
}
