01  GX-TDIFF-LDT                   PIC  X(26).
01  GX-TDIFF-RDT                   PIC  X(26).

01  GX-TDIFF-DAYSEC                PIC  9(009)V9(005).
01  GX-TDIFF-FILLER REDEFINES GX-TDIFF-DAYSEC.
    05 GX-TDIFF-DAYS               PIC  9(009).
    05 GX-TDIFF-SECS               PIC  9(005).

01  GX-TDIFF-OUTN                  PIC S9(013) COMP-3.
