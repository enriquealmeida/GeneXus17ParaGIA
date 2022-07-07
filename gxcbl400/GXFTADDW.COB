01  GX-TADD-DT                     PIC  X(026).
01  GX-TADD-SEC                    PIC S9(013) COMP-3.

01  GX-TADD-TDAY                   PIC S9(007) COMP-3.
01  GX-TADD-TSEC                   PIC S9(007)V9(005) COMP-3.
01  GX-TADD-DAYSEC                 PIC  9(009)V9(005).
01  GX-TADD-FILLER REDEFINES GX-TADD-DAYSEC.
    05 GX-TADD-DAYS                PIC  9(009).
    05 GX-TADD-SECS                PIC  9(005).

01  GX-TADD-OUTDT                  PIC  X(26).
