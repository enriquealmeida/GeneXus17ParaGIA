MOVE WS-AID-BYTE TO WS-AID-BYTE-SAVED
PERFORM READ-SCREEN
   TEST AFTER
  UNTIL WS-KEY-ENTER OR
        #1
IF WS-KEY-ENTER
   SET GX-CONFIRMED TO TRUE
ELSE
   SET GX-NOT-CONFIRMED TO TRUE
END-IF
MOVE WS-AID-BYTE-SAVED TO WS-AID-BYTE
