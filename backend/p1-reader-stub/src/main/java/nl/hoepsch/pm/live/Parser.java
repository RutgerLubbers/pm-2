package nl.hoepsch.pm.live;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

public class Parser {

    private static final String INPUT = "/Ene5\\XS210 ESMR 5.0\r\n"
            + "\r\n"
            + "1-3:0.2.8(50)\r\n"
            + "0-0:1.0.0(180228194003W)\r\n"
            + "0-0:96.1.1(4530303437303030303035393839353137)\r\n"
            + "1-0:1.8.1(001130.629*kWh)\r\n"
            + "1-0:1.8.2(000596.802*kWh)\r\n"
            + "1-0:2.8.1(000073.503*kWh)\r\n"
            + "1-0:2.8.2(000098.616*kWh)\r\n"
            + "0-0:96.14.0(0002)\r\n"
            + "1-0:1.7.0(00.813*kW)\r\n"
            + "1-0:2.7.0(00.000*kW)\r\n"
            + "0-0:96.7.21(00039)\r\n"
            + "0-0:96.7.9(00003)\r\n"
            + "1-0:99.97.0(1)(0-0:96.7.19)(170725200409S)(0000000249*s)\r\n"
            + "1-0:32.32.0(00003)\r\n"
            + "1-0:32.36.0(00000)\r\n"
            + "0-0:96.13.0()\r\n"
            + "1-0:32.7.0(227.0*V)\r\n"
            + "1-0:31.7.0(003*A)\r\n"
            + "1-0:21.7.0(00.813*kW)\r\n"
            + "1-0:22.7.0(00.000*kW)\r\n"
            + "0-1:24.1.0(003)\r\n"
            + "0-1:96.1.0(4730303533303033363335373135313137)\r\n"
            + "0-1:24.2.1(180228194000W)(00838.176*m3)\r\n"
            + "!0D9C\r\n";

    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        String name = "..";
        try (BufferedReader br = new BufferedReader(new StringReader(INPUT))) {
            int i;
            while ((i = br.read()) != -1) {
                char c = (char) i;
                sb.append(c);
                System.out.print(c);
                if (c == '/') {
                    sb = new StringBuilder();
                    name = readName(br);
                    // New datagram
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println();
        System.out.println();
        System.out.println("'" + name + "'");
        System.out.println();
        System.out.println();
        System.out.println(sb.toString());
    }

    private static String readName(final BufferedReader br) throws IOException {
        StringBuilder sb = new StringBuilder();
        int i;
        while ((i = br.read()) != -1) {
            char c = (char) i;
            System.out.print(c);
            if (c == '\r') {
                System.out.print((char)br.read());
                return sb.toString();
            }
            sb.append((char)i);
        }

        return null;
    }

}
