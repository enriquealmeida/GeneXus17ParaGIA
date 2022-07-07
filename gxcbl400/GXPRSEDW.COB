01  GX-CTOT-C                      PIC  X(022).
01  GX-CTOT-DLEN                   PIC S9(003).
01  GX-CTOT-TLEN                   PIC S9(003).
01  GX-CTOT-DFMT                   PIC S9(001).
    88 GX-CTOT-YMD                 VALUE 1.
    88 GX-CTOT-DMY                 VALUE 2.
    88 GX-CTOT-MDY                 VALUE 3.
01  GX-CTOT-TFMT                   PIC S9(001).
    88 GX-CTOT-24H                 VALUE 1.
    88 GX-CTOT-12H                 VALUE 2.

01  GX-CTOT-DT.
    05 YY                          PIC  X(004) JUST.
    05 FILLER                      PIC  X(001) VALUE '-'.
    05 MM                          PIC  X(002) JUST.
    05 FILLER                      PIC  X(001) VALUE '-'.
    05 DD                          PIC  X(002) JUST.
    05 FILLER                      PIC  X(001) VALUE '-'.
    05 HH                          PIC  X(002) JUST.
    05 FILLER                      PIC  X(001) VALUE '.'.
    05 MIN                         PIC  X(002) JUST.
    05 FILLER                      PIC  X(001) VALUE '.'.
    05 SS                          PIC  X(002) JUST.
    05 FILLER                      PIC  X(001) VALUE '.'.
    05 MS                          PIC  X(006) VALUE '000000'.
01  GX-CTOT-VDATE                  PIC S9(001).
    88 GX-CTOT-VALID               VALUE 0.
    88 GX-CTOT-NOT-VALID           VALUE 1.
    88 GX-CTOT-DATE-LENGTH         VALUE 2.
    88 GX-CTOT-YY-LENGTH           VALUE 3.
    88 GX-CTOT-MM-LENGTH           VALUE 4.
    88 GX-CTOT-DD-LENGTH           VALUE 5.
    88 GX-CTOT-HH-LENGTH           VALUE 6.
    88 GX-CTOT-MIN-LENGTH          VALUE 7.
    88 GX-CTOT-SS-LENGTH           VALUE 8.
    88 GX-CTOT-HH-VALUE            VALUE 9.

01  GX-CTOT-YY-L                   PIC S9(001).
01  GX-CTOT-MM-L                   PIC S9(001).
01  GX-CTOT-DD-L                   PIC S9(001).
01  GX-CTOT-HH-L                   PIC S9(001).
01  GX-CTOT-MIN-L                  PIC S9(001).
01  GX-CTOT-SS-L                   PIC S9(001).
01  GX-CTOT-HH-DELIM               PIC  X(001).
01  GX-CTOT-MIN-DELIM              PIC  X(001).
01  GX-CTOT-SS-DELIM               PIC  X(001).

01  GX-CTOT-HH                     PIC S9(003).
01  GX-CTOT-BUFFER                 PIC  X(026).
01  GX-CTOT-COUNT                  PIC S9(003).
01  GX-CTOT-DELIM                  PIC  X(001).
01  GX-CTOT-POINTER                PIC S9(003).

