01  GX-TDTE-INDATE.
    02 YY                          PIC  9(004).
    02 MM                          PIC  9(002).
    02 DD                          PIC  9(002).
01  GX-TDTE-DW.
    02 YY                          PIC S9(005) COMP-3.
    02 MM                          PIC S9(003) COMP-3.
       88 GX-TDTE-31-DM                     VALUE 1, 3, 5, 7, 8,
     10, 12.
       88 GX-TDTE-2-M                       VALUE 2.
    02 DD                          PIC S9(003) COMP-3.

01  GX-TDTE-Q                      PIC S9(003) COMP-3.
01  GX-TDTE-R-4                    PIC S9(001) COMP-3.
01  GX-TDTE-R-400                  PIC S9(003) COMP-3.
01  GX-TDTE-R-100                  PIC S9(003) COMP-3.
01  GX-TDTE-MFD                    PIC S9(003) COMP-3.
01  GX-TDTE-OUTNUM                 PIC S9(001) COMP-3.
