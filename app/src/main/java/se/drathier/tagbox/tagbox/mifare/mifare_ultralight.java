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

import se.drathier.tagbox.tagbox.Model;

import static se.drathier.tagbox.MainActivity.bytesToHex;

/**
 * Created by drathier on 2016-09-03.
 */
public class mifare_ultralight {
    private static final String TAG = mifare_ultralight.class.getSimpleName();
    public MifareUltralight mul;

    public mifare_ultralight(MifareUltralight a) {
            this.mul = a;
    }

    public ArrayList<Byte> read_all() throws IOException {
        ArrayList<Byte> out = new ArrayList<Byte>();
        int max = 0xDE;
        for (int i = 4; i < max; i+=4) {
            byte[] array = mul.readPages(i);
            for (int k = 0; k < 16; k++) {
                out.add(array[k]);
            }
            Log.d("r_id: " + i, bytesToHex(array));
        }

        byte[] oarr = new byte[out.size()];
        for (int i = 0; i < out.size(); i++) {
            oarr[i] = (byte)out.get(i);
        }

        Log.d("asd", bytesToHex(oarr));

        return out;
    }

    public void writeSerialized(Model m) throws IOException {
        NdefFormatable n = NdefFormatable.get(mul.getTag());
        byte[] data = serializer.serialize(m);
        Log.d("writeSerializedHex", bytesToHex(data));
        for (int i = 0; i < data.length && i < 800; i+=4) { // TODO: low limit to avoid bricking
            byte[] b = {data[i], data[i+1], data[i+2], data[i+3]}; // FIXME: might be out of bounds
            mul.writePage((i/4)+4, b);
            Log.d("w_id: " + i, bytesToHex(b));
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

