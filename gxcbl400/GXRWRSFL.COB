REWRITE-SUBFILE.

    MOVE SUBFILE-FLAGS      TO SUBFILE-INDICATORS
    MOVE MISCELANEOUS-FLAGS TO MISCELANEOUS-INDICATORS
    REWRITE SUBFILE GX-CRT-RECORD
            FORMAT IS GX-WS-FMT
            INDICATORS ARE GX-INDICATORS
            INVALID KEY
               CONTINUE
    END-REWRITE
    MOVE ZEROES TO GX-ATT-IND.

