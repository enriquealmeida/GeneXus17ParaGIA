
CLOSE GX-PRINTER
IF #1 NOT EQUAL #2
   MOVE 1 TO GX-QCMDEXC-PTR
   STRING 'DSPSPLF FILE(#3) '
                  'SPLNBR(*LAST)'
          DELIMITED BY SIZE
          INTO GX-QCMDEXC-CMD
          POINTER GX-QCMDEXC-PTR
   COMPUTE GX-QCMDEXC-CMDLEN = GX-QCMDEXC-PTR - 1
   CALL 'QCMDEXC' USING GX-QCMDEXC-CMD
                        GX-QCMDEXC-CMDLEN

   MOVE 1 TO GX-QCMDEXC-PTR
   STRING 'DLTSPLF FILE(#3) '
                  'SPLNBR(*LAST)'
          DELIMITED BY SIZE
          INTO GX-QCMDEXC-CMD
          POINTER GX-QCMDEXC-PTR
   COMPUTE GX-QCMDEXC-CMDLEN = GX-QCMDEXC-PTR - 1
   CALL 'QCMDEXC' USING GX-QCMDEXC-CMD
                        GX-QCMDEXC-CMDLEN
END-IF

