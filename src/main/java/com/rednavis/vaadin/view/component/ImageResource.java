package com.rednavis.vaadin.view.component;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public enum ImageResource {
  REDNAVIS_LOGO("rednavis-logo.jpg", "RedNavis Logo");

  private String fileName;
  private String alt;

  ImageResource(String fileName, String alt) {
    this.fileName = fileName;
    this.alt = alt;
  }

  public Image create() {
    return new Image(createImage(this.fileName), this.alt);
  }

  private StreamResource createImage(String fileName) {
    return new StreamResource(this.name(), (InputStreamFactory) () -> {
      try {
        return new FileInputStream("frontend/images/" + fileName);
      } catch (FileNotFoundException e) {
        log.error("Can't find image [fileName: {}]", fileName, e);
      }
      return null;
    });
  }
}
