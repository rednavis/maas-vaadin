package com.rednavis.vaadin.view.book;

import static com.rednavis.shared.util.RoleUtils.ROLE_USER;
import static com.rednavis.vaadin.util.ConstantUtils.PAGE_BOOK_CAPTION;
import static com.rednavis.vaadin.util.ConstantUtils.PAGE_BOOK_TITLE;
import static com.rednavis.vaadin.util.ConstantUtils.PAGE_BOOK_URL;

import com.github.appreciated.app.layout.annotations.Caption;
import com.github.appreciated.app.layout.annotations.Icon;
import com.rednavis.shared.dto.book.Book;
import com.rednavis.vaadin.annotation.AccessToken;
import com.rednavis.vaadin.service.BookService;
import com.rednavis.vaadin.view.MainView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.crudui.crud.LazyCrudListener;
import org.vaadin.crudui.crud.impl.GridCrud;

@Slf4j
@Secured({ROLE_USER})
@PageTitle(PAGE_BOOK_TITLE)
@Caption(PAGE_BOOK_CAPTION)
@Route(value = PAGE_BOOK_URL, layout = MainView.class)
@Icon(VaadinIcon.BOOK)
@Tag("book-view")
@JsModule("./src/view/book/book-view.js")
public class BookView extends PolymerTemplate<TemplateModel> {

  private final transient AccessToken accessToken;
  private final transient BookService bookService;

  public BookView(AccessToken accessToken, BookService bookService) {
    this.accessToken = accessToken;
    this.bookService = bookService;

    prepareUI();
  }

  private void prepareUI() {
    GridCrud<Book> crud = new GridCrud<>(Book.class);
    Grid<Book> bookGrid = crud.getGrid();
    bookGrid.setPageSize(20);
    bookGrid.setColumns(Book.Fields.title, Book.Fields.author, Book.Fields.pageCount);
    crud.setCrudListener(new LazyCrudListener<>() {
      @Override
      public DataProvider<Book, Void> getDataProvider() {
        return DataProvider.fromCallbacks(
            query -> {
              log.info("offset: {}, limit: {}", query.getOffset(), query.getLimit());
              return bookService.findAll(query.getOffset() / query.getLimit(), query.getLimit()).stream();
            },
            query -> (int) bookService.count()
        );
      }

      @Override
      public Book add(Book book) {
        return null;
        //return bookService.insert(book);
      }

      @Override
      public Book update(Book book) {
        return null;
        //return bookService.save(book);
      }

      @Override
      public void delete(Book book) {
        //bookService.delete(book);
      }
    });

    getElement().appendChild(crud.getElement());
  }
}
