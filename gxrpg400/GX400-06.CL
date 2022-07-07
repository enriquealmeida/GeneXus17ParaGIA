        CHGVAR  &IN70 '0'

        CHGVAR &CMD2 &CMD
        IF (&LONGCMD *EQ 'X' *AND &PMTCMD *EQ &CMD) CHGVAR &CMD2 &MSGCMD
        CHGVAR  &LONGCMD ' '

        IF (&IN74 *EQ '1') DO
            CHGVAR  &IN70 '1'
            IF (%SST(&CMD2 1 1) *NE '?') CHGVAR &CMD2 ('?' *CAT &CMD2)
        ENDDO

        IF (&IN79 *EQ '1') DO
            CHGVAR  &IN70 '1'
            IF (&CMD *EQ ' ') CHGVAR  &SX -3
            CHGVAR  &SX (&SX + 4)
            IF (&SX *GT 400) CHGVAR &SX 1
            CHGVAR  &KEYVAR %SST(&CMDSTACK &SX 4)
            IF (&SX *EQ 1 *AND &KEYVAR *EQ ' ') GOTO MAINLOOP
            IF (&KEYVAR *EQ ' ') DO
                CHGVAR &KEYVAR &CMDSTACK
                CHGVAR &SX     1
            ENDDO
            RCVMSG PGMQ(*EXT) MSGKEY(&KEYVAR) RMV(*NO) MSG(&MSGCMD)
            CHGVAR &CMD &MSGCMD
            CHGVAR &I (512 - &CMDLLEN)
            IF (%SST(&MSGCMD &CMDLLEN &I) *NE ' ') DO
                CHGVAR &I (&CMDLLEN - 3)
                CHGVAR %SST(&CMD &I 3) '...'
                CHGVAR &LONGCMD 'X'
                CHGVAR &PMTCMD &CMD
            ENDDO
            GOTO MAINLOOP
        ENDDO

        IF (&OPT *NE ' ' *AND &CMD2 *NE ' ') DO
            SNDPGMMSG MSGID(CPF9897) +
                      MSGF(QCPFMSG) +
 MSGDTA('#1') +
                      TOPGMQ(*SAME) +
                      MSGTYPE(*DIAG)
            GOTO MAINLOOP
        ENDDO

