IF GX-IO-DUP-KEY
   MOVE #1 TO MSG-DESCRIPTION
   PERFORM SEND-ERROR-MSG-WS
END-IF
IF GX-IO-OK
   PERFORM #2
   IF NO-ERRORS-FOUND
      PERFORM #3
         THRU #4
      IF NO-ERRORS-FOUND
         PERFORM #5
         IF NO-ERRORS-FOUND
            PERFORM #6
            IF NO-ERRORS-FOUND
               PERFORM #7
            END-IF
         END-IF
      END-IF
   END-IF
END-IF
