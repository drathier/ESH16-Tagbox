package se.drathier.tagbox.tagbox.mifare;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import se.drathier.tagbox.tagbox.Model;

import static se.drathier.tagbox.MainActivity.bytesToHex;

/**
 Chip: NTAG216: 888 bytes, 7104 bits

 nationality: SE (16bit) ISO-3166-1
 ssn: depends on nationality; make a lookup table, roughly 40bit

 SE:
 - 200 years
 - 366 days
 - 3 digit
 - 1 digit checksum
 = 73'200'000 (27bit)
 = 26 bit for max 183 years of age, e.g. 1900 as base year, max birth number would be 2083

 china has longest SSN format, 18 digits, almost 60 bit (can be compressed)

 if sex is not in SSN, add one bit for it at the end, i.e. y chromosome count

 then, bloodtype
 0,A,B,AB
 +-
 3bit

 organ donor: 1bit


 = 16+60+3+1 = 80bit = 10bytes

 SNOMED CT:
 - SCTID 32bit
 - start, stop, 16bit, 1900-2079
 - allvarlighetsgrad, 2bit (unknown, green, yellow, red)
 - WHAT ELSE?

 32 + 16 + 2 = 50bit = 6.25bytes ≈ 128 SNOMED CT entries


 TODO: digital signature, bankid or similar

 */
public class deserializer {
    private int offset = 0;

    public Model deserialize(ArrayList<Byte> in) {
        byte[] oarr = new byte[in.size()];
        for (int i = 0; i < in.size(); i++) {
            oarr[i] = (byte)in.get(i);
        }
        return deserialize(oarr);
    }
    public Model deserialize(byte[] in) {
        Model m = new Model();
        m.snomed_ids = new ArrayList<Model.Snomed_id>();

        m.CountryCode = get_string(in, 2);
        m.SSN = get_string(in, 18);

        byte bits = (byte)get_byte(in);
        m.is_male = extract_bit(1, bits);
        m.is_organ_donor = extract_bit(2, bits);

        boolean bt_a = extract_bit(3, bits);
        boolean bt_b = extract_bit(4, bits);

        if (bt_a && bt_b) {
            m.bt_ab = Model.BloodTypeAB.AB;
        } else if (bt_a) {
            m.bt_ab = Model.BloodTypeAB.A;
        } else if (bt_b) {
            m.bt_ab = Model.BloodTypeAB.B;
        } else {
            m.bt_ab = Model.BloodTypeAB.Zero;
        }

        m.bt_plus = extract_bit(5, bits) ? Model.BloodTypePlusMinus.Plus : Model.BloodTypePlusMinus.Minus;

        int ids = get_byte(in);
        for (int i = 0; i < ids; i++) {
            m.snomed_ids.add(get_snomed_id(in));
        }

        return m;
    }

    private Model.Snomed_id get_snomed_id(byte[] in) {
        Model.Snomed_id sid = new Model.Snomed_id();
        sid.id = get_int(in);
        sid.from = get_date(in);
        sid.to = get_date(in);
        sid.severity = Model.Severity.values()[get_byte(in)];
        return sid;
    }

    private Calendar get_date(byte[] in) {
        int days = get_int(in);
        Calendar epoch = Calendar.getInstance();
        epoch.set(1900,1,1);
        epoch.add(Calendar.DATE, days);
        return epoch;
    }

    private int get_int(byte[] in) {
        int out = ByteBuffer.wrap(Arrays.copyOfRange(in, this.offset, this.offset+4)).getInt();
        this.offset += 4;
        return out;
    }

    private boolean extract_bit(int i, byte bits) {
        return (bits & (1 << i)) != 0;
    }

    private int get_byte(byte[] in) {
        int out = (in[this.offset] + 0x100) & 0xFF;
        this.offset++;
        return out;
    }

    private String get_string(byte[] in, int length) {
        String s = "";
        for (int i = 0; i < length; i++) {
          if (in[this.offset + i] != 0x00) {
              s += (char) in[this.offset + i];
          }
        }
        this.offset += length;
        return s;
    }
}
