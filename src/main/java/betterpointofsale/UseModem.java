package betterpointofsale;

import java.io.IOException;
import java.net.Socket;

class ModemDidNotConectException extends Exception {}

class SerialPort {
  public static void dialModem(int number)
      throws ModemDidNotConectException {
  }
}

class InfrastructureException extends Exception {
  // should create ALL the "pass-throug" constructors
  public InfrastructureException(Throwable cause) {
    super(cause);
  }
} // or use IOException??

class NoMoneyException extends Exception {}
class StolenCardException extends Exception {}

public class UseModem {
  private static boolean USE_INTERNET = false;
  // Generally attempt to recover from an exception as soon
  // as you have the necessary "resources" (e.g. data,
  // AND business process logic). -- decision is (often)
  // specific to BUSINESS LOGIC
  public static void getPaid(int amount)
      throws InfrastructureException, NoMoneyException, StolenCardException {
    try {
      if (USE_INTERNET) {
        Socket s = new Socket("127.0.0.1", 8080);
      } else {
        SerialPort.dialModem(1234);
      }
    } catch (ModemDidNotConectException m) {
      throw new InfrastructureException(m);
      // or return Either.failure(... maybe this is a String? ...)
      // even if you use Throwable as you are NOT "throwing" it any more
    } catch (IOException ie) {
      throw new InfrastructureException(ie);
    }

  }

  public static void sellStuff() {
    // determine price
    try {
      getPaid(1234);
    } catch (InfrastructureException ie) {
      // wiggle the plugs
    } catch (NoMoneyException nme) {
      // laugh in face
    } catch (StolenCardException sce) {
      // call the cops!
    }

    // if that failed request other form of payment
  }
}
