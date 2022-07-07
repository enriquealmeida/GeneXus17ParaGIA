01  GX-TTOC-DT.
    05 YY                          PIC  9(004).
    05 FILLER                      PIC  X(001).
    05 MM                          PIC  9(002).
    05 FILLER                      PIC  X(001).
    05 DD                          PIC  9(002).
    05 FILLER                      PIC  X(001).
    05 HH                          PIC  9(002).
    05 FILLER                      PIC  X(001).
    05 MIN                         PIC  9(002).
    05 FILLER                      PIC  X(001).
    05 SS                          PIC  9(002).
    05 FILLER                      PIC  X(001) VALUE '.'.
    05 MS                          PIC  X(006) VALUE '000000'.
01  GX-TTOC-DLEN                   PIC S9(003).
01  GX-TTOC-DFMT                   PIC S9(001).
    88 GX-TTOC-YMD                 VALUE 1.
    88 GX-TTOC-DMY                 VALUE 2.
    88 GX-TTOC-MDY                 VALUE 3.
01  GX-TTOC-TLEN                   PIC S9(003).
01  GX-TTOC-TFMT                   PIC S9(001).
    88 GX-TTOC-24H                 VALUE 1.
    88 GX-TTOC-12H                 VALUE 2.

01  GX-TTOC-C                      PIC  X(022).

01  GX-TTOC-AMPM-SYM               PIC  X(003).
01  GX-TTOC-POINTER                PIC S9(003).


