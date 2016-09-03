package se.drathier.tagbox.tagbox;

import java.util.Date;
import java.util.List;

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
public class Model {
    public enum BloodTypeAB {
        Zero, A, B, AB;
    }
    public enum BloodTypePlusMinus {
        Plus, Minus;
    }
    public enum Severity {
        Unknown, Low, Medium, High;
    }
    public class Snomed_id {
        int id;
        Date from;
        Date to;
        Severity severity;
    }
    public class Sign {
        int sign; // FIXME: placeholder
    }

    String CountryCode;
    String SSN;
    BloodTypePlusMinus bt_plus;
    BloodTypeAB bt_ab;
    boolean is_organ_donor;
    boolean is_male;

    List<Snomed_id> snomed_ids;

    Sign sign;

}

