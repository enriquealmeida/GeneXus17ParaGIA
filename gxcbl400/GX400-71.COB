MOVE 0 TO GX-SFL1STMOD
PERFORM TEST AFTER
   UNTIL ERRORS-FOUND OR NO-MORE-SFL-REC
   PERFORM #2
   IF MORE-SFL-REC
      IF GX-SFL1STMOD = 0
         MOVE GX-SFLRELRECNBR TO GX-SFL1STMOD
      END-IF
      PERFORM #1
      SET MODIFIED-MARK-ON TO TRUE
      PERFORM #3
      SET MODIFIED-MARK-OFF TO TRUE
   END-IF
END-PERFORM

IF NO-ERRORS-FOUND AND GX-SFL1STMOD > 0
   MOVE GX-SFL1STMOD TO GX-SFLRECNBR

