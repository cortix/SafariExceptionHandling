package pointofsale;

import java.io.IOException;
import java.net.Socket;

class ModemDidNotConectException extends Exception {}
class SerialPort {
  public static void dialModem(int number)
      throws ModemDidNotConectException {

  }
}

public class UseModem {
  private static boolean USE_INTERNET = false;
  // Generally attempt to recover from an exception as soon
  // as you have the necessary "resources" (e.g. data,
  // AND business process logic). -- decision is (often)
  // specific to BUSINESS LOGIC
  public static void getPaid(int amount)
      throws ModemDidNotConectException, IOException {
    if (USE_INTERNET) {
      Socket s = new Socket("127.0.0.1", 8080);
    } else {
      SerialPort.dialModem(1234);
    }
    // request money...
  }

  public static void sellStuff() {
    // determine price
    try {
      getPaid(1234);
    } catch (ModemDidNotConectException m) {
      // check the modem connections
      System.out.println("suggest a different card?");
    } catch (IOException ie) {
      // something else
    }
    // if that failed request other form of payment
  }
}
