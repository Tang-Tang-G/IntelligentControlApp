package com.example.intelligentcontrolapp.PairDevice;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

public abstract class Pair {
    public static class Message {
        String ssid;
        String password;
        String host;
        public Message(String ssid, String password, String host) {
            this.host = host;
            this.password = password;
            this.ssid = ssid;
        }
    }

    public enum PairingStatus {
        MessageOk,
        AskConfig,
        MessageTooLongError,
        FormatError,
        WiFiConnected,
        HostConnected,
        InterruptedError,
        UnknownError,
        Finish,
        WiFiConnectionTimeout,
        HostConnectionTimeOut,
    }

    public static class PairingError extends Throwable {
        String message;
        PairingStatus status;

        PairingError(String message, PairingStatus status) {
            this.message = message;
            this.status = status;
        }
    }

    protected static PairingStatus getPairingStatus(int code) {
        Log.d("pair", "read in " + code);
        switch (code) {
            case 0:
                return PairingStatus.MessageOk;
            case 1:
            case 7:
                return PairingStatus.AskConfig;
            case 2:
                return PairingStatus.MessageTooLongError;
            case 3:
                return PairingStatus.FormatError;
            case 4:
                return PairingStatus.WiFiConnected;
            case 5:
                return PairingStatus.HostConnected;
            case 6:
                return PairingStatus.InterruptedError;
            case 8:
                return PairingStatus.Finish;

            case 9:
                return PairingStatus.WiFiConnectionTimeout;

            case 10:
                return PairingStatus.HostConnectionTimeOut;

            default:
                return PairingStatus.UnknownError;
        }
    }

    protected static final Logger logger = Logger.getLogger(Pair.class.getName());

    void onMessageOk() {}
    void onAskConfig() {}
    void onMessageErr() {}
    void onWiFiConnected() {}
    void onHostConnected() {}
    void onOtherError() {}

    public boolean devicePair(OutputStream out, InputStream ins, Message message) throws PairingError, IOException {

        String msg = message.ssid + "\t" + message.password + "\t" + message.host + "\n";
        logger.info("write " + msg);
        out.write(msg.getBytes());
        PairingStatus status = getPairingStatus(ins.read());

        while (true) {
            switch (status) {
                case MessageOk:
                    logger.info("message ok");
                        onMessageOk();
                    break;

                case AskConfig:
                    logger.info("ask for config");
                    onAskConfig();
                    out.write(msg.getBytes());
                    break;

                case MessageTooLongError:
                    logger.info("message too long error");
                    throw new PairingError(msg + "is too long", PairingStatus.MessageTooLongError);

                case FormatError:
                    logger.info("format error");
                    throw new PairingError(msg + " is not the correct msg format", PairingStatus.FormatError);

                case WiFiConnected:
                    logger.info("connect to wifi");
                    onWiFiConnected();
                    if (message.host.equals("")) {
                        return true;
                    }
                    break;

                case HostConnected:
                    logger.info("connect to wifi and host");
                    onHostConnected();
                    return true;

                case InterruptedError:
                    logger.info("another phone try to pair the device");
                    onOtherError();
                    throw new PairingError("pairing was interrupted by another client", PairingStatus.InterruptedError);

                case Finish:
                    return true;

                case HostConnectionTimeOut:
                    logger.info("connect to host timeout");
                    throw new PairingError("host connect timeout", PairingStatus.HostConnectionTimeOut);

                case WiFiConnectionTimeout:
                    logger.info("connect to wifi timeout");
                    throw new PairingError("wifi connect timeout", PairingStatus.WiFiConnectionTimeout);

                case UnknownError:

                default:
                    throw new PairingError("unknown error", PairingStatus.UnknownError);
            }
            status = getPairingStatus(ins.read());
        }
    }

    abstract boolean pair();
}
