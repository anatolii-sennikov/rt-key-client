package io.github.anatoliisennikov.rtkey.model;

import java.util.List;

/**
 * A page of items with the total count reported by the server.
 *
 * <p>Returned by the paginated list endpoints, for example
 * {@link io.github.anatoliisennikov.rtkey.DeviceService#devices()} and
 * {@link io.github.anatoliisennikov.rtkey.KeysService#rfids()}.
 *
 * @param items items of the page; the list is copied and immutable
 * @param total total number of items reported by the server
 * @param <T>   type of the items
 */
public record Page<T>(List<T> items, Integer total) {
    public Page {
        items = List.copyOf(items);
    }
}
