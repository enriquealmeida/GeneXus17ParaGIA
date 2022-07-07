GX-TODAY.

    IF GX-TDAY-SW = 0
       MOVE  1 TO GX-TDAY-SW
       MOVE #1 TO GX-TDAY-ADJ
       CALL 'GXFJD' USING GX-TDAY-ADJ
                          GX-TDAY-DATE
    END-IF.

