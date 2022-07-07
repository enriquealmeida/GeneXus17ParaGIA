MAINLOOP:
        RCLRSC
        SNDF RCDFMT(MSGCTL)
        SNDF RCDFMT(MAINLOOP)
        SNDRCVF RCDFMT(CMDLIN)
        RCVF RCDFMT(MAINLOOP)
        RMVMSG CLEAR(*ALL)

        IF (&IN73 *EQ '1') GOTO ENDPGM

        IF (&IN75 *EQ '1') GOTO LOOP

        IF (&IN82 *EQ '1') GOTO ENDPGM

        IF (%SST(&OPT 2 1) *EQ ' ') CHGVAR &OPT VALUE(' ' *CAT +
                                                       %SST(&OPT 1 1))

