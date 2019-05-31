package nl.hoepsch.pm.kafka;

import nl.hoepsch.pm.dsmr.model.DSMR5Datagram;
import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectData;

/**
 * Utility to generate an Avro schema for a given class.
 */
@SuppressWarnings({"checkstyle:HideUtilityClassConstructor", "PMD.UseUtilityClass"})
public final class SchemaGen {

    /**
     * The main method to generate the schema.
     *
     * @param args Std java args.
     */
    @SuppressWarnings({"PMD.LawOfDemeter", "PMD.SystemPrintln"})
    public static void main(final String[] args) {
        // get the reflected schema for packets
        final Schema schema = ReflectData.get().getSchema(DSMR5Datagram.class);
        System.out.println(schema.toString(true));
    }
}
