
        IF (&OPT *NE ' ') DO
            SNDPGMMSG MSGID(CPF9897) +
                      MSGF(QCPFMSG) +
 MSGDTA('#1') +
                      TOPGMQ(*SAME) +
                      MSGTYPE(*DIAG)
            GOTO MAINLOOP 
        ENDDO
