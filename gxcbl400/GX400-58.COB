IF GX-IO-OK AND NO-ERRORS-FOUND
   PERFORM #1
   IF NO-ERRORS-FOUND
      PERFORM #2
         THRU #3
      IF NO-ERRORS-FOUND
         PERFORM #4
         IF NO-ERRORS-FOUND
            PERFORM #5
            IF NO-ERRORS-FOUND
               PERFORM #6
            END-IF
         END-IF
      END-IF
   END-IF
END-IF
