GX-IOERR-LOOP.

    EVALUATE TRUE
       WHEN GX-IO-OK OR GX-IO-EOF
          CONTINUE
       WHEN GX-IO-LOCKED-FILE OR
            GX-IO-LOCKED-RCD
          ADD 1 TO GX-LOCK-COUNT
          IF GX-LOCK-COUNT >= 10
             MOVE SPACES TO GX-RETRY-MSG
             STRING #1 GX-IO-XFI
                 DELIMITED BY SIZE INTO GX-RETRY-MSG
             CALL 'GXAR' USING GX-RETRY-MSG GX-RETRY-ANS
             IF GX-RETRY-ANS = 'R'
                MOVE 0 TO GX-LOCK-COUNT
             ELSE
                MOVE 'GXM9999' TO GX-MSGID
                MOVE #4 TO GX-MSGDTA
                GO TO GX-ABORT
             END-IF
          END-IF
       WHEN OTHER
          MOVE 'GXM9999' TO GX-MSGID
          MOVE SPACES    TO GX-MSGDTA
          STRING #2 V-FS ') ' GX-IO-OP ' ' GX-IO-XFI
              DELIMITED BY SIZE INTO GX-MSGDTA
          GO TO GX-ABORT
    END-EVALUATE.
