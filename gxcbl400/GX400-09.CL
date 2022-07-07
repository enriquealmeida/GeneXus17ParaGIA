        IF (%SST(&CMD2 1 1) *EQ '?') DO
            CHGVAR  &IN70 '1'
            CALL QCMDCHK PARM(&CMD2 512)
            MONMSG MSGID(CPF6801) EXEC(DO)
                RCVMSG
                GOTO LOOP
            ENDDO
            MONMSG MSGID(CPF0000) EXEC(GOTO MAINLOOP)
        ENDDO
        IF (&CMD2 *NE ' ') DO
            CHGVAR  &IN70 '1'
            SNDPGMMSG MSG(&CMD2) TOPGMQ(*EXT) MSGTYPE(*RQS) +
                    KEYVAR(&KEYVAR)
            RCVMSG PGMQ(*EXT) MSGKEY(&KEYVAR) RMV(*NO)

            CHGVAR &CMDSTACK (&KEYVAR *CAT &CMDSTACK)
            CHGVAR &SX -3

            CALL QCMDEXC PARM(&CMD2 512)
            MONMSG MSGID(CPF0000) EXEC(DO)
                CHGVAR &CMD &CMD2
                CHGVAR &I (512 - &CMDLLEN)
                IF (%SST(&MSGCMD &CMDLLEN &I) *NE ' ') DO
                    CHGVAR &I (&CMDLLEN - 2)
                    CHGVAR %SST(&CMD &I 3) '...'
                    CHGVAR &LONGCMD 'X'
                    CHGVAR &PMTCMD &CMD
                ENDDO
                GOTO MAINLOOP
            ENDDO
        ENDDO
