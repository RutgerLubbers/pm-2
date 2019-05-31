package nl.hoepsch.pm.dsmr;

import org.apache.commons.compress.utils.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.lang.String.format;

/**
 * A SerialDataAcceptor that writes the data to files, one per minute.
 */
@Component
public class ToFileSerialDataAcceptor implements SerialDataAcceptor {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ToFileSerialDataAcceptor.class);

    /**
     * Formatter to format the date for the output file.
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmm");

    /**
     * Path for stored, older, P1 datagrams.
     */
    @Value("${datagram-store.current}")
    private String datagramStore;

    /**
     * The open file writer.
     */
    private BufferedWriter fileWriter;

    /**
     * The current time stamp in use.
     */
    private String current;

    /**
     * {@inheritDoc}
     */
    @Override
    public void accept(final byte[] data) {
        try {
            updateFileWriter(FORMATTER.format(LocalDateTime.now()));

            fileWriter.write(new String(data, Charsets.UTF_8));
        } catch (IOException e) {
            LOGGER.error("Got error with writing the file.", e);
        }
    }

    private void updateFileWriter(final String timestamp) throws IOException {
        if (!timestamp.equals(current)) {
            if (fileWriter != null) {
                fileWriter.flush();
                fileWriter.close();
            }
            final String fileName = format("p1-%s.txt", timestamp);
            LOGGER.debug("Writing raw output to '{}'.", fileName);
            fileWriter = Files.newBufferedWriter(Paths.get(datagramStore, fileName));
            current = timestamp;
        }
    }

}
