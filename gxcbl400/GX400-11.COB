WORKING-STORAGE SECTION.

01  GX-INT-FMT-DATE.
    05 YY                          PIC  9(004).
    05 MM                          PIC  9(002).
    05 DD                          PIC  9(002).
01  GX-USR-FMT-DATE.
    05 #1                          PIC  9(002).
    05 #2                          PIC  9(002).
    05 #3                          PIC  9(002).
01  GX-USR-FMT-DATE-W-C.
    05 #1                          PIC  9(#5).
    05 #2                          PIC  9(002).
    05 #3                          PIC  9(#6).
01  GX-EXT-DTIME.
    05 YY                          PIC  9(004).
    05 GX-EXT-DTIME-S1             PIC  X.
    05 MM                          PIC  9(002).
    05 GX-EXT-DTIME-S2             PIC  X.
    05 DD                          PIC  9(002).
    05 GX-EXT-DTIME-S3             PIC  X.
    05 HH                          PIC  9(002).
    05 GX-EXT-DTIME-D1             PIC  X.
    05 MIN                         PIC  9(002).
    05 GX-EXT-DTIME-D2             PIC  X.
    05 SS                          PIC  9(002).
    05 GX-EXT-DTIME-D3             PIC  X.
    05 MS                          PIC  9(006).
01  GX-INT-FMT-DTIME.
    05 YY                          PIC  9(004).
    05 FILLER                      PIC  X(001) VALUE '-'.
    05 MM                          PIC  9(002).
    05 FILLER                      PIC  X(001) VALUE '-'.
    05 DD                          PIC  9(002).
    05 FILLER                      PIC  X(001) VALUE '-'.
    05 HH                          PIC  9(002).
    05 FILLER                      PIC  X(001) VALUE '.'.
    05 MIN                         PIC  9(002).
    05 FILLER                      PIC  X(001) VALUE '.'.
    05 SS                          PIC  9(002).
    05 FILLER                      PIC  X(001) VALUE '.'.
    05 MS                          PIC  X(006) VALUE '000000'.
01  GX-BOOLEAN                     PIC S9(001)       VALUE 1.
    88 GX-TRUE                                       VALUE 1.
    88 GX-FALSE                                      VALUE 0.
01  GX-FIRST-TIME                  PIC  X(001)       VALUE '1'.
01  GX-COMMIT                      PIC S9      COMP-3.
01  GX-ROLLBACK                    PIC S9      COMP-3.
01  GX-RETURN                      PIC S9      COMP-3.
01  GX-EXIT-LEVEL                  PIC S9      COMP-3.
01  GX-ALARM                       PIC S9      COMP-3.
01  GX-QCMDEXC-PTR                 PIC S9(003) COMP-3 VALUE 1.
01  GX-QCMDEXC-CMD                 PIC  X(3000).
01  GX-QCMDEXC-CMDLEN              PIC S9(10)V9(5) COMP-3.
01  GX-STATUS-VARIABLES.
    05 GX-CSRROW                   PIC S9(002) COMP-4.
    05 GX-CSRCOL                   PIC S9(002) COMP-4.
    05 GX-LREC                     PIC S9(004) COMP-4.
    05 GX-SFL-PAGE-COUNT           PIC S9(004) COMP-4.
    05 GX-SFLRELRECNBR             PIC  9(004) COMP-4.
    05 GX-WS-STATUS                PIC  X(002).
    05 GX-SFLRECNBR                PIC S9(004) COMP-4.
    05 GX-S-SFLRECNBR              PIC S9(004) COMP-4.
    05 GX-NBR-OF-SELECTIONS        PIC S9(003) COMP-3.
    05 GX-WS-FMT                   PIC  X(010).
    05 GX-MODETXT                  PIC  X(010).
    05 GX-MODE-FLAG                PIC  X(003).
       88 INSERT-MODE                                VALUE 'INS'.
       88 UPDATE-MODE                                VALUE 'UPD'.
       88 DELETE-MODE                                VALUE 'DLT'.
       88 DISPLAY-MODE                               VALUE 'DSP'.
    05 MISCELANEOUS-FLAGS.
       10 FILLER                   PIC 1.
       10 FILLER                   PIC 1.
          88 INSRCD                                  VALUE B'1'.
          88 NO-INSRCD                               VALUE B'0'.
       10 FILLER                   PIC 1.
          88 DELRCD                                  VALUE B'1'.
          88 NO-DELRCD                               VALUE B'0'.
       10 FILLER                   PIC 1.
          88 MODIFIED-MARK-ON                        VALUE B'1'.
          88 MODIFIED-MARK-OFF                       VALUE B'0'.
       10 FILLER                   PIC 1.
          88 RECORD-HAS-ERRORS                       VALUE B'1'.
          88 RECORD-HAS-NO-ERRORS                    VALUE B'0'.
       10 FILLER                   PIC 1.
          88 ALWAYS-ON                               VALUE B'1'.
       10 FILLER                   PIC 1.
          88 ALARM-ON                                VALUE B'1'.
          88 ALARM-OFF                               VALUE B'0'.
       10 FILLER        OCCURS  3  PIC 1.
    05 SUBFILE-FLAGS.
       10 FILLER                   PIC 1.
       10 FILLER                   PIC 1.
          88 DISPLAY-SFL                             VALUE B'1'.
          88 NO-DISPLAY-SFL                          VALUE B'0'.
       10 FILLER                   PIC 1.
          88 DISPLAY-SFLCTL                          VALUE B'1'.
          88 NO-DISPLAY-SFLCTL                       VALUE B'0'.
       10 FILLER                   PIC 1.
          88 SFL-END                                 VALUE B'1'.
          88 NO-SFL-END                              VALUE B'0'.
    05 LEVEL-FLAGS.
       10 FILLER                   PIC 1.
          88 ERRORS-FOUND                            VALUE B'1'.
          88 NO-ERRORS-FOUND                         VALUE B'0'.
       10 FILLER                   PIC 1.
       10 FILLER                   PIC 1.
          88 MORE-SFL-REC                            VALUE B'1'.
          88 NO-MORE-SFL-REC                         VALUE B'0'.
       10 FILLER                   PIC 1.
       10 FILLER                   PIC 1.
       10 FILLER                   PIC 1.
          88 RECORD-INSERTED                         VALUE B'1'.
          88 RECORD-NOT-INSERTED                     VALUE B'0'.
       10 FILLER                   PIC 1.
          88 RECORD-UPDATED                          VALUE B'1'.
          88 RECORD-NOT-UPDATED                      VALUE B'0'.
       10 FILLER                   PIC 1.
          88 RECORD-DELETED                          VALUE B'1'.
          88 RECORD-NOT-DELETED                      VALUE B'0'.
       10 FILLER                   PIC 1.
          88 RECORD-DISPLAYED                        VALUE B'1'.
          88 RECORD-NOT-DISPLAYED                    VALUE B'0'.
       10 FILLER                   PIC 1.
          88 RECORD-PROCESSED                        VALUE B'1'.
          88 NO-RECORD-PROCESSED                     VALUE B'0'.
       10 FILLER                   PIC 1.
          88 LOOKUP-AVAILABLE                        VALUE B'1'.
          88 LOOKUP-NOT-AVAILABLE                    VALUE B'0'.
       10 FILLER                   PIC 1.
          88 SELECTION-DISPLAYED                     VALUE B'1'.
          88 SELECTION-NOT-DISPLAYED                 VALUE B'0'.
01  MSG-DESCRIPTION                PIC  X(#4).
01  FILLER.
    05 MSG-ARRAY       OCCURS 100.
       10 MSG-TYPE                 PIC  X(001).
          88 ERROR-MSG                               VALUE '1'.
          88 WARNING-MSG                             VALUE '0'.
       10 MSG-TEXT                 PIC  X(#4).
01  MSG-COUNTER                    PIC  9(003) COMP-4.
01  MSG-COUNTER-LOOP               PIC  9(003) COMP-4.
