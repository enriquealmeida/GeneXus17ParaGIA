GX-TDTE.
    MOVE 0 TO GX-TDTE-OUTNUM
    IF GX-TDTE-INDATE NUMERIC
       MOVE 1 TO GX-TDTE-OUTNUM
       IF GX-TDTE-INDATE NOT EQUAL ZEROES
          MOVE 0 TO GX-TDTE-OUTNUM
          MOVE CORR GX-TDTE-INDATE TO GX-TDTE-DW
          IF YY OF GX-TDTE-DW LESS 100
             ADD 1900 TO YY OF GX-TDTE-DW
          END-IF
          DIVIDE YY OF GX-TDTE-DW BY 4 GIVING GX-TDTE-Q
                         REMAINDER GX-TDTE-R-4
          DIVIDE YY OF GX-TDTE-DW BY 400 GIVING GX-TDTE-Q
                         REMAINDER GX-TDTE-R-400
          DIVIDE YY OF GX-TDTE-DW BY 100 GIVING GX-TDTE-Q
                         REMAINDER GX-TDTE-R-100
          MOVE 29 TO GX-TDTE-MFD
          IF (GX-TDTE-R-4 EQUAL 0 AND
             GX-TDTE-R-100 NOT EQUAL 0) OR
             GX-TDTE-R-400 EQUAL 0
             MOVE 30 TO GX-TDTE-MFD
          END-IF
          IF (MM OF GX-TDTE-DW GREATER 0 AND LESS 13) AND
             (DD OF GX-TDTE-DW GREATER 0 AND LESS 32) AND
             (DD OF GX-TDTE-DW LESS 31 OR GX-TDTE-31-DM) AND
             (DD OF GX-TDTE-DW LESS GX-TDTE-MFD OR
              NOT GX-TDTE-2-M)
             MOVE 1 TO GX-TDTE-OUTNUM
          END-IF
       END-IF
    END-IF.
