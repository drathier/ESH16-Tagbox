package se.drathier.tagbox.tagbox.mifare;

import android.nfc.NdefMessage;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by drathier on 2016-09-03.
 */
public class mifare_ultralight {
    private static final String TAG = mifare_ultralight.class.getSimpleName();
    public MifareUltralight mul;

    public mifare_ultralight(MifareUltralight a) {
            this.mul = a;
    }

    public void writeTag(Tag tag, String tagText) {
        MifareUltralight ultralight = MifareUltralight.get(tag);
        try {
            ultralight.connect();
            ultralight.writePage(4, "abcd".getBytes(Charset.forName("US-ASCII")));
            ultralight.writePage(5, "efgh".getBytes(Charset.forName("US-ASCII")));
            ultralight.writePage(6, "ijkl".getBytes(Charset.forName("US-ASCII")));
            ultralight.writePage(7, "mnop".getBytes(Charset.forName("US-ASCII")));
        } catch (IOException e) {
            Log.e(TAG, "IOException while closing MifareUltralight...", e);
        } finally {
            try {
                ultralight.close();
            } catch (IOException e) {
                Log.e(TAG, "IOException while closing MifareUltralight...", e);
            }
        }
    }

    public String readTag(Tag tag) {
        MifareUltralight mifare = MifareUltralight.get(tag);
        try {
            mifare.connect();
            byte[] payload = mifare.readPages(4);
            return new String(payload, Charset.forName("US-ASCII"));
        } catch (IOException e) {
            Log.e(TAG, "IOException while writing MifareUltralight message...", e);
        } finally {
            if (mifare != null) {
                try {
                    mifare.close();
                }
                catch (IOException e) {
                    Log.e(TAG, "Error closing tag...", e);
                }
            }
        }
        return null;
    }

    public ArrayList<Byte> read_all() throws IOException {
        ArrayList<Byte> out = new ArrayList<Byte>();
        int max = 0xDE;
        for (int i = 4; i < max; i+=4) {
            byte[] array = mul.readPages(i);
            out.add(array[0]);
            out.add(array[1]);
            out.add(array[2]);
            out.add(array[3]);
        }
        return out;
    }

    public void writeParcel(Parcel p) throws IOException {
        NdefFormatable n = NdefFormatable.get(mul.getTag());
        byte[] data = p.marshall();
        for (int i = 4; i < data.length && i < 0xD0; i+=4) {
            byte[] b = {data[i], data[i+1], data[i+2], data[i+3]};/*,
                        data[i+4], data[i+5], data[i+6], data[i+7],
                        data[i+8], data[i+9], data[i+10], data[i+11],
                        data[i+12], data[i+13], data[i+14], data[i+15]}; */// FIXME: might be out of bounds
            mul.writePage(i+4, b);
            Log.d("id: " + i, Arrays.toString(b));
        }
    }

    public static boolean writeTag(NdefMessage message, Tag tag) {
        int size = message.toByteArray().length;
        try {
            Ndef ndef = Ndef.get(tag);
            if (ndef != null) {
                ndef.connect();
                if (!ndef.isWritable()) {
                    return false;
                }
                if (ndef.getMaxSize() < size) {
                    return false;
                }
                ndef.writeNdefMessage(message);
                return true;
            } else {
                NdefFormatable format = NdefFormatable.get(tag);
                if (format != null) {
                    try {
                        format.connect();
                        format.format(message);
                        return true;
                    } catch (IOException e) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            return false;
        }
    }
}

