package nl.hoepsch.pm.dsmr;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

/**
 * A pipe that accepts data and can be read via an input stream.
 */
@Component
public class SerialPortDataPipe {

    /**
     * The output stream to write to.
     */
    private final PipedOutputStream outputStream;

    /**
     * The input stream paired to the piped output stream.
     */
    private final PipedInputStream inputStream;

    /**
     * The constructor.
     *
     * @throws IOException In case the output (write) cannot connect to the input (read).
     */
    public SerialPortDataPipe() throws IOException {
        this.outputStream = new PipedOutputStream();
        this.inputStream = new PipedInputStream();
        outputStream.connect(inputStream);
    }

    /**
     * Returns the input stream to read from.
     *
     * @return The input stream.
     */
    public InputStream getInputStream() {
        return inputStream;
    }


    /**
     * Write data to the pipe.
     *
     * @param data The data to write.
     * @throws IOException In case of an error.
     */
    public void write(final byte[] data) throws IOException {
        outputStream.write(data);
    }
}
