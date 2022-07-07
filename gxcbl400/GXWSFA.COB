01  GX-RST-SCR                     PIC S9      COMP-3.
01  WS-FEEDBACK-AREA.
    05 FILLER                      PIC  X(144).
    05 FILLER.
       10 FILLER                   PIC  X(002).
       10 WS-AID                   PIC  X(001).
       10 WS-CURSOR.
          15 WS-ROW                PIC  X(001).
          15 WS-COL                PIC  X(001).
       10 FILLER                   PIC  X(006).
       10 WS-FIRST-SFL-REC         PIC  9(004) COMP-4.
       10 FILLER                   PIC  X(019).
       10 FILLER                   PIC  X(067).
01  WS-AID-BYTE-SAVED              PIC  X(002).
01  WS-AID-BYTE.
    07 FILLER                      PIC  X(001) VALUE LOW-VALUE.
    07 WS-AID-BYTE-HEX             PIC  X(001).
01  WS-AID-BYTE-REDEFINED
    REDEFINES WS-AID-BYTE          PIC S9(04) COMP-4.
    88  WS-KEY-F1                             VALUE +49.
    88  WS-KEY-F2                             VALUE +50.
    88  WS-KEY-F3                             VALUE +51.
    88  WS-KEY-F4                             VALUE +52.
    88  WS-KEY-F5                             VALUE +53.
    88  WS-KEY-F6                             VALUE +54.
    88  WS-KEY-F7                             VALUE +55.
    88  WS-KEY-F8                             VALUE +56.
    88  WS-KEY-F9                             VALUE +57.
    88  WS-KEY-F10                            VALUE +58.
    88  WS-KEY-F11                            VALUE +59.
    88  WS-KEY-F12                            VALUE +60.
    88  WS-KEY-F13                            VALUE +177.
    88  WS-KEY-F14                            VALUE +178.
    88  WS-KEY-F15                            VALUE +179.
    88  WS-KEY-F16                            VALUE +180.
    88  WS-KEY-F17                            VALUE +181.
    88  WS-KEY-F18                            VALUE +182.
    88  WS-KEY-F19                            VALUE +183.
    88  WS-KEY-F20                            VALUE +184.
    88  WS-KEY-F21                            VALUE +185.
    88  WS-KEY-F22                            VALUE +186.
    88  WS-KEY-F23                            VALUE +187.
    88  WS-KEY-F24                            VALUE +188.
    88  WS-KEY-CLEAR                          VALUE +189.
    88  WS-KEY-ENTER                          VALUE +241 +63.
    88  WS-KEY-HELP                           VALUE +243.
    88  WS-KEY-PAGE-UP                        VALUE +244.
    88  WS-KEY-PAGE-DOWN                      VALUE +245.
    88  WS-KEY-PRINT                          VALUE +246.
    88  WS-KEY-REC-BKSP                       VALUE +248.
    88  WS-KEY-AUTO-ENTER                     VALUE +63.
01  WS-ROW-BYTE.
    07 FILLER                      PIC  X(001).
    07 WS-ROW-BYTE-HEX             PIC  X(001).
01  WS-ROW-POS
    REDEFINES WS-ROW-BYTE          PIC  9(002) COMP-4.
01  WS-COL-BYTE.
    07 FILLER                      PIC  X(001).
    07 WS-COL-BYTE-HEX             PIC  X(001).
01  WS-COL-POS
    REDEFINES WS-COL-BYTE          PIC  9(002) COMP-4.
01  GX-INDICATORS.
    05 FILLER       OCCURS 99      PIC 1 INDICATOR 1.
01  FILLER REDEFINES GX-INDICATORS.
    05 GX-ATT-IND.
       10 IND-ARRAY OCCURS 79      PIC 1.
    05 MISCELANEOUS-INDICATORS.
       10 FILLER    OCCURS 10      PIC 1.
    05 SUBFILE-INDICATORS.
       10 FILLER    OCCURS 10      PIC 1.
01  GX-S-CPOS.
    05 GX-S-ROW                PIC S9(003).
    05 GX-S-COL                PIC S9(003).
