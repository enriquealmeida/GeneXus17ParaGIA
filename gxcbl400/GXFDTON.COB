GX-DTON.
    MOVE ZERO TO GX-DTON-OUTNUM
    IF GX-DTON-INDATE > ZEROES
       MOVE CORR GX-DTON-INDATE TO GX-DTON-DW
       IF MM OF GX-DTON-DW GREATER 2
          SUBTRACT 3 FROM MM OF GX-DTON-DW
       ELSE
          ADD 9 TO MM OF GX-DTON-DW
          SUBTRACT 1 FROM YY OF GX-DTON-DW
       END-IF
       COMPUTE GX-DTON-D = (( 153 * MM OF GX-DTON-DW) + 2) / 5
       COMPUTE GX-DTON-L-4 = YY OF GX-DTON-DW / 4
       COMPUTE GX-DTON-L-400 = YY OF GX-DTON-DW / 400
       COMPUTE GX-DTON-L-100 = YY OF GX-DTON-DW / 100
       COMPUTE GX-DTON-OUTNUM = YY OF GX-DTON-DW * 365 +
                                GX-DTON-L-4 -
                                GX-DTON-L-100 +
                                GX-DTON-L-400 +
                                GX-DTON-D +
                                DD OF GX-DTON-DW
    END-IF.
