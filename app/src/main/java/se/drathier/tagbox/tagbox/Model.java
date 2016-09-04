package se.drathier.tagbox.tagbox;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Calendar;

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
public class Model implements Parcelable {
    public enum BloodTypeAB {
        Zero, A, B, AB;
    }
    public enum BloodTypePlusMinus {
        Plus, Minus;
    }
    public enum Severity {
        Unknown, Low, Medium, High;
    }
    public static class Snomed_id {
        public int id;
        public String response;
        public Calendar from;
        public Calendar to;
        public Severity severity;

        public Snomed_id() {
        }

    }
    public static class Sign implements Parcelable {
        public int sign; // FIXME: placeholder

        public Sign() {
        }

        protected Sign(Parcel in) {
            sign = in.readInt();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(sign);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Sign> CREATOR = new Parcelable.Creator<Sign>() {
            @Override
            public Sign createFromParcel(Parcel in) {
                return new Sign(in);
            }

            @Override
            public Sign[] newArray(int size) {
                return new Sign[size];
            }
        };
    }

    public String CountryCode;
    public String given_name;
    public String SSN;
    public BloodTypePlusMinus bt_plus;
    public BloodTypeAB bt_ab;
    public Boolean is_organ_donor;
    public Boolean is_male;

    public ArrayList<Snomed_id> snomed_ids;

    public Sign sign;

    public Model() {
    }

    protected Model(Parcel in) {
        CountryCode = in.readString();
        given_name = in.readString();
        SSN = in.readString();
        bt_plus = (BloodTypePlusMinus) in.readValue(BloodTypePlusMinus.class.getClassLoader());
        bt_ab = (BloodTypeAB) in.readValue(BloodTypeAB.class.getClassLoader());
        is_organ_donor = in.readByte() != 0x00;
        is_male = in.readByte() != 0x00;
        if (in.readByte() == 0x01) {
            snomed_ids = new ArrayList<Snomed_id>();
            in.readList(snomed_ids, Snomed_id.class.getClassLoader());
        } else {
            snomed_ids = null;
        }
        sign = (Sign) in.readValue(Sign.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(CountryCode);
        dest.writeString(given_name);
        dest.writeString(SSN);
        dest.writeValue(bt_plus);
        dest.writeValue(bt_ab);
        dest.writeByte((byte) (is_organ_donor ? 0x01 : 0x00));
        dest.writeByte((byte) (is_male ? 0x01 : 0x00));
        if (snomed_ids == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(snomed_ids);
        }
        dest.writeValue(sign);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Model> CREATOR = new Parcelable.Creator<Model>() {
        @Override
        public Model createFromParcel(Parcel in) {
            return new Model(in);
        }

        @Override
        public Model[] newArray(int size) {
            return new Model[size];
        }
    };
}