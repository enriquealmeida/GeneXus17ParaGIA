01  GX-UPLO-CTR-BLK.
    05 GX-UPLO-TYPE                PIC S9(006) COMP-4 VALUE 1.
    05 GX-UPLO-INPUT               PIC S9(006) COMP-4 VALUE 0.
    05 GX-UPLO-CASE                PIC S9(006) COMP-4.
    05 FILLER                      PIC  X(010)        VALUE LOW-VALUE.
01  GX-UPLO-IN                     PIC  X(256).
01  GX-UPLO-OUT                    PIC  X(256).
01  GX-UPLO-LEN                    PIC S9(006) COMP-4.
01  GX-UPLO-ERRDATA.
    05 GX-UPLO-INPUT               PIC S9(006) COMP-4 VALUE 0.
    05 GX-UPLO-OUTPUT              PIC S9(006) COMP-4 VALUE 0.
    05 GX-UPLO-EXCPT-ID            PIC  X(007).
    05 FILLER                      PIC  X(001).
    05 GX-UPLO-EXCPT-DATA          PIC  X(087).

