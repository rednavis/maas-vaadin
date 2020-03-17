package com.rednavis.vaadin.view.book;

import static com.rednavis.shared.util.RoleUtils.ROLE_USER;
import static com.rednavis.vaadin.util.ConstantUtils.PAGE_BOOK_CAPTION;
import static com.rednavis.vaadin.util.ConstantUtils.PAGE_BOOK_TITLE;
import static com.rednavis.vaadin.util.ConstantUtils.PAGE_BOOK_URL;

import com.github.appreciated.app.layout.annotations.Caption;
import com.github.appreciated.app.layout.annotations.Icon;
import com.rednavis.vaadin.view.MainView;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.templatemodel.TemplateModel;
import org.springframework.security.access.annotation.Secured;

@Secured({ROLE_USER})
@PageTitle(PAGE_BOOK_TITLE)
@Caption(PAGE_BOOK_CAPTION)
@Route(value = PAGE_BOOK_URL, layout = MainView.class)
@Icon(VaadinIcon.BOOK)
@Tag("book-view")
@JsModule("./src/view/book/book-view.js")
public class BookView extends PolymerTemplate<TemplateModel> {

  public BookView() {
    H1 h1 = new H1("Book View");
    getElement().appendChild(h1.getElement());
  }
}
