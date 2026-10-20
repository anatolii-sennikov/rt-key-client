package io.github.anatoliisennikov.rtkey.internal.events;

import io.github.anatoliisennikov.rtkey.BaseOperationTest;
import io.github.anatoliisennikov.rtkey.Host;
import io.github.anatoliisennikov.rtkey.internal.transport.Query;
import io.github.anatoliisennikov.rtkey.internal.transport.Response;
import io.github.anatoliisennikov.rtkey.model.BellCountQuery;
import io.github.anatoliisennikov.rtkey.model.NotificationStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

public class BellCountOperationTest extends BaseOperationTest {

    @Test
    public void countWithoutQuery() {
        when(transport.get(eq(Host.KEYAPIS), eq("/bell/api/v1/notification/count"), eq(Query.empty()), same(tokenSupplier)))
                .thenReturn(new Response(200, "{\"data\": 280}"));

        int count = BellCountOperation.call(transport, tokenSupplier, BellCountQuery.empty());
        assertThat(count).isEqualTo(280);

    }

    @Test
    public void countFiltered() {
        Query filters = Query.of("filter.orpons", "121").add("filter.roomNumbers", "11").add("filter.statusTypes", "NEW");
        when(transport.get(eq(Host.KEYAPIS), eq("/bell/api/v1/notification/count"), eq(filters), same(tokenSupplier)))
                .thenReturn(new Response(200, "{\"data\": 112}"));

        BellCountQuery query = BellCountQuery.builder()
                .orpons("121")
                .roomNumbers("11")
                .statusTypes(NotificationStatus.NEW)
                .build();

        int count = BellCountOperation.call(transport, tokenSupplier, query);
        assertThat(count).isEqualTo(112);
    }
}
