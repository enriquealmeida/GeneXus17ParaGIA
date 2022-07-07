        CHGVAR &OPTD 0
        IF (&OPT *NE ' ') DO
            CHGVAR &OPTD &OPT
            MONMSG MSGID(CPF0818) EXEC(DO)
                 RCVMSG
                 SNDPGMMSG MSGID(CPF9897) +
                           MSGF(QCPFMSG) +
 MSGDTA('#1') +
                           TOPGMQ(*SAME) +
                           MSGTYPE(*DIAG)
                 GOTO MAINLOOP
            ENDDO
        ENDDO
