package se.drathier.tagbox.tagbox.mifare;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.ByteBuffer;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import se.drathier.tagbox.tagbox.Model;

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
public class serializer {
    public static byte[] serialize(Model m) {
        ArrayList<Byte> out = new ArrayList<>();

        // SE
        add_string(out, 2, m.CountryCode);

        // SSN (18 char string)
        add_string(out, 18, m.SSN);

        // bits
        // [is_male, is_organ_donor, bloodtype_a, bloodtype_b, bloodtype_plus]
        out.add((byte) (
                byte_shift(1, byte_bool(m.is_male)) |
                byte_shift(2, byte_bool(m.is_organ_donor)) |
                byte_shift(3, byte_bool(((m.bt_ab == Model.BloodTypeAB.A || m.bt_ab == Model.BloodTypeAB.AB)))) |
                byte_shift(4, byte_bool(((m.bt_ab == Model.BloodTypeAB.B || m.bt_ab == Model.BloodTypeAB.AB)))) |
                byte_shift(5, byte_bool(m.bt_plus == Model.BloodTypePlusMinus.Plus))));

        out.add((byte)m.snomed_ids.size());
        for (Model.Snomed_id sid : m.snomed_ids) {
            serialize_snomed(out, sid);
        }

        // output as byte array
        int size = out.size();
        while (size % 4 != 0) {
            size++;
        }
        byte[] oarr = new byte[size];
        for (int i = 0; i < out.size(); i++) {
            oarr[i] = (byte)out.get(i);
        }

        return oarr;
    }

    private static void serialize_snomed(ArrayList<Byte> out, Model.Snomed_id sid) {
        add_int(out, sid.id);

        add_date(out, sid.from);
        add_date(out, sid.to);
        out.add((byte)sid.severity.ordinal());
    }

    public static void add_int(ArrayList<Byte> out, int i) {
        byte[] res = ByteBuffer.allocate(4).putInt(i).array();
        out.add(res[0]);
        out.add(res[1]);
        out.add(res[2]);
        out.add(res[3]);
    }

    public static void add_date(ArrayList<Byte> out, Calendar date) {
        Calendar epoch = Calendar.getInstance();
        epoch.set(1900,1,1);
        add_int(out, daysBetween(epoch, date));
    }

    public static void add_string(ArrayList<Byte> out, int length, String str) {
        for (int i = 0; i < length; i++) {
            if (i < str.length()) {
                out.add((byte)str.charAt(i));
            } else {
                out.add((byte)0x00);
            }
        }
    }

    public static byte byte_bool(boolean b) {
        return b ? (byte)1 : 0;
    }

    public static byte byte_shift(int steps, byte b) {
        return (byte) (b << steps);
    }

    // date diff
    public static int daysBetween(Calendar day1, Calendar day2){
        Calendar dayOne = (Calendar) day1.clone(),
                dayTwo = (Calendar) day2.clone();

        if (dayOne.get(Calendar.YEAR) == dayTwo.get(Calendar.YEAR)) {
            return Math.abs(dayOne.get(Calendar.DAY_OF_YEAR) - dayTwo.get(Calendar.DAY_OF_YEAR));
        } else {
            if (dayTwo.get(Calendar.YEAR) > dayOne.get(Calendar.YEAR)) {
                //swap them
                Calendar temp = dayOne;
                dayOne = dayTwo;
                dayTwo = temp;
            }
            int extraDays = 0;

            int dayOneOriginalYearDays = dayOne.get(Calendar.DAY_OF_YEAR);

            while (dayOne.get(Calendar.YEAR) > dayTwo.get(Calendar.YEAR)) {
                dayOne.add(Calendar.YEAR, -1);
                // getActualMaximum() important for leap years
                extraDays += dayOne.getActualMaximum(Calendar.DAY_OF_YEAR);
            }

            return extraDays - dayTwo.get(Calendar.DAY_OF_YEAR) + dayOneOriginalYearDays ;
        }
    }
}
