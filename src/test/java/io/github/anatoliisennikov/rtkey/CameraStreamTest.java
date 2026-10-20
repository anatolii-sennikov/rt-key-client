package io.github.anatoliisennikov.rtkey;

import io.github.anatoliisennikov.rtkey.exception.RtKeyNetworkException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CameraStreamTest {

    @Test
    public void closeClosesBody() throws IOException {
        InputStream body = mock(InputStream.class);

        CameraStream stream = new CameraStream(body);

        assertThatCode(stream::close).doesNotThrowAnyException();
        verify(body).close();
    }

    @Test
    public void closeWrapsIOExceptionIntoRtKeyNetworkException() throws IOException {
        InputStream body = mock(InputStream.class);
        IOException cause = new IOException("boom");
        doThrow(cause).when(body).close();

        CameraStream stream = new CameraStream(body);

        assertThatThrownBy(stream::close)
                .isInstanceOf(RtKeyNetworkException.class)
                .hasMessage("Failed to close camera stream")
                .hasCause(cause);
    }
}
