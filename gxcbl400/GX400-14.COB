MOVE 1 TO GX-QCMDEXC-PTR
IF #1 NOT EQUAL #2
   STRING 'OVRPRTF FILE(#7) '
                  'HOLD(*YES) '
                  'PAGESIZE(#3 #4) '
                  'OVRFLW(#5) '
                  'SHARE(#6)'
          DELIMITED BY SIZE
          INTO GX-QCMDEXC-CMD
          POINTER GX-QCMDEXC-PTR
ELSE
   STRING 'OVRPRTF FILE(#7) '
                  'HOLD(*NO) '
                  'PAGESIZE(#3 #4) '
                  'OVRFLW(#5) '
                  'SHARE(#6)'
          DELIMITED BY SIZE
          INTO GX-QCMDEXC-CMD
          POINTER GX-QCMDEXC-PTR
END-IF

COMPUTE GX-QCMDEXC-CMDLEN = GX-QCMDEXC-PTR - 1
CALL 'QCMDEXC' USING GX-QCMDEXC-CMD
                     GX-QCMDEXC-CMDLEN

#8

SET  RPT-NO-RCDS-FOUND   TO TRUE
SET  RPT-HEADER-ON      TO TRUE
SET  RPT-FIRST-PAGE      TO TRUE
