IF CCIO-RCD-NOT-FOUND
   MOVE #2 TO #1
   SET  CCIO-RCD-FOUND TO TRUE
ELSE
   ADD #2 TO #1
END-IF
