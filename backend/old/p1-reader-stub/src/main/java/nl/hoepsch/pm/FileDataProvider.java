package nl.hoepsch.pm;

import nl.hoepsch.pm.config.PowerMeterConfigurationProperties;
import nl.hoepsch.pm.config.StubConfigurationProperties;
import nl.hoepsch.pm.model.LogLine;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

import static java.util.Objects.requireNonNull;

/**
 * A {@link DataProvider} that read from the file configured in {@code application.yml} under the key {@code powerMeter.stub.dataFile}.
 */
@Component
public class FileDataProvider implements DataProvider {

    /**
     * The logger ot use.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FileDataProvider.class);

    /**
     * The data file date format (like MySQL).
     */
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("y-M-d H:m:s");
    /**
     * The filename to read.
     */
    private final String fileName;

    /**
     * The meter's id.
     */
    private final String meterId;

    /**
     * The meter's name.
     */
    private final String meterName;

    /**
     * The file reader.
     */
    private Reader reader;

    /**
     * The records iterator.
     */
    private Iterator<CSVRecord> records;

    private boolean eof;

    /**
     * The constructor.
     */
    @Autowired
    public FileDataProvider(final PowerMeterConfigurationProperties configurationProperties) {
        requireNonNull(configurationProperties);
        final StubConfigurationProperties stubProperties = configurationProperties.getStub();
        this.fileName = stubProperties.getDataFile();
        this.meterId = stubProperties.getMeterId();
        this.meterName = stubProperties.getMeterName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LogLine get() throws Exception {
        if (eof) {
            return null;
        }
        if (records == null) {
            openFile();
        }
        if (records.hasNext()) {
            final CSVRecord record = records.next();
            return convert(record);
        }
        reader.close();
        LOGGER.debug("Reached end of file.");
        eof = true;
        return null;
    }

    @SuppressWarnings("PMD.LawOfDemeter")
    private void openFile() throws IOException {
        reader = new InputStreamReader(new FileInputStream(fileName), "UTF-8");
        records = CSVFormat.MYSQL.parse(reader).iterator();
    }

    private LogLine convert(final CSVRecord csvRecord) {
        final LogLine logLine = new LogLine();
        logLine.setMeterId(meterId);
        logLine.setMeterName(meterName);
        logLine.setId(Long.parseLong(csvRecord.get(0)));
        logLine.setLogDate(csvRecord.get(1));
        logLine.setTimestamp(getDateTime(csvRecord.get(2)));
        logLine.setUsageHigh(Integer.parseInt(csvRecord.get(3)));
        logLine.setUsageLow(Integer.parseInt(csvRecord.get(4)));
        logLine.setCurrentUsage(Integer.parseInt(csvRecord.get(5)));
        logLine.setSupplyHigh(Integer.parseInt(csvRecord.get(6)));
        logLine.setSupplyLow(Integer.parseInt(csvRecord.get(7)));
        logLine.setCurrentSupply(Integer.parseInt(csvRecord.get(8)));
        logLine.setGas(Integer.parseInt(csvRecord.get(9)));
        logLine.setGasTimestamp(Integer.parseInt(csvRecord.get(10)));
        return logLine;
    }

    private LocalDateTime getDateTime(final String value) {
        return LocalDateTime.parse(value, DATE_TIME_FORMATTER);
    }

}
