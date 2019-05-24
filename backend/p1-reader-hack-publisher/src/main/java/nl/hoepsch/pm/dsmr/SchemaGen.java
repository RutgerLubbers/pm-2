package nl.hoepsch.pm.dsmr;

import nl.hoepsch.pm.dsmr.dto_input.DSMR5DatagramDto;
import nl.hoepsch.pm.dsmr.model.DSMR5Datagram;
import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectData;

import java.io.IOException;

public class SchemaGen {

    public static void main(String[] args) throws IOException {
        // get the reflected schema for packets
        Schema schema = ReflectData.get().getSchema(DSMR5Datagram.class);
        System.out.println(schema.toString(true));
    }
}
