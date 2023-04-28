package com.xu.pet.core;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Accessors(chain = true)
@Builder(toBuilder = true)
public class Label implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;

    public static LabelBuilder builder() {
        return new LabelBuilder();
    }

    public Long getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setName(final String name) {
        this.name = name;
    }


    protected boolean canEqual(final Object other) {
        return other instanceof Label;
    }


    public Label(final Long id, final String name) {
        this.id = id;
        this.name = name;
    }

    public Label() {
    }

    public static class LabelBuilder {
        private Long id;
        private String name;

        LabelBuilder() {
        }

        public LabelBuilder id(final Long id) {
            this.id = id;
            return this;
        }

        public LabelBuilder name(final String name) {
            this.name = name;
            return this;
        }

        public Label build() {
            return new Label(this.id, this.name);
        }

        public String toString() {
            return "Label.LabelBuilder(id=" + this.id + ", name=" + this.name + ")";
        }
    }
}