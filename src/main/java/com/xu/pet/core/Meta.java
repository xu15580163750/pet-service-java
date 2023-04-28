package com.xu.pet.core;


import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
@Builder(toBuilder = true)
public class Meta extends HashMap<String, String> implements Serializable {
    private static final long serialVersionUID = 1L;

    public Meta() {
    }

    public Meta(Map<String, String> m) {
        if (null != m && !m.isEmpty()) {
            this.putAll(m);
        }

    }
}
