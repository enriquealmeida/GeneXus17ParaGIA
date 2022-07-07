GX-ABORT.

    #1
    MOVE 'S'       TO GX-MSGTYPE
    CALL 'GXMSGS'  USING GX-MSGID
                         GX-MSGDTA
                         GX-MSGTYPE
    STOP RUN.
