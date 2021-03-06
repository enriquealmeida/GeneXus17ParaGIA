CLR-PP-SCREEN.

    MOVE 'CLSPPS' TO GX-WS-FMT
    PERFORM WRITE-SCREEN.

GX-POPUP-MSG-MANAGER.

    IF MSG-COUNTER NOT GREATER ZERO
       MOVE SPACES TO MSG-DESCRIPTION
       PERFORM GX-ADD-MSG
    END-IF

    PERFORM SEND-ALL-POPUP-MESSAGES

    MOVE MISCELANEOUS-FLAGS TO MISCELANEOUS-INDICATORS

    SET  DISPLAY-SFLCTL     TO TRUE
    SET  DISPLAY-SFL        TO TRUE
    SET  SFL-END            TO TRUE
    MOVE SUBFILE-FLAGS      TO SUBFILE-INDICATORS

    WRITE GX-CRT-RECORD
          FORMAT IS 'GXPMCR'
          INDICATORS ARE GX-INDICATORS

    MOVE ZERO                TO MSG-COUNTER.

SEND-ALL-POPUP-MESSAGES.

    PERFORM CLEAR-OLD-POPUP-MESSAGES
    MOVE MSG-COUNTER TO MSG-COUNTER-LOOP
    PERFORM UNTIL MSG-COUNTER-LOOP EQUAL ZERO
       MOVE MSG-COUNTER-LOOP TO GX-SFLRELRECNBR

       MOVE MSG-TEXT (MSG-COUNTER-LOOP) TO MSTXT OF GXPUMSGR-O
       IF ERROR-MSG (MSG-COUNTER-LOOP)
          SET RECORD-HAS-ERRORS TO TRUE
       ELSE
          SET RECORD-HAS-NO-ERRORS TO TRUE
       END-IF

       MOVE SUBFILE-FLAGS      TO SUBFILE-INDICATORS
       MOVE MISCELANEOUS-FLAGS TO MISCELANEOUS-INDICATORS
       WRITE SUBFILE GX-CRT-RECORD
             FORMAT IS 'GXPUMSGR'
             INDICATORS ARE GX-INDICATORS
             INVALID KEY CONTINUE
       END-WRITE
       SUBTRACT 1 FROM MSG-COUNTER-LOOP
    END-PERFORM

    CONTINUE.

CLEAR-OLD-POPUP-MESSAGES.

    MOVE MISCELANEOUS-FLAGS TO MISCELANEOUS-INDICATORS

    SET  DISPLAY-SFLCTL     TO TRUE
    SET  NO-SFL-END         TO TRUE
    SET  NO-DISPLAY-SFL     TO TRUE
    MOVE SUBFILE-FLAGS      TO SUBFILE-INDICATORS

    WRITE GX-CRT-RECORD
          FORMAT IS 'GXPMCR'
          INDICATORS ARE GX-INDICATORS.

