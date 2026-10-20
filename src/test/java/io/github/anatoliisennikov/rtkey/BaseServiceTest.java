package io.github.anatoliisennikov.rtkey;

import io.github.anatoliisennikov.rtkey.internal.transport.Body;
import io.github.anatoliisennikov.rtkey.internal.transport.Query;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.ThrowableAssert.catchThrowable;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.verify;


public abstract class BaseServiceTest extends BaseOperationTest {


    protected void assertReachedTransport(ThrowingCallable serviceCall) {
        catchThrowable(serviceCall);
        assertThat(mockingDetails(transport).getInvocations()).hasSize(1);
    }

    protected void verifyGet(Host host, String path) {
        verify(transport).get(eq(host), eq(path), same(tokenSupplier));
    }

    protected void verifyGet(Host host, String path, String expectedQuery) {
        ArgumentCaptor<Query> query = ArgumentCaptor.forClass(Query.class);

        verify(transport).get(eq(host), eq(path), query.capture(), same(tokenSupplier));

        assertThat(query.getValue().encoded()).isEqualTo(expectedQuery);
    }

    protected void verifyGetEmptyQuery(Host host, String path) {
        ArgumentCaptor<Query> query = ArgumentCaptor.forClass(Query.class);
        verify(transport).get(eq(host), eq(path), query.capture(), same(tokenSupplier));
        assertThat(query.getValue().isEmpty()).isTrue();
    }

    protected void verifyPost(Host host, String path) {
        verify(transport).post(eq(host), eq(path), same(tokenSupplier));
    }

    protected void verifyPost(Host host, String path, String expectedJsonPayload) {
        ArgumentCaptor<Body> body = ArgumentCaptor.forClass(Body.class);

        verify(transport).post(eq(host), eq(path), body.capture(), same(tokenSupplier));

        assertJsonBody(body.getValue(), expectedJsonPayload);
    }

    protected void verifyPatch(Host host, String path, String expectedJsonPayload) {
        ArgumentCaptor<Body> body = ArgumentCaptor.forClass(Body.class);

        verify(transport).patch(eq(host), eq(path), body.capture(), same(tokenSupplier));

        assertJsonBody(body.getValue(), expectedJsonPayload);
    }

    protected void verifyDelete(Host host, String path) {
        verify(transport).delete(eq(host), eq(path), same(tokenSupplier));
    }

    protected void assertDidNotReachTransport() {
        assertThat(mockingDetails(transport).getInvocations()).isEmpty();
    }
}
